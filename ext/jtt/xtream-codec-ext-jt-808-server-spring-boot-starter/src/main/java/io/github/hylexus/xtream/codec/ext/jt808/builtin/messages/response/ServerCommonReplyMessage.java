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

package io.github.hylexus.xtream.codec.ext.jt808.builtin.messages.response;

import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808Request;
import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808RequestHeader;

/**
 * Alias for {@link BuiltinMessage8001}
 * <p>
 * 平台通用应答 0x8001
 *
 * @author hylexus
 */
public class ServerCommonReplyMessage extends BuiltinMessage8001 {

    public static ServerCommonReplyMessage success(Jt808Request request) {
        final Jt808RequestHeader header = request.header();
        return success(header.flowId(), header.messageId());
    }

    public static ServerCommonReplyMessage success(int clientFlowId, int clientMessageId) {
        final ServerCommonReplyMessage message = new ServerCommonReplyMessage();
        message.setClientFlowId(clientFlowId);
        message.setClientMessageId(clientMessageId);
        message.setResult((short) 0);
        return message;
    }

}
