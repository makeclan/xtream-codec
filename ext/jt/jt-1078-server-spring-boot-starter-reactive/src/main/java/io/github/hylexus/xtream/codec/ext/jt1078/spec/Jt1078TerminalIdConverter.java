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

package io.github.hylexus.xtream.codec.ext.jt1078.spec;

/**
 * 1078 的 SIM 为 BCD[6]，但是
 * <ol>
 *     <li>v2011 或 2013 版的终端手机号就是 BCD[6]</li>
 *     <li>v2019的终端手机号是 BCD[10]</li>
 * </ol>
 * 默认实现: 截取最后 12(BCD[6]) 个字符
 * <p>
 *
 * @see Jt1078RequestHeader#sim()
 * @see DefaultJt1078TerminalIdConverter
 */
public interface Jt1078TerminalIdConverter {

    String convert(String original);

    class DefaultJt1078TerminalIdConverter implements Jt1078TerminalIdConverter {
        @Override
        public String convert(String original) {
            // BCD[6] ==> 12
            // 视为 2013||2011 版
            if (original.length() <= 12) {
                return original;
            }
            // 视为 2019 ==> 只保留最后 12 位
            return original.substring(original.length() - 12);
        }
    }

}
