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
import io.quarkus.scheduler.Scheduled;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;

@ApplicationScoped
public class SchedulerCommunicationSenderServiceImpl implements SchedulerCommunicationSenderService {

    private static final Logger LOGGER = Logger.getLogger(SchedulerCommunicationSenderServiceImpl.class);

    private static final int LIMIT_TO_SEND = 50;

    private final Lock lock;
    private final ScheduleRepository repository;
    private final Jsonb jsonb;
    private final String servicesUrl;

    @Inject
    public SchedulerCommunicationSenderServiceImpl(
            final ScheduleRepository repository,
            final @ConfigProperty(name = "services.url") String servicesUrl) {
        this.repository = repository;
        this.servicesUrl = servicesUrl;
        this.jsonb = JsonbBuilder.create();
        this.lock = new ReentrantLock();
    }

    @Scheduled(every = "10s")
    public void jobSend() {

        lock.lock();
        try {
            final var messages = repository.getUnsentScheduleMessagesAsync(LIMIT_TO_SEND).join();
            if (messages.isEmpty()) {
                LOGGER.info("No message to send!");
                return;
            }

            LOGGER.info("Will try to send " + messages.size() + " messages!");
            final var httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            messages.forEach(schedule -> {
                final var json = jsonb.toJson(schedule);
                final var urlServer = String.format("%s/%s", servicesUrl, schedule.getType().name().toLowerCase());
                try {
                    final var httpRequest = newBuilder(URI.create(urlServer))
                            .timeout(Duration.ofSeconds(10))
                            .header("Content-Type", "application/json")
                            .header("Accept", "*/*")
                            .POST(BodyPublishers.ofString(json))
                            .build();
                    final var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                    if (httpResponse.statusCode() == HTTP_OK) {
                        updateToSendAsync(Schedule.withId(schedule.getId()));
                    } else {
                        LOGGER.error("Failed to send json: " + json);
                    }
                } catch (Exception ex) {
                    LOGGER.info("Request failed, message: " + ex.getMessage());
                }
            });
        } finally {
            lock.unlock();
        }
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void updateToSendAsync(final Schedule schedule) {
        repository.updateToSendAsync(schedule)
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    LOGGER.info(schedule + " : " + throwable.getMessage());
                    return;
                }

                LOGGER.info("Schedule of id " + schedule.getId() + " succeeded");
            });
    }
}
