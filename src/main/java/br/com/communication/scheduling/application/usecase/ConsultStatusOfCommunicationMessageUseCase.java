package br.com.communication.scheduling.application.usecase;

import br.com.communication.scheduling.application.converter.ScheduleDTOConverter;
import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.domain.entity.StatusMessage;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;

/**
 * @author Guilherme Alves Silveira
 */
@ApplicationScoped
public class ConsultStatusOfCommunicationMessageUseCase {

	private final ScheduleRepository repository;
	private final ScheduleDTOConverter converter;

	@Inject
	public ConsultStatusOfCommunicationMessageUseCase(
			final ScheduleRepository repository,
			final ScheduleDTOConverter converter) {
		this.repository = repository;
		this.converter = converter;
	}

	public CompletableFuture<StatusMessage> consultStatusMessage(final ScheduleDTO dto) {

		final var schedule = converter.convert(dto);
		return repository.getStatusOfScheduledCommunicationMessageAsync(schedule);
	}
}
