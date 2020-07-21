package br.com.communication.scheduling.application.dto;

import br.com.communication.scheduling.domain.entity.MessageType;
import java.time.LocalDateTime;
import java.util.StringJoiner;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Guilherme Alves Silveira
 */
@Setter
@Getter
@EqualsAndHashCode
@Accessors(chain = true)
public class ScheduleDTO {

	private Long id;
	private String to;
	private String message;
	private MessageType type;
	private LocalDateTime timeToSend;

	public static ScheduleDTO withId(final Long id) {
		return new ScheduleDTO().setId(id);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ScheduleDTO.class.getSimpleName() + "[", "]")
				.add("to='" + to + "'")
				.add("message='" + message + "'")
				.add("type=" + type)
				.add("timeToSend=" + timeToSend)
				.toString();
	}
}
