package com.pricehunter.core.adapter.rest.exception;

import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.GenericException;

public final class TimeoutRestClientException extends GenericException {

    public TimeoutRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
