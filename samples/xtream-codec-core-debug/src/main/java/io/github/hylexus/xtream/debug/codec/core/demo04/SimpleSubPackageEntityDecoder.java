/*
 * Copyright (c) 2024 xtream-codec
 * xtream-codec is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package io.github.hylexus.xtream.debug.codec.core.demo04;

import io.github.hylexus.xtream.codec.common.bean.BeanMetadata;
import io.github.hylexus.xtream.codec.common.utils.XtreamByteReader;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.core.BeanMetadataRegistry;
import io.github.hylexus.xtream.codec.core.EntityDecoder;
import io.github.hylexus.xtream.codec.core.impl.SimpleBeanMetadataRegistry;
import io.github.hylexus.xtream.debug.codec.core.demo04.spec.BaseJt808Msg;
import io.github.hylexus.xtream.debug.codec.core.demo04.spec.Jt808MsgHeader;
import io.github.hylexus.xtream.debug.codec.core.demo04.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.debug.codec.core.demo04.spec.SimpleCache;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleSubPackageEntityDecoder extends EntityDecoder {

    protected final SimpleCache cache;
    private final EntityDecoder delegateDecoder;
    protected final ByteBufAllocator allocator = ByteBufAllocator.DEFAULT;

    public SimpleSubPackageEntityDecoder() {
        this(new SimpleBeanMetadataRegistry());
    }

    public SimpleSubPackageEntityDecoder(BeanMetadataRegistry beanMetadataRegistry) {
        this.delegateDecoder = new EntityDecoder(beanMetadataRegistry);
        this.cache = new SimpleCache();
    }

    /**
     * 子包没有到齐则返回 {@code null}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T decode(ByteBuf source, BeanMetadata beanMetadata, Object instance) {
        // BaseJt808Msg 子类 --> 有可能是子包
        if (instance instanceof BaseJt808Msg jt808Msg) {
            return (T) this.tryMergeSubPackagesOrNull(jt808Msg, source);
        }
        // 不是 BaseJt808Msg 类型 --> 直接视为完整包
        return super.decode(source, beanMetadata, instance);
    }

    private BaseJt808Msg tryMergeSubPackagesOrNull(BaseJt808Msg jt808Msg, ByteBuf source) {
        final Jt808MsgHeader header = this.decodeHeader(source.slice());
        jt808Msg.setHeader(header);
        // 校验码
        jt808Msg.setCheckSum(source.getByte(source.readableBytes() - 1));

        final ByteBuf bodySlice = source.slice(header.msgBodyStartIndex(), header.msgBodyLength());
        // 分包消息 ==> 缓存或合并后解析
        if (header.isSubPackage()) {
            final String key = this.buildSubPackageCacheKey(header);

            final Map<Integer, BaseJt808Msg> subPackages = this.cacheAndGetSubPackages(jt808Msg, key, bodySlice);
            if (subPackages.size() == header.getTotalPackageCount()) {
                return this.mergeAndDecode(jt808Msg, key, bodySlice);
            }
            return null;
        }

        // 不是分包消息 ==> 常规解析
        return this.delegateDecoder.decode(bodySlice, jt808Msg);
    }

    private Map<Integer, BaseJt808Msg> cacheAndGetSubPackages(BaseJt808Msg jt808Msg, String key, ByteBuf bodySlice) {
        final Map<Integer, BaseJt808Msg> subPackages = this.cache.get(key, k -> new ConcurrentHashMap<>());
        jt808Msg.setInternalTemporarySubPackageBodyByteBuf(bodySlice.copy());
        final BaseJt808Msg old = subPackages.put(jt808Msg.getHeader().getCurrentPackageNo(), jt808Msg);
        if (old != null) {
            XtreamBytes.releaseBuf(old.getInternalTemporarySubPackageBodyByteBuf());
        }
        return subPackages;
    }

    private <T extends BaseJt808Msg> T mergeAndDecode(BaseJt808Msg jt808Msg, String key, ByteBuf bodySlice) {
        final List<BaseJt808Msg> subPackages = this.cache.get(key, k -> new ConcurrentHashMap<>())
                .values()
                .stream()
                .sorted(Comparator.comparing(it -> it.getHeader().getCurrentPackageNo()))
                .toList();

        final CompositeByteBuf compositeByteBuf = this.allocator.compositeBuffer(subPackages.size());
        try {
            int totalLength = 0;
            for (final BaseJt808Msg subPackage : subPackages) {
                compositeByteBuf.addComponents(true, subPackage.getInternalTemporarySubPackageBodyByteBuf());
                // 累加中间包的长度(下面重新生成一个新的 `消息体属性`)
                totalLength += subPackage.getHeader().msgBodyLength();
            }
            final int newMsgBodyProps = Jt808MsgHeader.generateMsgBodyProps(
                    totalLength, 0, false,
                    0, jt808Msg.getHeader().getVersion()
            );
            jt808Msg.getHeader().setMsgBodyProperty(newMsgBodyProps);

            final BaseJt808Msg lastPackage = subPackages.getLast();
            jt808Msg.setCheckSum(lastPackage.getCheckSum());
            jt808Msg.getHeader().setMsgSerialNo(lastPackage.getHeader().getMsgSerialNo());
            jt808Msg.setInternalTemporarySubPackageBodyByteBuf(null);

            return this.delegateDecoder.decode(compositeByteBuf, jt808Msg);
        } finally {
            this.cache.invalidate(key);
            compositeByteBuf.release();
        }
    }

    private Jt808MsgHeader decodeHeader(ByteBuf source) {
        final XtreamByteReader reader = XtreamByteReader.of(source);
        final Jt808MsgHeader header = new Jt808MsgHeader();

        // 消息ID
        reader.readU16(header::setMsgId)
                // 消息体属性
                .readU16(header::setMsgBodyProperty);

        final Jt808ProtocolVersion version = Jt808ProtocolVersion.detectVersion(header, source);
        header.setVersion(version);
        if (version.getVersionBit() == 1) {
            reader
                    // 协议版本号 v2019 新增
                    .readU8(header::setMsgVersionNumber)
                    // 终端手机号或设备ID
                    .readBcd(10, header::setTerminalPhoneNo)
                    // 消息流水号
                    .readU16(header::setMsgSerialNo);
        } else {
            // 协议版本号 v2019 之前没有该属性
            header.setMsgVersionNumber(0);
            reader
                    // 终端手机号或设备ID
                    .readBcd(6, header::setTerminalPhoneNo)
                    // 消息流水号
                    .readU16(header::setMsgSerialNo);
        }


        if (header.isSubPackage()) {
            // 消息包总数
            reader.readU16(header::setTotalPackageCount)
                    // 包序号(word(16)) 从 1 开始
                    .readU16(header::setCurrentPackageNo);
        }

        return header;
    }

    protected String buildSubPackageCacheKey(Jt808MsgHeader request) {
        return String.format("%s_%d_%d", request.getTerminalPhoneNo(), request.getMsgId(), request.getTotalPackageCount());
    }

}
