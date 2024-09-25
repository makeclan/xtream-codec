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

package io.github.hylexus.xtream.codec.ext.jt808.utils;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Session;
import io.netty.util.AttributeKey;

import java.time.format.DateTimeFormatter;

/**
 * @author hylexus
 **/
public interface JtProtocolConstant {
    int PACKAGE_DELIMITER = 0x7E;
    int DEFAULT_MAX_INSTRUCTION_FRAME_LENGTH = 1024;
    int DEFAULT_MAX_STREAM_FRAME_LENGTH = 1024 * 65;

    AttributeKey<Jt808Session> NETTY_ATTR_KEY_SESSION = AttributeKey.newInstance("jt808Session-" + Jt808Session.class.getName());
    String DEFAULT_DATE_TIME_FORMAT = "yyMMddHHmmss";
    DateTimeFormatter DEFAULT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(JtProtocolConstant.DEFAULT_DATE_TIME_FORMAT);

    String BEAN_NAME_CHANNEL_INBOUND_HANDLER_ADAPTER = "Jt808DelimiterBasedFrameDecoder";

    String BEAN_NAME_JT_808_TCP_XTREAM_NETTY_HANDLER_ADAPTER_INSTRUCTION_SERVER = "jt808TcpXtreamNettyHandlerAdapterInstructionServer";
    String BEAN_NAME_JT_808_TCP_XTREAM_NETTY_HANDLER_ADAPTER_ATTACHMENT_SERVER = "jt808TcpXtreamNettyHandlerAdapterAttachmentServer";
    String BEAN_NAME_JT_808_UDP_XTREAM_NETTY_HANDLER_ADAPTER_INSTRUCTION_SERVER = "jt808UdpXtreamNettyHandlerAdapterInstructionServer";
    String BEAN_NAME_JT_808_UDP_XTREAM_NETTY_HANDLER_ADAPTER_ATTACHMENT_SERVER = "jt808UdpXtreamNettyHandlerAdapterAttachmentServer";

    String BEAN_NAME_JT_808_TCP_XTREAM_NETTY_RESOURCE_FACTORY_INSTRUCTION_SERVER = "jt808TcpXtreamNettyResourceFactoryInstructionServer";
    String BEAN_NAME_JT_808_TCP_XTREAM_NETTY_RESOURCE_FACTORY_ATTACHMENT_SERVER = "jt808TcpXtreamNettyResourceFactoryAttachmentServer";
    String BEAN_NAME_JT_808_UDP_XTREAM_NETTY_RESOURCE_FACTORY_INSTRUCTION_SERVER = "jt808UdpXtreamNettyResourceFactoryInstructionServer";
    String BEAN_NAME_JT_808_UDP_XTREAM_NETTY_RESOURCE_FACTORY_ATTACHMENT_SERVER = "jt808UdpXtreamNettyResourceFactoryAttachmentServer";

    String BEAN_NAME_JT_808_TCP_XTREAM_SERVER_INSTRUCTION_SERVER = "jt808TcpXtreamServerInstructionServer";
    String BEAN_NAME_JT_808_TCP_XTREAM_SERVER_ATTACHMENT_SERVER = "jt808TcpXtreamServerAttachmentServer";
    String BEAN_NAME_JT_808_UDP_XTREAM_SERVER_INSTRUCTION_SERVER = "jt808UdpXtreamServerInstructionServer";
    String BEAN_NAME_JT_808_UDP_XTREAM_SERVER_ATTACHMENT_SERVER = "jt808UdpXtreamServerAttachmentServer";
}
