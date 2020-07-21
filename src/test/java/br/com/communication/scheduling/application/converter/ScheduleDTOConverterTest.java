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
package br.com.communication.scheduling.application.converter;

import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.domain.entity.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleDTOConverterTest {

    private ScheduleDTOConverter converter;

    @BeforeEach
    public void beforeEach() {
        this.converter = new ScheduleDTOConverter();
    }

    @Test
    void shouldConvertDTOToSchedule() {

        final var now = LocalDateTime.now();
        final var schedule = converter.convert(new ScheduleDTO()
            .setId(1L)
            .setTo("joaopedro@email.com")
            .setMessage("An e-mail message")
            .setTimeToSend(now)
            .setType(MessageType.EMAIL));

        assertAll(
                () -> assertEquals(schedule.getId(), 1L, "id"),
                () -> assertEquals(schedule.getTo(), "joaopedro@email.com", "to"),
                () -> assertEquals(schedule.getMessage(), "An e-mail message", "message"),
                () -> assertEquals(schedule.getTimeToSend(), now, "time to send"),
                () -> assertEquals(schedule.getType(), MessageType.EMAIL, "email")
        );
    }

    @Test
    void shouldThrowExceptionIfDTOIsNull() {

        final var exception = assertThrows(NullPointerException.class,
                () -> converter.convert(null));
        assertEquals(exception.getMessage(), "dto cannot be null!");
    }
}