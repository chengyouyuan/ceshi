package com.winhxd.b2c.common.exception.order;

import com.winhxd.b2c.common.exception.BusinessException;

public class OrderCreateExcepton extends BusinessException {

    public OrderCreateExcepton() {
        super();
    }

    public OrderCreateExcepton(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public OrderCreateExcepton(int errorCode, String message) {
        super(errorCode, message);
    }

    public OrderCreateExcepton(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public OrderCreateExcepton(int errorCode) {
        super(errorCode);
    }

}
