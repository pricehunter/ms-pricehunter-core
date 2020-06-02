package com.pricehunter.core.config.exception;

import com.pricehunter.core.config.ErrorCode;

public class ProductNotFoundException extends GenericException {

  public ProductNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
