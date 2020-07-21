package br.com.communication.scheduling.infrastructure.domain.service;

import br.com.communication.scheduling.domain.entity.Schedule;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import br.com.communication.scheduling.domain.service.SchedulerCommunicationSenderService;
import br.com.communication.scheduling.infrastructure.fixture.ScheduleFixture;
import br.com.communication.scheduling.test.wiremock.SchedulerCommunicationSenderServiceWireMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(SchedulerCommunicationSenderServiceWireMock.class)
class SchedulerCommunicationSenderServiceImplTest {

    @Inject
    SchedulerCommunicationSenderService service;

    @InjectMock
    ScheduleRepository repository;

    @Test
    public void shouldSendAllPastMessages() {

        when(repository.getUnsendedScheduleMessagesAsync(50))
                .thenReturn(CompletableFuture.completedFuture(ScheduleFixture.getPastSchedules()));

        when(repository.updateToSendAsync(any(Schedule.class)))
                .thenReturn(CompletableFuture.completedFuture(true));

        service.jobSend();

        assertAll(
                () -> verify(repository, times(1)).getUnsendedScheduleMessagesAsync(anyInt()),
                () -> verify(repository, times(5)).updateToSendAsync(any(Schedule.class))
        );
    }
}