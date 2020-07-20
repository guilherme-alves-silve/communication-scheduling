package br.com.communication.scheduling.test.db;

import br.com.communication.scheduling.domain.entity.Schedule;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import lombok.experimental.UtilityClass;
import org.jboss.logging.Logger;

import java.util.function.Supplier;
import java.util.stream.Stream;

@UtilityClass
public class RepositoryUtils {

    private static final Logger LOGGER = Logger.getLogger(RepositoryUtils.class);

    public static boolean save(ScheduleRepository repository, Schedule... schedules) {
        return tryExecution(() ->
                Stream.of(schedules)
                        .allMatch(schedule -> repository.saveAsync(schedule)
                                .join())
        );
    }

    public static boolean delete(ScheduleRepository repository, Schedule... schedules) {
        return tryExecution(() ->
                Stream.of(schedules)
                        .allMatch(schedule -> repository.deleteAsync(schedule)
                                .join())
        );
    }

    private static boolean tryExecution(Supplier<Boolean> block) {
        try {
            return block.get();
        } catch (Exception ex) {
            LOGGER.error("Error: " + ex.getMessage(), ex);
            return false;
        }
    }
}
