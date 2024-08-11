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

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "jt808-server")
public class XtreamJt808ServerProperties {
    private boolean enabled = true;
    private TcpServerProps tcpServer = new TcpServerProps();
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

    }

    @Getter
    @Setter
    @ToString
    public static class UdpServerProps extends BaseTcpServerProps {

    }
}
