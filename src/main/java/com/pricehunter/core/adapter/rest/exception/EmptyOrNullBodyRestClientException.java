package com.pricehunter.core.adapter.rest.exception;

import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.GenericException;

public final class EmptyOrNullBodyRestClientException extends GenericException {

    public EmptyOrNullBodyRestClientException(ErrorCode errorCode) {
        super(errorCode);
    }

}
