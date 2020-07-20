package br.com.communication.scheduling.application.converter;

import javax.enterprise.context.ApplicationScoped;

import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.domain.entity.Schedule;

import java.util.Objects;

/**
 * @author Guilherme Alves Silveira
 */
@ApplicationScoped
public class ScheduleDTOConverter {

	public Schedule convert(final ScheduleDTO dto) {
		Objects.requireNonNull(dto, "dto cannot be null!");
		return Schedule.builder()
				.to(dto.getTo())
				.message(dto.getMessage())
				.type(dto.getType())
				.timeToSend(dto.getTimeToSend())
				.build();
	}
}
