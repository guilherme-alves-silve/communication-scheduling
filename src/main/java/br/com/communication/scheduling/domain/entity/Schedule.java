package br.com.communication.scheduling.domain.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.StringJoiner;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author Guilherme Alves Silveira
 */
@Entity
@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@RegisterForReflection
@Table(name = "schedule_tbl")
public class Schedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonbProperty
	@Column(name = "to_address")
	private String to;

	@JsonbProperty
	private String message;

	@JsonbProperty
	@Enumerated(EnumType.STRING)
	private MessageType type;

	@Column(name = "time_to_send")
	private LocalDateTime timeToSend;

	private boolean sent;

	/**
	 * Used by hibernate
	 */
	protected Schedule() {

	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Schedule.class.getSimpleName() + "[", "]")
				.add("id=" + id)
				.add("to='" + to + "'")
				.add("message='" + message + "'")
				.add("type=" + type)
				.add("timeToSend=" + timeToSend)
				.add("sent=" + sent)
				.toString();
	}
}
