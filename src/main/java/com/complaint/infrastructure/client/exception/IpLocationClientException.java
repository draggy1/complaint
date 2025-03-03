package com.complaint.infrastructure.client.exception;

import lombok.Getter;


@Getter
public final class IpLocationClientException extends RuntimeException {
    public IpLocationClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
