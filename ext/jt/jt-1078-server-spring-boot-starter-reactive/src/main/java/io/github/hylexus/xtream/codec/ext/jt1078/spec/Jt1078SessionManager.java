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

import io.github.hylexus.xtream.codec.server.reactive.spec.XtreamSessionManager;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface Jt1078SessionManager extends XtreamSessionManager<Jt1078Session> {
    default Stream<Jt1078Session> list(int page, int pageSize, Predicate<Jt1078Session> filter) {
        return this.list().filter(filter).sorted(Comparator.comparing(Jt1078Session::terminalId)).skip((long) (page - 1) * pageSize).limit(pageSize);
    }

    default Stream<Jt1078Session> list(int page, int pageSize) {
        return this.list().sorted(Comparator.comparing(Jt1078Session::terminalId)).skip((long) (page - 1) * pageSize).limit(pageSize);
    }

    default <T> Stream<T> list(int page, int pageSize, Predicate<Jt1078Session> filter, Function<Jt1078Session, T> converter) {
        return this.list().filter(filter).sorted(Comparator.comparing(Jt1078Session::terminalId)).skip((long) (page - 1) * pageSize).limit(pageSize).map(converter);
    }

    default <T> Stream<T> list(int page, int pageSize, Function<Jt1078Session, T> converter) {
        return this.list().sorted(Comparator.comparing(Jt1078Session::terminalId)).skip((long) (page - 1) * pageSize).map(converter).limit(pageSize);
    }

}
