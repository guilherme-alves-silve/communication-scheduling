package br.com.communication.scheduling.domain.repository;

import br.com.communication.scheduling.domain.entity.Schedule;
import br.com.communication.scheduling.domain.entity.StatusMessage;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author Guilherme Alves Silveira
 */
public interface ScheduleRepository {

	CompletableFuture<Boolean> saveAsync(final Schedule schedule);

	CompletableFuture<List<Schedule>> getUnsendedScheduleMessagesAsync(final int limit);

	CompletableFuture<Boolean> deleteAsync(final Schedule schedule);

	CompletableFuture<Boolean> updateToSendAsync(final Schedule schedule);

    CompletableFuture<StatusMessage> getStatusOfScheduledCommunicationMessageAsync(final Schedule schedule);
}
