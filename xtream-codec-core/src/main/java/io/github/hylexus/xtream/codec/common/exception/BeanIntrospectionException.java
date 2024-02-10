package io.github.hylexus.xtream.codec.common.exception;

public class BeanIntrospectionException extends RuntimeException {
    public BeanIntrospectionException() {
    }

    public BeanIntrospectionException(String message) {
        super(message);
    }

    public BeanIntrospectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanIntrospectionException(Throwable cause) {
        super(cause);
    }

    public BeanIntrospectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
