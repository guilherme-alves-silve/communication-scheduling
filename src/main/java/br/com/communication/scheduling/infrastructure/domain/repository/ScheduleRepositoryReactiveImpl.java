package br.com.communication.scheduling.infrastructure.domain.repository;

import br.com.communication.scheduling.domain.entity.Schedule;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Guilherme Alves Silveira
 */
@ApplicationScoped
public class ScheduleRepositoryReactiveImpl implements ScheduleRepository, PanacheRepository<Schedule> {

	private final ThreadContext threadContext;
	private final ManagedExecutor managedExecutor;

	@Inject
	public ScheduleRepositoryReactiveImpl(
			final ThreadContext threadContext,
			final ManagedExecutor managedExecutor
	) {
		this.threadContext = threadContext;
		this.managedExecutor = managedExecutor;
	}

	@Override
	public CompletableFuture<Boolean> saveAsync(final Schedule schedule) {

		return threadContext.withContextCapture(managedExecutor.supplyAsync(() -> {
			persist(schedule);
			return true;
		}));
	}

	@Override
	public CompletableFuture<List<Schedule>> getUnsendedScheduleMessagesAsync(final int limit) {

		return threadContext.withContextCapture(managedExecutor.supplyAsync(() ->
				find("SELECT s FROM Schedule s " +
					"WHERE s.sent = false AND " +
					"s.timeToSend <= :pTimeToSend", Map.of("pTimeToSend", LocalDateTime.now()))
				.page(Page.ofSize(limit))
				.list()
		));
	}

	@Override
	public CompletableFuture<Boolean> deleteAsync(final Schedule schedule) {

		return threadContext.withContextCapture(managedExecutor.supplyAsync(() -> {
			delete(schedule);
			return true;
		}));
	}

	@Override
	public CompletableFuture<Boolean> updateToSendAsync(final Schedule schedule) {
		return threadContext.withContextCapture(managedExecutor.supplyAsync(() -> {
			int result = update("UPDATE FROM Schedule s " +
					"SET s.sent = true " +
					"WHERE s.id = :pId", Map.of("pId", schedule.getId()));
			return result > 0;
		}));
	}
}
