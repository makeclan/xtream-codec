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

package io.github.hylexus.xtream.codec.ext.jt808.dashboard.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.hylexus.xtream.codec.common.utils.DefaultXtreamClassScanner;
import io.github.hylexus.xtream.codec.common.utils.FormatUtils;
import io.github.hylexus.xtream.codec.common.utils.XtreamBytes;
import io.github.hylexus.xtream.codec.common.utils.XtreamClassScanner;
import io.github.hylexus.xtream.codec.core.EntityCodec;
import io.github.hylexus.xtream.codec.core.tracker.CodecTracker;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808BytesProcessor;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808RequestDecoder;
import io.github.hylexus.xtream.codec.ext.jt808.codec.Jt808ResponseEncoder;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.bo.DecodedMessageMetadata;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.dto.DecodeMessageDto;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.dto.EncodeMessageDto;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808DashboardDebugMessage;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.values.Jt808EntityClassMetadata;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.vo.DecodedMessageVo;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.bo.EncodedMessageMetadata;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.vo.EncodedMessageVo;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.domain.vo.Jt808MessageHeaderMetadata;
import io.github.hylexus.xtream.codec.ext.jt808.dashboard.service.Jt808DashboardCodecService;
import io.github.hylexus.xtream.codec.ext.jt808.exception.BadRequestException;
import io.github.hylexus.xtream.codec.ext.jt808.extensions.handler.Jt808ResponseBody;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageCodecTracker;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808MessageDescriber;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Jt808DashboardCodecServiceImpl implements Jt808DashboardCodecService {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final EntityCodec entityCodec;
    private final Jt808RequestDecoder jt808RequestDecoder;
    private final Jt808ResponseEncoder responseEncoder;
    private final Jt808BytesProcessor jt808BytesProcessor;
    private final Map<String, Jt808EntityClassMetadata> entityClassMapping;

    public Jt808DashboardCodecServiceImpl(EntityCodec entityCodec, Jt808RequestDecoder jt808RequestDecoder, Jt808ResponseEncoder responseEncoder, Jt808BytesProcessor jt808BytesProcessor) {
        this.entityCodec = entityCodec;
        this.jt808RequestDecoder = jt808RequestDecoder;
        this.responseEncoder = responseEncoder;
        this.jt808BytesProcessor = jt808BytesProcessor;
        this.entityClassMapping = this.scanEntityClass();
    }

    @Override
    public List<DecodedMessageVo> encodeWithTracker(EncodeMessageDto dto) {
        final Jt808EntityClassMetadata classMetadata = this.entityClassMapping.get(dto.getBodyClass());
        if (classMetadata == null) {
            throw new BadRequestException("未知实体类: " + dto.getBodyClass());
        }
        final Object body;
        try {
            body = this.objectMapper.readValue(this.objectMapper.writeValueAsString(dto.getBodyData()), classMetadata.targetClass());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        final List<EncodedMessageMetadata> metadataList = this.getEncodeMetadata(dto, body);

        if (metadataList.size() > 1) {
            return metadataList.stream().map(it -> {
                final Jt808DashboardDebugMessage instance = toEntityInstance(it, null, it.getBodyBeforeEncryption());
                try {
                    final CodecTracker tracker = new CodecTracker();
                    final ByteBuf temp = ByteBufAllocator.DEFAULT.buffer();
                    try {
                        this.entityCodec.encode(instance, temp, tracker);
                    } finally {
                        XtreamBytes.releaseBuf(temp);
                    }
                    return new DecodedMessageVo()
                            .setDetails(tracker.getRootSpan())
                            .setRawHexString("7e" + it.getRawHexString() + "7e")
                            .setEscapedHexString("7e" + it.getEscapedHexString() + "7e");
                } finally {
                    if (instance.getBody() instanceof ByteBuf byteBuf) {
                        XtreamBytes.releaseBuf(byteBuf);
                    }
                }
            }).toList();
        }
        final EncodedMessageMetadata vo = metadataList.getFirst();
        final Jt808DashboardDebugMessage entityInstance = toEntityInstance(vo, body, null);
        final CodecTracker tracker = new CodecTracker();
        final ByteBuf temp = ByteBufAllocator.DEFAULT.buffer();
        try {
            this.entityCodec.encode(entityInstance, temp, tracker);
        } finally {
            XtreamBytes.releaseBuf(temp);
        }
        return List.of(new DecodedMessageVo()
                .setDetails(tracker.getRootSpan())
                .setRawHexString("7e" + vo.getRawHexString() + "7e")
                .setEscapedHexString("7e" + vo.getEscapedHexString() + "7e")
        );
    }

    @Override
    public EncodedMessageVo decodeWithTracker(DecodeMessageDto dto) {
        final Jt808EntityClassMetadata classMetadata = this.entityClassMapping.get(dto.getBodyClass());
        if (classMetadata == null) {
            throw new BadRequestException("未知实体类: " + dto.getBodyClass());
        }
        final Jt808DashboardDebugMessage entityInstance = new Jt808DashboardDebugMessage().setBodyType(classMetadata.targetClass());
        final CodecTracker tracker = new CodecTracker();
        if (dto.getHexString().size() == 1) {
            final DecodedMessageMetadata parsed = this.parse(dto.getHexString().getFirst());
            final ByteBuf source = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, parsed.getDecryptedHexString() != null ? parsed.getDecryptedHexString() : parsed.getEscapedHexString());
            try {
                this.entityCodec.decode(entityInstance, source.slice(), tracker);
                return new EncodedMessageVo(new EncodedMessageVo.Single()
                        .setRawHexString(parsed.getOriginalHexString())
                        .setEscapedHexString(parsed.getEscapedHexString())
                        .setDetails(tracker.getRootSpan())
                );
            } finally {
                XtreamBytes.releaseBuf(source);
            }
        }
        final List<DecodedMessageMetadata> tempList = dto.getHexString().stream().map(this::parse).sorted(Comparator.comparing(it -> it.getHeader().subPackage().currentPackageNo())).toList();
        final Jt808RequestHeader firstHeader = tempList.getFirst().getHeader();
        int mergedBodyLength = 0;
        final CompositeByteBuf bodyBytes = ByteBufAllocator.DEFAULT.compositeBuffer();
        for (final DecodedMessageMetadata temp : tempList) {
            final ByteBuf byteBuf = XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT,
                    temp.getHeader().messageBodyProps().encryptionType() == 0
                            ? temp.getEscapedBody()
                            : temp.getDecryptedBody()
            );
            bodyBytes.addComponent(true, byteBuf);
            mergedBodyLength += temp.getHeader().messageBodyLength();
        }
        final Jt808RequestHeader newHeader = firstHeader.mutate().messageBodyProps(firstHeader.messageBodyProps().mutate().messageBodyLength(mergedBodyLength).hasSubPackage(false).build()).build();
        final ByteBuf headerBytes = newHeader.encode();
        final CompositeByteBuf source = ByteBufAllocator.DEFAULT.compositeBuffer();
        try {
            source
                    .addComponent(true, headerBytes)
                    .addComponent(true, bodyBytes)
                    .addComponent(true, ByteBufAllocator.DEFAULT.buffer().writeByte(0));

            this.entityCodec.decode(entityInstance, source.slice(), tracker);
            final List<EncodedMessageVo.SubPackageMetadata> headerList = tempList.stream().map(it -> {
                final Jt808MessageHeaderMetadata headerMetadata = new Jt808MessageHeaderMetadata();
                final Jt808RequestHeader header = it.getHeader();
                headerMetadata.setMessageId(header.messageId());
                headerMetadata.setBodyProps(new Jt808MessageHeaderMetadata.BodyProps(header.messageBodyProps()));
                headerMetadata.setTerminalId(header.terminalId());
                headerMetadata.setFlowId(header.flowId());
                headerMetadata.setProtocolVersion(header.version());
                if (header.messageBodyProps().hasSubPackage()) {
                    headerMetadata.setSubPackageProps(new Jt808MessageHeaderMetadata.Jt808SubPackageProps(header.subPackage().totalSubPackageCount(), header.subPackage().currentPackageNo()));
                }

                return new EncodedMessageVo.SubPackageMetadata(
                        headerMetadata,
                        header.messageBodyProps().encryptionType() == 0
                                ? it.getEscapedBody()
                                : it.getDecryptedBody()
                );
            }).toList();
            return new EncodedMessageVo(new EncodedMessageVo.Multiple()
                    .setSubPackageMetadata(headerList)
                    .setDetails(tracker.getRootSpan())
                    .setMergedHexString("7e" + FormatUtils.toHexString(source) + "7e")
            );
        } finally {
            XtreamBytes.releaseBuf(source);
        }
    }

    @Override
    public Collection<Jt808EntityClassMetadata> getClassMetadata() {
        return this.entityClassMapping.values();
    }

    private String trim(String hex) {
        final String lowerCase = hex.toLowerCase();
        if (lowerCase.startsWith("7e") && lowerCase.endsWith("7e")) {
            return hex.substring(2, hex.length() - 2);
        }
        return hex;
    }

    private ByteBuf hexStringToByteBuf(String hexString) {
        return XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, this.trim(hexString));
    }

    private Map<String, Jt808EntityClassMetadata> scanEntityClass() {
        final XtreamClassScanner classScanner = new DefaultXtreamClassScanner();
        final Set<Class<?>> classes = classScanner.scan(
                new String[]{"io.github.hylexus.xtream.codec.ext.jt808.builtin.messages"},
                Set.of(XtreamClassScanner.ScanMode.CLASS_ANNOTATION),
                Jt808ResponseBody.class,
                classInfo -> !classInfo.isAnnotation()
        );
        return classes.stream()
                .map(cls -> {
                    final Jt808ResponseBody annotation = AnnotationUtils.findAnnotation(cls, Jt808ResponseBody.class);
                    if (annotation == null) {
                        return null;
                    }
                    return new Jt808EntityClassMetadata(
                            cls,
                            annotation.messageId(),
                            annotation.encryptionType(),
                            annotation.maxPackageSize(),
                            annotation.reversedBit15InHeader(),
                            annotation.desc());
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(it -> it.targetClass().getName(), Function.identity()));

    }

    DecodedMessageMetadata parse(String hexString) {
        ByteBuf originalBuffer = null;
        ByteBuf escapedBuffer = null;
        CompositeByteBuf mergedBuffer = null;
        try {
            originalBuffer = this.hexStringToByteBuf(hexString);
            final DecodedMessageMetadata temp = new DecodedMessageMetadata();
            temp.setOriginalHexString(FormatUtils.toHexString(originalBuffer));
            escapedBuffer = this.jt808BytesProcessor.doEscapeForReceive(originalBuffer);
            temp.setEscapedHexString(FormatUtils.toHexString(escapedBuffer));
            final Jt808RequestHeader header = this.jt808RequestDecoder.decodeHeader(escapedBuffer);
            temp.setHeader(header);
            temp.setEncryptionType(header.messageBodyProps().encryptionType());

            final byte originalCheckSum = escapedBuffer.getByte(escapedBuffer.readableBytes() - 1);
            final int messageBodyStartIndex = Jt808RequestHeader.messageBodyStartIndex(header.version(), header.messageBodyProps().hasSubPackage());
            final ByteBuf body = escapedBuffer.slice(messageBodyStartIndex, header.messageBodyLength());
            temp.setEscapedBody(FormatUtils.toHexString(body));
            if (header.messageBodyProps().encryptionType() == 0) {
                return temp;
            }
            final ByteBuf decryptedBody = this.decryptBody(header, body);
            final Jt808RequestHeader newHeader = header.mutate().messageBodyProps(header.messageBodyProps().mutate().messageBodyLength(decryptedBody.readableBytes()).build()).build();
            mergedBuffer = ByteBufAllocator.DEFAULT.compositeBuffer();
            mergedBuffer.addComponent(true, newHeader.encode())
                    .addComponent(true, decryptedBody)
                    .addComponent(true, ByteBufAllocator.DEFAULT.buffer().writeByte(originalCheckSum));
            temp.setDecryptedHexString(FormatUtils.toHexString(mergedBuffer));
            temp.setDecryptedBody(FormatUtils.toHexString(decryptedBody));
            temp.setHeader(newHeader);
            return temp;
        } finally {
            XtreamBytes.releaseBuf(originalBuffer);
            XtreamBytes.releaseBuf(escapedBuffer);
            XtreamBytes.releaseBuf(mergedBuffer);
        }
    }

    private ByteBuf decryptBody(Jt808RequestHeader header, ByteBuf body) {
        return body.retain();
    }

    private Jt808DashboardDebugMessage toEntityInstance(EncodedMessageMetadata vo, Object body, String bodyHexString) {
        return new Jt808DashboardDebugMessage()
                .setHeader(new Jt808DashboardDebugMessage.Header()
                        .setMessageId(vo.getHeader().messageId())
                        .setMessageBodyProps(vo.getHeader().messageBodyProps().intValue())
                        .setProtocolVersion(vo.getHeader().version().versionBit())
                        .setTerminalId(vo.getHeader().terminalId())
                        .setMessageSerialNo(vo.getFlowId())
                        .setSubPackageProps(new Jt808DashboardDebugMessage.SubPackageProps()
                                .setTotalSubPackageCount(vo.getTotalPackageNo())
                                .setCurrentPackageNo(vo.getCurrentPackageNo())
                        )
                )
                .setBody(bodyHexString == null
                        ? body
                        : XtreamBytes.byteBufFromHexString(ByteBufAllocator.DEFAULT, bodyHexString)
                )
                .setCheckSum(vo.getCheckSum());
    }

    private List<EncodedMessageMetadata> getEncodeMetadata(EncodeMessageDto dto, Object body) {
        final Map<Integer, EncodedMessageMetadata> resultMapping = new LinkedHashMap<>();
        final CodecTracker tracker = new CodecTracker();
        final Jt808MessageDescriber describer = new Jt808MessageDescriber(Jt808ProtocolVersion.VERSION_2019, "00000000013912344329")
                .messageId(dto.getMessageId())
                .maxPackageSize(dto.getMaxPackageSize())
                .reversedBit15InHeader(dto.getReversedBit15InHeader())
                .encryptionType(dto.getEncryptionType())
                .flowId(dto.getFlowId())
                .bodyCodecTracker(tracker)
                .messageCodecTracker(new Jt808MessageCodecTracker() {
                    @Override
                    public void beforeMessageBodyEncrypt(int totalSubPackageCount, int currentPackageNo, int flowId, Jt808MessageDescriber describer, ByteBuf bodyBeforeEncryption) {
                        resultMapping.computeIfAbsent(currentPackageNo, k -> new EncodedMessageMetadata()
                                .setTotalPackageNo(totalSubPackageCount)
                                .setCurrentPackageNo(currentPackageNo)
                                .setFlowId(flowId)
                                .setBodyBeforeEncryption(FormatUtils.toHexString(bodyBeforeEncryption))
                        );
                    }

                    @Override
                    public void afterMessageEncoded(int totalSubPackageCount, int currentPackageNo, int flowId, byte checkSum, Jt808MessageDescriber describer, Jt808RequestHeader header, ByteBuf rawBytes, ByteBuf escaped) {
                        final EncodedMessageMetadata vo = resultMapping.get(currentPackageNo);
                        vo.setHeader(header)
                                .setRawHexString(FormatUtils.toHexString(rawBytes))
                                .setEscapedHexString(FormatUtils.toHexString(escaped))
                                .setCheckSum(checkSum);
                    }

                });
        final ByteBuf encoded = this.responseEncoder.encode(body, describer);
        XtreamBytes.releaseBuf(encoded);
        return new ArrayList<>(resultMapping.values());
    }
}
