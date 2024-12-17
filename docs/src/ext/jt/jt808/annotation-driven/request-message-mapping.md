---
icon: at
article: false
---

# 请求消息映射

## @Jt808RequestHandlerMapping

### 属性介绍

| 属性           | 类型                       | 作用             | 取值示例             |
|--------------|--------------------------|----------------|------------------|
| `scheduler`  | `String`                 | 处理器方法在哪个线程池上运行 | `my-scheudler-1` |
| `messageIds` | `int[]`                  | 处理器方法支持的消息ID   | `0x0200`         |
| `versions`   | `Jt808ProtocolVersion[]` | 处理器方法支持的消息版本   | `VERSION_2019`   |
| `desc`       | `String`                 | 描述信息           | `some desc...`   |
