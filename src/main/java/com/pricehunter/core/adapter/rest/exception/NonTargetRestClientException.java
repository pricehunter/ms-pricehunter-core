package com.pricehunter.core.adapter.rest.exception;

import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.GenericException;

public final class NonTargetRestClientException extends GenericException {

    public NonTargetRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
