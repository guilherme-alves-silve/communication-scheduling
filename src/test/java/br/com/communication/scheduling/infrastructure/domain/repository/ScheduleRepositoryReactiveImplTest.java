package br.com.communication.scheduling.infrastructure.domain.repository;

import br.com.communication.scheduling.domain.entity.Schedule;
import br.com.communication.scheduling.domain.entity.StatusMessage;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import br.com.communication.scheduling.infrastructure.fixture.ScheduleFixture;
import br.com.communication.scheduling.test.db.RepositoryUtils;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.TransactionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class ScheduleRepositoryReactiveImplTest {

    private final ScheduleRepository repository;

    private final TransactionManager transactionManager;

    @Inject
    ScheduleRepositoryReactiveImplTest(final ScheduleRepository repository,
                                       final TransactionManager transactionManager) {
        this.repository = repository;
        this.transactionManager = transactionManager;
    }

    @BeforeEach
    public void setUpEach() throws Exception {

        transactionManager.begin();

        RepositoryUtils.save(repository, ScheduleFixture.getPastSchedules().toArray(new Schedule[0]));
        RepositoryUtils.save(repository, ScheduleFixture.getFutureSchedules().toArray(new Schedule[0]));

        transactionManager.commit();
    }

    @AfterEach
    public void tearDownEach() throws Exception {

        transactionManager.begin();

        final var schedules = repository.getAllAsync().join();

        RepositoryUtils.delete(repository, schedules.toArray(new Schedule[0]));

        transactionManager.commit();
    }

    @Test
    void shouldGetAllUnsentSchedules() {

        final var schedules = repository.getUnsentScheduleMessagesAsync(3)
                .toCompletableFuture()
                .join();

        assertEquals(3, schedules.size());
    }

    @Test
    void shouldUpdateSentSchedules()
            throws SystemException, NotSupportedException, HeuristicRollbackException,
            HeuristicMixedException, RollbackException {

        final var schedules = repository.getUnsentScheduleMessagesAsync(5)
                .toCompletableFuture()
                .join();

        assertEquals(5, schedules.size());

        transactionManager.begin();

        schedules.forEach(schedule -> repository.updateToSendAsync(schedule).join());

        transactionManager.commit();

        final var mustBeEmptySchedules = repository.getUnsentScheduleMessagesAsync(5)
                .toCompletableFuture()
                .join();

        assertEquals(0, mustBeEmptySchedules.size());
    }

    @Test
    void shouldGetStatusOfScheduledCommunicationMessage() {

        final var schedules = repository.getUnsentScheduleMessagesAsync(1)
                .toCompletableFuture()
                .join();

        assertTrue(schedules.size() > 0);

        final var status = repository.getStatusOfScheduledCommunicationMessageAsync(schedules.get(0)).join();

        assertEquals(status, StatusMessage.UNSENT);
    }
}