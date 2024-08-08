package com.natsukashiiz.sbws.exception;

public class LoginException extends BaseException {
    public LoginException(String code) {
        super("login." + code);
    }

    public static LoginException invalidDeviceId() {
        return new LoginException("invalid.deviceId");
    }
}
