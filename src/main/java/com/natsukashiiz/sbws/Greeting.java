package com.natsukashiiz.sbws;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@RequiredArgsConstructor
public class Greeting {
    private final String content;
}
