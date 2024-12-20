---
icon: arrow-right-arrow-left
article: false
---

# 和jt-framework有什么区别?

- 协议
    - **jt-framework** 仅仅针对 **JT/T 808协议**
    - **xtream-codec** 和具体协议无关
- 实现细节
    - **jt-framework** 是阻塞的，基于 [Netty](https://netty.io)
    - **xtream-codec** 是<span style="color:red;font-weight:bold;">非阻塞的</span>
      ，基于 [Reactor Netty](https://projectreactor.io/docs/netty/release/reference/about-doc.html)
- 项目维护
    - 两个项目都会积极维护
    - 但是，我们推荐 **xtream-codec**，毕竟它是 <span style="color:red;font-weight:bold;">非阻塞的</span>
