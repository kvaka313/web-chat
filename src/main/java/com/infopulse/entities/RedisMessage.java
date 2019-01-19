package com.infopulse.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RedisMessage {
    private String sender;
    private String message;
}
