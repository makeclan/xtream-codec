---
icon: cube
---

# OrderedComponent

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
