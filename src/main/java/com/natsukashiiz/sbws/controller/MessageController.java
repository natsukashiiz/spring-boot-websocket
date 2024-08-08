package com.natsukashiiz.sbws.controller;

import com.natsukashiiz.sbws.entity.Message;
import com.natsukashiiz.sbws.exception.BaseException;
import com.natsukashiiz.sbws.model.request.SendMessageRequest;
import com.natsukashiiz.sbws.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/send")
    public Message sendMessage(@RequestBody SendMessageRequest request) throws BaseException {
        return messageService.sendMessage(request);
    }
}
