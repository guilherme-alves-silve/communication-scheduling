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
                    .timeToSend(LocalDateTime.now().minusMinutes(1 + random.nextInt(58)))
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
