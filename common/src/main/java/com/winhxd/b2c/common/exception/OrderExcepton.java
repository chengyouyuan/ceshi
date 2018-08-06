package com.winhxd.b2c.common.exception;

public class OrderExcepton extends BusinessException {

    public OrderExcepton() {
        super();
    }

    public OrderExcepton(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public OrderExcepton(int errorCode, String message) {
        super(errorCode, message);
    }

    public OrderExcepton(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public OrderExcepton(int errorCode) {
        super(errorCode);
    }

}
