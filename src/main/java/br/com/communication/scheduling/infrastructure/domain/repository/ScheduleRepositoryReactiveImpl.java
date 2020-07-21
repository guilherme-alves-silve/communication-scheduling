/*
 * The MIT License
 * Copyright © 2020 Guilherme Alves Silveira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.communication.scheduling.infrastructure.domain.repository;

import br.com.communication.scheduling.domain.entity.Schedule;
import br.com.communication.scheduling.domain.entity.StatusMessage;
import br.com.communication.scheduling.domain.exception.NotFoundExceptionDomain;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
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
	public CompletableFuture<List<Schedule>> getUnsentScheduleMessagesAsync(final int limit) {

		return threadContext.withContextCapture(managedExecutor.supplyAsync(() ->
				find("SELECT s FROM Schedule s " +
					"WHERE s.sent = :pSent AND " +
					"s.timeToSend <= :pTimeToSend", Map.of(
							"pSent", false,
							"pTimeToSend", LocalDateTime.now()
				))
				.page(Page.ofSize(limit))
				.list()
		));
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public CompletableFuture<Boolean> deleteAsync(final Schedule schedule) {

		return threadContext.withContextCapture(managedExecutor.supplyAsync(() -> {
			delete(schedule);
			return true;
		}));
	}

	@Override
	@Transactional(Transactional.TxType.REQUIRED)
	public CompletableFuture<Boolean> updateToSendAsync(final Schedule schedule) {
		return threadContext.withContextCapture(managedExecutor.supplyAsync(() -> {
			int result = update("UPDATE FROM Schedule s " +
					"SET s.sent = :pSent " +
					"WHERE s.id = :pId", Map.of(
					"pSent", true,
					"pId", schedule.getId()
			));
			return result > 0;
		}));
	}

	@Override
	public CompletableFuture<StatusMessage> getStatusOfScheduledCommunicationMessageAsync(Schedule schedule) {
		return threadContext.withContextCapture(managedExecutor.supplyAsync(() ->
				find("SELECT s FROM Schedule s " +
					"WHERE s.id = :pId", Map.of("pId", schedule.getId()))
					.firstResultOptional()
					.map(Schedule::isSent)
					.map(result -> result? StatusMessage.SENT : StatusMessage.UNSENT)
					.orElseThrow(NotFoundExceptionDomain::new)
		));
	}

	@Override
	public CompletableFuture<List<Schedule>> getAllAsync() {
		return threadContext.withContextCapture(managedExecutor.supplyAsync(this::listAll));
	}
}
