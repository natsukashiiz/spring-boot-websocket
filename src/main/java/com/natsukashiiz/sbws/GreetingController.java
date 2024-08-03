package com.natsukashiiz.sbws;

import lombok.Getter;
import lombok.Setter;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(Message message) throws Exception {
        return "Hello, " + message.getMessage() + "!";
    }

    @Setter
    @Getter
    public static class Message {
        private String message;
    }
}
