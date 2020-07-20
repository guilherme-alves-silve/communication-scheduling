package br.com.communication.scheduling.infrastructure.domain.repository;

import br.com.communication.scheduling.domain.entity.Schedule;
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
import javax.transaction.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
class ScheduleRepositoryReactiveImplTest {

    private final ScheduleRepository repository;

    private final TransactionManager transactionManager;

    @Inject
    ScheduleRepositoryReactiveImplTest(final ScheduleRepository repository, TransactionManager transactionManager) {
        this.repository = repository;
        this.transactionManager = transactionManager;
    }

    @BeforeEach
    @Transactional
    public void setUpEach() {

        RepositoryUtils.save(repository, ScheduleFixture.getPastSchedules().toArray(new Schedule[0]));
        RepositoryUtils.save(repository, ScheduleFixture.getFutureSchedules().toArray(new Schedule[0]));
    }

    @AfterEach
    @Transactional
    public void tearDownEach() {
        RepositoryUtils.delete(repository, ScheduleFixture.getPastSchedules().toArray(new Schedule[0]));
        RepositoryUtils.delete(repository, ScheduleFixture.getFutureSchedules().toArray(new Schedule[0]));
    }

    @Test
    void shouldGetAllUnsendedSchedules() {

        final var schedules = repository.getUnsendedScheduleMessagesAsync(3)
                .toCompletableFuture()
                .join();

        assertEquals(3, schedules.size());
    }

    @Test
    void shouldUpdateSentSchedules()
            throws SystemException, NotSupportedException, HeuristicRollbackException,
            HeuristicMixedException, RollbackException {

        final var schedules = repository.getUnsendedScheduleMessagesAsync(5)
                .toCompletableFuture()
                .join();

        assertEquals(5, schedules.size());

        transactionManager.begin();

        schedules.forEach(schedule -> repository.updateToSendAsync(schedule).join());

        transactionManager.commit();

        final var mustBeEmptySchedules = repository.getUnsendedScheduleMessagesAsync(5)
                .toCompletableFuture()
                .join();

        assertEquals(0, mustBeEmptySchedules.size());
    }
}