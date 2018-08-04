package com.winhxd.b2c.common.exception;

public class ProductExcepton extends BusinessException {

    public ProductExcepton() {
    }

    public ProductExcepton(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public ProductExcepton(int errorCode, String message) {
        super(errorCode, message);
    }

    public ProductExcepton(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ProductExcepton(int errorCode) {
        super(errorCode);
    }

}
