---
icon: leaf
article: false
---

# ReactorNetty相关

## 介绍

这里介绍的是对 **reactor-netty** 服务的自定义：

- `TcpNettyServerCustomizer`
- `UdpNettyServerCustomizer`

![](/img/ext/jt/jt808/customization/netty-customizer.png)

## 示例

```java
@Component
public class MyTcpNettyServerCustomizer implements TcpNettyServerCustomizer {

    @Override
    public TcpServer customize(TcpServer server) {
        return server.option(ChannelOption.SO_BACKLOG, 2048);
    }

}
```
