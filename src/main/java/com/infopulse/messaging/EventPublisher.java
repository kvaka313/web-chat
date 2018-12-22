package com.infopulse.messaging;

import com.infopulse.dto.ChatUserDto;
import com.infopulse.dto.Payload;
import com.infopulse.exceptions.MessageException;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.context.event.EventListener;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@EnableBinding
public class EventPublisher {

    private BinderAwareChannelResolver resolver;

    public EventPublisher(BinderAwareChannelResolver binderAwareChannelResolver){
        this.resolver = binderAwareChannelResolver;
    }


    @EventListener
    public void publishObject(ChatUserDto chatUserDto){
        publish(chatUserDto);
    }

    private void publish(ChatUserDto chatUserDto){
        Payload payload = new Payload();
        payload.setChatUserDto(chatUserDto);
        payload.setEvent("CREATE");

        Map<String, String> headers = new HashMap<>();
        headers.put("EventVersion", "v1");
        headers.put("EntityVersion", "v1");

        Message<Payload> message = MessageBuilder
                .withPayload(payload)
                .copyHeaders(headers)
                .build();

        MessageChannel channel = resolver.resolveDestination("user-event-output");

        if(!channel.send(message)){
            throw new MessageException("Message can not send to keycloak");
        }

    }
}
