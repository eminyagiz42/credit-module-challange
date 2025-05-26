package com.eminyagiz.creditmodule.model.dto;

import java.util.Date;

public record APIErrorResponse(String message, String errorCode, Date timestamp) {
}
