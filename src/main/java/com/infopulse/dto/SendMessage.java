package com.infopulse.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class SendMessage {
    private String type;
    private String message;
    private String sender;
    private List<String> usersActive;
}
