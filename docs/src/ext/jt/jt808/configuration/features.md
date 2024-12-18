---
icon: toggle-on
article: false
---

# features

## 概览

```yaml {3}
jt808-server:
  ## ... 省略其他配置 ...
  features:
    ### 转发请求时使用指定的调度器(而不是上游默认的Reactor调度器)
    request-dispatcher-scheduler:
      enabled: false
    ### 请求日志(仅仅用于调试)
    request-logger:
      enabled: true
    ### 请求合并(如果不启用的话，你需要手动合并子包请求)
    request-combiner:
      enabled: true
      # 子包暂存器
      sub-package-storage:
        # 最大缓存数量
        maximum-size: 1024
        # 缓存过期时间
        ttl: 45s
```

## request-dispatcher-scheduler

todo: 补充配置项详细说明

## request-logger

todo: 补充配置项详细说明

## request-combiner

todo: 补充配置项详细说明
