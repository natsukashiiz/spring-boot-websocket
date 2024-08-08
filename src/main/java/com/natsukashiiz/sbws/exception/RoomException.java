package com.natsukashiiz.sbws.exception;

public class RoomException extends BaseException {
    public RoomException(String code) {
        super("room." + code);
    }

    public static RoomException userNotInRoom() {
        return new RoomException("user.not.in.room");
    }
}
