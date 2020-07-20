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
            .setTo("joaopedro@email.com")
            .setMessage("An e-mail message")
            .setTimeToSend(now)
            .setType(MessageType.EMAIL));

        assertAll(
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