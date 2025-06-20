package com.MoleLaw_backend.exception;

import lombok.Getter;

@Getter
public class MolelawException extends RuntimeException {
    private final ErrorCode errorCode;

    public MolelawException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}