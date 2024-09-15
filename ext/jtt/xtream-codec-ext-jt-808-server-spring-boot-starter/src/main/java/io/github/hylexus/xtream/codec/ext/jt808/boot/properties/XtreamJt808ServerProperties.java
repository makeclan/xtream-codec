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

package io.github.hylexus.xtream.codec.ext.jt808.boot.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import reactor.netty.resources.LoopResources;

import java.time.Duration;

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "jt808-server")
public class XtreamJt808ServerProperties {
    private boolean enabled = true;

    @NestedConfigurationProperty
    private BuiltinFilters builtinFilters = new BuiltinFilters();

    @NestedConfigurationProperty
    private RequestSubPackageStorage requestSubPackageStorage = new RequestSubPackageStorage();

    @NestedConfigurationProperty
    private TcpServerProps tcpInstructionServer = new TcpServerProps();

    @NestedConfigurationProperty
    private TcpAttachmentServerProps tcpAttachmentServer = new TcpAttachmentServerProps();

    @NestedConfigurationProperty
    private UdpServerProps udpInstructionServer = new UdpServerProps();

    @NestedConfigurationProperty
    private UdpAttachmentServerProps udpAttachmentServer = new UdpAttachmentServerProps();

    @NestedConfigurationProperty
    private HandlerMethodSchedulerProperties handlerSchedulers = new HandlerMethodSchedulerProperties();

    @Getter
    @Setter
    @ToString
    public static class BaseServerProps {
        private boolean enabled = true;
        private String host;
        private int port = 6666;
    }

    @Getter
    @Setter
    @ToString
    public static class TcpServerProps extends BaseServerProps {

        @NestedConfigurationProperty
        private TcpLoopResourcesProperty loopResources = new TcpLoopResourcesProperty();

    }

    @Getter
    @Setter
    @ToString
    public static class TcpAttachmentServerProps extends BaseServerProps {

        @NestedConfigurationProperty
        private TcpLoopResourcesProperty loopResources = new TcpLoopResourcesProperty();

    }

    @Getter
    @Setter
    @ToString
    public static class UdpServerProps extends BaseServerProps {

        @NestedConfigurationProperty
        private UdpLoopResourcesProperty loopResources = new UdpLoopResourcesProperty();

    }


    @Getter
    @Setter
    @ToString
    public static class UdpAttachmentServerProps extends BaseServerProps {

        @NestedConfigurationProperty
        private UdpLoopResourcesProperty loopResources = new UdpLoopResourcesProperty();

    }

    @Getter
    @Setter
    @ToString
    public static class HandlerMethodSchedulerProperties {
        @NestedConfigurationProperty
        private XtreamServerSchedulerProperties nonBlockingScheduler = new XtreamServerSchedulerProperties();

        @NestedConfigurationProperty
        private XtreamServerSchedulerProperties blockingScheduler = new XtreamServerSchedulerProperties();
    }

    @Getter
    @Setter
    @ToString
    public static class TcpLoopResourcesProperty {
        private String threadNamePrefix = "xtream-tcp";
        private int selectCount = LoopResources.DEFAULT_IO_SELECT_COUNT;
        private int workerCount = LoopResources.DEFAULT_IO_WORKER_COUNT;
        private boolean daemon = true;
        private boolean colocate = true;
        private boolean preferNative = LoopResources.DEFAULT_NATIVE;
    }

    @Getter
    @Setter
    @ToString
    public static class UdpLoopResourcesProperty {
        private String threadNamePrefix = "xtream-udp";
        private int selectCount = LoopResources.DEFAULT_IO_SELECT_COUNT;
        private int workerCount = LoopResources.DEFAULT_IO_WORKER_COUNT;
        private boolean daemon = true;
        private boolean colocate = true;
        private boolean preferNative = LoopResources.DEFAULT_NATIVE;
    }

    @Getter
    @Setter
    @ToString
    public static class RequestSubPackageStorage {
        /**
         * 缓存最大大小
         */
        private int maximumSize = 1024;
        /**
         * 缓存条目的存活时间
         */
        private Duration ttl = Duration.ofSeconds(60);
    }

    @Getter
    @Setter
    @ToString
    public static class BuiltinFilters {
        private BaseFilterProps requestLogger = new BaseFilterProps();
        private BaseFilterProps requestDecoder = new BaseFilterProps();
    }

    @Getter
    @Setter
    @ToString
    public static class BaseFilterProps {
        private boolean enabled = true;
    }
}
