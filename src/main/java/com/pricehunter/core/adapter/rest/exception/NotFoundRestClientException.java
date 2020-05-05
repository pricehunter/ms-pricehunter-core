package com.pricehunter.core.adapter.rest.exception;

import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.GenericException;

public final class NotFoundRestClientException extends GenericException {

    public NotFoundRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }
}
