package com.natsukashiiz.sbws.websocket.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Log4j2
public class WebsocketController {

    @MessageExceptionHandler
    @SendTo("/topic/error")
    public String handleException(Exception e) {
        var message = "Something went wrong processing the request: " + NestedExceptionUtils.getMostSpecificCause(e);
        log.error("Error processing request: {}", message);
        return message;
    }
}
