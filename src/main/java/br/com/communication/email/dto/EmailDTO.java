package br.com.communication.email.dto;

import br.com.communication.scheduling.domain.entity.MessageType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.StringJoiner;

@Getter
@Setter
@Accessors(chain = true)
public class EmailDTO {

    private String to;

    private String message;

    private MessageType type;

    @Override
    public String toString() {
        return new StringJoiner(", ", EmailDTO.class.getSimpleName() + "[", "]")
                .add("to='" + to + "'")
                .add("message='" + message + "'")
                .add("type=" + type)
                .toString();
    }
}
