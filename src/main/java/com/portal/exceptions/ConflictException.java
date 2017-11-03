package com.portal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Status conflict! Such user already exist!")
public class ConflictException extends RuntimeException {
}
