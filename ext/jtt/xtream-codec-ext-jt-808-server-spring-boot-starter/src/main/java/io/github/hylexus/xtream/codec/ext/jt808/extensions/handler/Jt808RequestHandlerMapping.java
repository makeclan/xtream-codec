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

package io.github.hylexus.xtream.codec.ext.jt808.extensions.handler;


import io.github.hylexus.xtream.codec.ext.jt808.spec.Jt808ProtocolVersion;
import io.github.hylexus.xtream.codec.server.reactive.spec.common.XtreamRequestHandlerMapping;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * @author hylexus
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@XtreamRequestHandlerMapping
public @interface Jt808RequestHandlerMapping {

    @AliasFor(annotation = XtreamRequestHandlerMapping.class, attribute = "scheduler")
    String scheduler() default "";

    /**
     * @return 当前处理器方法能处理的消息类型
     */
    int[] messageIds();

    /**
     * @return 当前处理器能处理的消息对应的版本
     */
    Jt808ProtocolVersion[] versions() default {Jt808ProtocolVersion.AUTO_DETECTION};

    String desc() default "";

}
