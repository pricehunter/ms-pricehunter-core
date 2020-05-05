package com.pricehunter.core.adapter.rest.exception;

import com.pricehunter.core.config.ErrorCode;
import com.pricehunter.core.config.exception.GenericException;

public final class RestClientGenericException extends GenericException {

    public RestClientGenericException(ErrorCode errorCode) {
        super(errorCode);
    }

}
