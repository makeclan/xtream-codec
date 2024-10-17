## 0.0.1-beta.3

### ⭐ New Features

新增两个子模块:

- **xtream-codec-server-reactive**: 和具体协议格式无关的 **纯异步**、<font color="red">非阻塞</font> 的服务端
- **xtream-codec-ext-jt-808-server-spring-boot-starter**: 基于 **xtream-codec-server-reactive** 实现的 **JT/T 808** 服务端扩展

### ⚠️ Breaking Changes

- **LICENSE** 从 **MulanPSL2** 改为 <font color="red">Apache License 2.0</font>。改动原因如下：
    1. 项目里复制并修改了很多 **spring** 的源码
    2. **spring** 是使用 **Apache License 2.0** 开源的
    3. **MulanPSL2** 能兼容 **Apache License 2.0**，但反过来不行
- `FiledDataType.nested` 重命名为 `FiledDataType.struct`
- `@Preset.JtStyle.BCD` 重命名为 `@Preset.JtStyle.Bcd`
