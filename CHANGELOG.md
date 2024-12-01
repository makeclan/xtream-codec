## 0.0.1-beta.6(2024-12-01)

### ⭐ New Features

- 新增 `jt-808-server-dashboard-spring-boot-starter-reactive` 模块
- 新增 `jt-808-server-dashboard-ui` 模块

### ⚠️ Breaking Changes

- `xtream-codec-ext-jt-808-server-spring-boot-starter` 重名为 `jt-808-server-spring-boot-starter-reactive`

### ❤️ Contributors

- [@dfEric](https://github.com/dfEric)
- [@hylexus](https://github.com/hylexus)

## 0.0.1-beta.5

### ⭐ New Features

- 优化 `Jt808SessionManager`
- 优化 `Jt808CommandSender`
- 优化 `XtreamEventPublisher`
- 内置 **actuator** 指标
- 请求解码逻辑移动到 `Filter` 之前
- 新增 `RequestDispatcherSchedulerFilter`

### ⚠️ Breaking Changes

- 配置项重新调整

## 0.0.1-beta.4

### ⭐ New Features

- 新增 **JT/T 808** 部分消息映射示例实体类
- `@XtreamField` 注解新增属性:
    - `prependLengthFieldType()`
    - `prependLengthFieldLength()`
    - `iterationTimes()`
    - `iterationTimesExpression()`

### 🐞 Bug Fixes

- 修复 `NestedBeanPropertyMetadata` 没有调用 `FieldConditionEvaluator` 的问题

## 0.0.1-beta.3

### ⭐ New Features

新增两个子模块:

- **xtream-codec-server-reactive**: 和具体协议格式无关的 **纯异步**、<span style="color:red;">非阻塞的</span> 的服务端
- **jt-808-server-spring-boot-starter-reactive**: 基于 **xtream-codec-server-reactive** 实现的 **JT/T 808** 服务端扩展

### ⚠️ Breaking Changes

- **LICENSE** 从 **MulanPSL2** 改为 <span style="color:red;">Apache License 2.0</style>。改动原因如下：
    1. 项目里复制并修改了很多 **spring** 的源码
    2. **spring** 是使用 **Apache License 2.0** 开源的
    3. **MulanPSL2** 能兼容 **Apache License 2.0**，但反过来不行
- `FiledDataType.nested` 重命名为 `FiledDataType.struct`
- `@Preset.JtStyle.BCD` 重命名为 `@Preset.JtStyle.Bcd`
