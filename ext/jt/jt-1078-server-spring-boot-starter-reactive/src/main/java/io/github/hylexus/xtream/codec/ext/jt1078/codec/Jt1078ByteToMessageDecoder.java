/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.hylexus.xtream.codec.ext.jt1078.codec;

import io.github.hylexus.xtream.codec.common.utils.XtreamByteReader;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078RequestHeader;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.Jt1078TerminalIdConverter;
import io.github.hylexus.xtream.codec.ext.jt1078.spec.impl.DefaultJt1078RequestHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 这个实现来自 <a href="https://gitee.com/hui_hui_zhou/open-source-repository">sky/jt1078</a> 的作者 sky
 */
public class Jt1078ByteToMessageDecoder extends ByteToMessageDecoder {

    public record Jt1078PackageInfo(
            Jt1078RequestHeader header,
            ByteBuf body) {

        @SuppressWarnings("Redundent")
        public Jt1078PackageInfo {
        }
    }

    private final Jt1078TerminalIdConverter terminalIdConverter;

    private int simLength = 0;

    public Jt1078ByteToMessageDecoder(Jt1078TerminalIdConverter terminalIdConverter) {
        this.terminalIdConverter = terminalIdConverter;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        this.decodePackage(in, out);
    }

    enum State {
        // 报文格式错误
        PACKAGE_FORMAT_ERROR,
        // 等待更多数据(报文不完整)
        WAITING_FOR_MORE_DATA_ARRIVE,
        // 使用 BCD[6] 解析成功
        DECODED_AS_SIM_LENGTH_6,
        // 尝试使用 BCD[10] 解析
        RETRY_WITH_SIM_LENGTH_10,
    }

    // for Junit
    void decodePackage(ByteBuf in, List<Object> out) {
        if (this.simLength == 0) {
            final State state = this.decodePackage(in, 6, out);
            switch (state) {
                case WAITING_FOR_MORE_DATA_ARRIVE -> {
                    // ignored
                }
                case DECODED_AS_SIM_LENGTH_6 -> this.simLength = 6;
                case RETRY_WITH_SIM_LENGTH_10 -> {
                    this.simLength = 10;
                    final State newState = this.decodePackage(in, 10, out);
                    if (newState == State.RETRY_WITH_SIM_LENGTH_10) {
                        throw new IllegalStateException("Parse JT/T 1078 stream-data error");
                    }
                }
                case PACKAGE_FORMAT_ERROR -> throw new IllegalStateException("Parse JT/T 1078 stream-data error");
                default -> throw new IllegalStateException("Unexpected value: " + state);
            }
        } else {
            this.decodePackage(in, this.simLength, out);
        }
    }


    State decodePackage(ByteBuf in, int simLen, List<Object> out) {
        final int readerIndex = in.readerIndex();
        if (in.readableBytes() < simLen + 24) {
            return State.WAITING_FOR_MORE_DATA_ARRIVE;
        }

        in.markReaderIndex();
        if (in.getInt(readerIndex) != 0x30316364) {
            return State.PACKAGE_FORMAT_ERROR;
        }

        final int dataType = (in.getByte(readerIndex + simLen + 9) >> 4) & 0x0F;
        final int lengthFieldOffset = switch (dataType) {
            // 透传
            case 0x04 -> simLen + 10;
            // 音频
            case 0x03 -> simLen + 18;
            // 视频
            default -> simLen + 22;
        };
        final int packageSize = in.getShort(readerIndex + lengthFieldOffset) + lengthFieldOffset + 2;
        if (packageSize < 0) {
            return State.RETRY_WITH_SIM_LENGTH_10;
        }
        // 下一包的开始标记
        if (in.readableBytes() < packageSize + 4) {
            in.resetReaderIndex();
            return State.WAITING_FOR_MORE_DATA_ARRIVE;
        }
        if (in.getInt(readerIndex + packageSize) != 0x30316364) {
            return State.RETRY_WITH_SIM_LENGTH_10;
        }
        this.doParsePackageInfo(out, in.readSlice(packageSize), simLen);
        return State.DECODED_AS_SIM_LENGTH_6;
    }

    private void doParsePackageInfo(List<Object> out, ByteBuf in, int simLen) {
        final XtreamByteReader reader = XtreamByteReader.of(in.skipBytes(4));
        final DefaultJt1078RequestHeader headerBuilder = new DefaultJt1078RequestHeader();
        // V + P + X + CC
        reader.readU8(headerBuilder::offset4);
        // M + PT
        reader.readU8(headerBuilder::markerBitAndPayloadType);
        // 包序号
        reader.readU16(headerBuilder::sequenceNumber);
        // SIM
        headerBuilder.simLength(simLen);
        final String sim = reader.readBcd(simLen);
        // SIM-rawSim
        headerBuilder.rawSim(sim);
        final String convertedSim = this.terminalIdConverter.convert(sim);
        // SIM-convertedSim
        headerBuilder.convertedSim(convertedSim);
        // 通道号
        reader.readU8(headerBuilder::channelNumber);
        // 数据类型 + 分包处理标记
        final short dataTypeAndSubPackageIdentifier = reader.readU8();
        headerBuilder.dataTypeAndSubPackageIdentifier(dataTypeAndSubPackageIdentifier);
        final byte dataType = Jt1078RequestHeader.dataTypeValue(dataTypeAndSubPackageIdentifier);
        final boolean hasTimestampField = Jt1078RequestHeader.hasTimestampField(dataType);
        final boolean hasFrameIntervalFields = Jt1078RequestHeader.hasFrameIntervalFields(dataType);
        if (hasTimestampField) {
            // 时间戳
            reader.readI64(headerBuilder::timestamp);
        }
        if (hasFrameIntervalFields) {
            // Last I Frame Interval
            reader.readU16(headerBuilder::lastIFrameInterval);
            // Last Frame Interval
            reader.readU16(headerBuilder::lastFrameInterval);
        }

        // 数据体长度字段
        final int bodyLength = reader.readU16();
        headerBuilder.msgBodyLength(bodyLength);
        final ByteBuf body = reader.readable().readSlice(bodyLength);
        final Jt1078PackageInfo packageInfo = new Jt1078PackageInfo(headerBuilder, body);
        out.add(packageInfo);
    }
}
