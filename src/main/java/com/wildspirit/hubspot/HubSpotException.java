package com.wildspirit.hubspot;

public class HubSpotException extends RuntimeException {
    public HubSpotException() {
    }

    public HubSpotException(String message) {
        super(message);
    }

    public HubSpotException(String message, Throwable cause) {
        super(message, cause);
    }

    public HubSpotException(Throwable cause) {
        super(cause);
    }

    public HubSpotException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
