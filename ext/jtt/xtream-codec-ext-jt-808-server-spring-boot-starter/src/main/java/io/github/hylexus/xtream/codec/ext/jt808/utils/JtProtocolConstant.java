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

import java.nio.charset.Charset;

/**
 * @author hylexus
 **/
public interface JtProtocolConstant {
    String DEFAULT_DATE_TIME_FORMAT = "yyMMddHHmmss";

    Charset JT_808_STRING_ENCODING = Charset.forName("GBK");
    int PACKAGE_DELIMITER = 0x7E;
    int DEFAULT_MAX_PACKAGE_SIZE = 1024;

    String BEAN_NAME_CHANNEL_INBOUND_HANDLER_ADAPTER = "Jt808DelimiterBasedFrameDecoder";

}
