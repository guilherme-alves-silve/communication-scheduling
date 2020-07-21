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

        when(repository.getUnsentScheduleMessagesAsync(50))
                .thenReturn(CompletableFuture.completedFuture(ScheduleFixture.getPastSchedules()));

        when(repository.updateToSendAsync(any(Schedule.class)))
                .thenReturn(CompletableFuture.completedFuture(true));

        service.jobSend();

        assertAll(
                () -> verify(repository, times(1)).getUnsentScheduleMessagesAsync(anyInt()),
                () -> verify(repository, times(5)).updateToSendAsync(any(Schedule.class))
        );
    }
}