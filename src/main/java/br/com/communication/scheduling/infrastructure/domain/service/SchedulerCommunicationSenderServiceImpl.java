package br.com.communication.scheduling.infrastructure.domain.service;

import br.com.communication.scheduling.domain.entity.Schedule;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import br.com.communication.scheduling.domain.service.SchedulerCommunicationSenderService;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;

import static br.com.communication.scheduling.infrastructure.domain.util.Constants.URL_SERVER;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.http.HttpRequest.BodyPublishers;
import static java.net.http.HttpRequest.newBuilder;

@ApplicationScoped
public class SchedulerCommunicationSenderServiceImpl implements SchedulerCommunicationSenderService {

    private static final Logger LOGGER = Logger.getLogger(SchedulerCommunicationSenderServiceImpl.class);

    private static final int LIMIT_TO_SEND = 50;

    private final ScheduleRepository repository;
    private final Jsonb jsonb;

    @Inject
    public SchedulerCommunicationSenderServiceImpl(final ScheduleRepository repository) {
        this.repository = repository;
        this.jsonb = JsonbBuilder.create();
    }

    @Transactional
    @Scheduled(every = "10s")
    public void jobSend(ScheduledExecution execution) {

        final var messages = repository.getUnsendedScheduleMessagesAsync(LIMIT_TO_SEND).join();
        LOGGER.info("Will try to send " + messages.size() + " messages!");
        final var httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        messages.forEach(schedule -> {
            final var json = jsonb.toJson(schedule);
            final var urlServer = String.format(URL_SERVER, schedule.getType().name().toLowerCase());
            try {
                final var httpRequest = newBuilder(URI.create(urlServer))
                        .timeout(Duration.ofSeconds(10))
                        .header("Content-Type", "application/json")
                        .header("Accept", "*/*")
                        .POST(BodyPublishers.ofString(json))
                        .build();
                final var httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                if (httpResponse.statusCode() == HTTP_OK) {
                    updateToSendAsync(schedule);
                } else {
                    LOGGER.error("Failed to send " + schedule);
                }
            } catch (Exception ex) {
                LOGGER.info("Request failed, message: " + ex.getMessage(), ex);
            }
        });
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    private void updateToSendAsync(final Schedule schedule) {
        repository.updateToSendAsync(schedule)
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    LOGGER.info(schedule + " : " + throwable.getMessage(), throwable);
                    return;
                }

                LOGGER.info("Schedule of id " + schedule.getId() + " succeeded");
            });
    }
}
