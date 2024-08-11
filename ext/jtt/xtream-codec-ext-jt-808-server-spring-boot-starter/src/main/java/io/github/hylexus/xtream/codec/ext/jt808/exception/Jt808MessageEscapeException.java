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

package io.github.hylexus.xtream.codec.ext.jt808.exception;

public class Jt808MessageEscapeException extends Jt808MessageDecodeException {
    public Jt808MessageEscapeException() {
    }

    public Jt808MessageEscapeException(String message) {
        super(message);
    }

    public Jt808MessageEscapeException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jt808MessageEscapeException(Throwable cause) {
        super(cause);
    }

    public Jt808MessageEscapeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
