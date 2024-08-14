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
    private RequestSubPackageStorage requestSubPackageStorage = new RequestSubPackageStorage();

    @NestedConfigurationProperty
    private TcpServerProps tcpServer = new TcpServerProps();

    @NestedConfigurationProperty
    private UdpServerProps udpServer = new UdpServerProps();

    @Getter
    @Setter
    @ToString
    public static class BaseTcpServerProps {
        private boolean enabled = true;
        private String host;
        private int port = 6666;
    }

    @Getter
    @Setter
    @ToString
    public static class TcpServerProps extends BaseTcpServerProps {

        @NestedConfigurationProperty
        private TcpLoopResourcesProperty loopResources = new TcpLoopResourcesProperty();

    }

    @Getter
    @Setter
    @ToString
    public static class UdpServerProps extends BaseTcpServerProps {
        /**
         * 是否启用内置的 UDP 多包拆分器
         */
        private boolean enableBuiltinMultipleUdpPackageSplitter = true;

        @NestedConfigurationProperty
        private UdpLoopResourcesProperty loopResources = new UdpLoopResourcesProperty();

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
}
