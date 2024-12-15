---
icon: cube
---

# OrderedComponent

## 作用

有些组件需要多个实例。比如 `XtreamFilter`，一个系统中出现 N 个过滤器是很正常的。但是这些过滤器的执行顺序怎么保证？

**Xtream** 中简单粗暴的方案是：让这些组件实现 `OrderedComponent` 接口，然后用户自己通过 `order()` 方法返回一个优先级，按照优先级排序(升序)。

::: tip

- 类似于 **spring** 的 `@org.springframework.core.Ordered`
- 数字越小，优先级越高

:::

接口定义如下：

```java
public interface OrderedComponent {
    int BUILTIN_COMPONENT_PRECEDENCE = 1000000;
    int DEFAULT_PRECEDENCE = 0;
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    default int order() {
        return DEFAULT_PRECEDENCE;
    }

    static <T extends OrderedComponent> List<T> sort(List<T> components) {
        return components.stream()
                .sorted(Comparator.comparingInt(OrderedComponent::order))
                .toList();
    }
}
```

## 支持的组件

![](/img/server/core-component/ordered-components.png)
