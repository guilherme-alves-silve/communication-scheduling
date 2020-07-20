package br.com.communication.scheduling.infrastructure.fixture;

import br.com.communication.scheduling.domain.entity.MessageType;
import br.com.communication.scheduling.domain.entity.Schedule;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@UtilityClass
public class ScheduleFixture {

    private static final MessageType[] MESSAGE_TYPES = MessageType.values();

    public static List<Schedule> getPastSchedules() {

        final var random = new Random();
        final var schedules = new ArrayList<Schedule>(5);
        for (int i = 0; i < 5; i++) {
            final var randomMessageType = MESSAGE_TYPES[random.nextInt(MESSAGE_TYPES.length)];
            final var schedule = Schedule.builder()
                    .message("Past Message " + i)
                    .to("past.email" + i + "@email.com")
                    .type(randomMessageType)
                    .timeToSend(LocalDateTime.now().minusMinutes(random.nextInt(59)))
                    .build();
            schedules.add(schedule);
        }

        return schedules;
    }

    public static List<Schedule> getFutureSchedules() {

        final var random = new Random();
        final var schedules = new ArrayList<Schedule>(5);
        for (int i = 0; i < 5; i++) {
            final var randomMessageType = MESSAGE_TYPES[random.nextInt(MESSAGE_TYPES.length)];
            final var schedule = Schedule.builder()
                    .message("Future Message " + i)
                    .to("future.email" + i + "@email.com")
                    .type(randomMessageType)
                    .timeToSend(LocalDateTime.now().plusMinutes(1 + random.nextInt(58)))
                    .build();
            schedules.add(schedule);
        }

        return schedules;
    }
}
