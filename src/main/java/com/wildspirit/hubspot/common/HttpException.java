package com.wildspirit.hubspot.common;

public class HttpException extends RuntimeException {
    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public final static class NotFoundException extends HttpException {
        public NotFoundException(String message) {
            super(message);
        }
    }

    public final static class UnmappedHttpException extends HttpException {
        public final int httpCode;
        public UnmappedHttpException(int httpCode, String message) {
            super("code: " + httpCode + " message: " + message);
            this.httpCode = httpCode;
        }
    }

    public static class UnauthorisedException extends HttpException {
        public UnauthorisedException(String message) {
            super(message);
        }
    }

    public static class TooManyRequestsException extends HttpException {
        public TooManyRequestsException(String message) {
            super(message);
        }

        public TooManyRequestsException(String message, Throwable e) {
            super(message, e);
        }
    }

    public static class ForbiddenException extends HttpException {
        public ForbiddenException(String message) {
            super(message);
        }
    }

    public static class BadRequestException extends HttpException {
        public BadRequestException(String message) {
            super(message);
        }
    }

    public static class ConflictException extends HttpException {
        public ConflictException(String message) {
            super(message);
        }
    }
}
