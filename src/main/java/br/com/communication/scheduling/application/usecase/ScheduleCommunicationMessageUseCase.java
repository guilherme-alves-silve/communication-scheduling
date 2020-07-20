package br.com.communication.scheduling.application.usecase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.communication.scheduling.application.converter.ScheduleDTOConverter;
import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import java.util.concurrent.CompletableFuture;

/**
 * @author Guilherme Alves Silveira
 */
@ApplicationScoped
public class ScheduleCommunicationMessageUseCase {

	private final ScheduleRepository repository;
	private final ScheduleDTOConverter converter;

	@Inject
	public ScheduleCommunicationMessageUseCase (
			final ScheduleRepository repository,
			final ScheduleDTOConverter converter) {
		this.repository = repository;
		this.converter = converter;
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public CompletableFuture<Boolean> sendSchedule(final ScheduleDTO dto) {

		final var schedule = converter.convert(dto);
		return repository.saveAsync(schedule);
	}
}
