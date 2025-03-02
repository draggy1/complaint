package com.complaint.common;

public record Result<T>(int statusCode, T data) {
}
