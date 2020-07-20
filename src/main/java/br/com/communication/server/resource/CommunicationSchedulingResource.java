package br.com.communication.server.resource;

import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.application.usecase.ScheduleCommunicationMessageUseCase;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/scheduling")
public class CommunicationSchedulingResource {

    private static final Logger LOGGER = Logger.getLogger(CommunicationSchedulingResource.class);

    private final ScheduleCommunicationMessageUseCase useCase;

    @Inject
    public CommunicationSchedulingResource(final ScheduleCommunicationMessageUseCase useCase) {
        this.useCase = useCase;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> post(final ScheduleDTO schedule) {

        final var response = useCase.sendSchedule(schedule)
                .thenApply(result -> result ? Response.ok().build() : Response.status(Status.BAD_REQUEST).build());
        return Uni.createFrom()
                .completionStage(response);
    }
}
