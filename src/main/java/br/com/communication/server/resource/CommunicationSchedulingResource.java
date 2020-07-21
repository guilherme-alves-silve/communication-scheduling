package br.com.communication.server.resource;

import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.application.usecase.ConsultStatusOfCommunicationMessageUseCase;
import br.com.communication.scheduling.application.usecase.RemoveScheduleCommunicationMessageUseCase;
import br.com.communication.scheduling.application.usecase.ScheduleCommunicationMessageUseCase;
import io.smallrye.mutiny.Uni;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/scheduling")
public class CommunicationSchedulingResource {

    private final ScheduleCommunicationMessageUseCase scheduleUseCase;
    private final ConsultStatusOfCommunicationMessageUseCase consultStatusUseCase;
    private final RemoveScheduleCommunicationMessageUseCase removeScheduleUseCase;

    @Inject
    public CommunicationSchedulingResource(
            final ScheduleCommunicationMessageUseCase scheduleUseCase,
            final ConsultStatusOfCommunicationMessageUseCase consultStatusUseCase,
            final RemoveScheduleCommunicationMessageUseCase removeScheduleUseCase) {
        this.scheduleUseCase = scheduleUseCase;
        this.consultStatusUseCase = consultStatusUseCase;
        this.removeScheduleUseCase = removeScheduleUseCase;
    }

    @POST
    @Path("/schedule")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> postSchedule(final ScheduleDTO schedule) {

        final var response = scheduleUseCase.sendSchedule(schedule)
                .thenApply(result -> result ? Response.ok().build() : Response.status(Status.INTERNAL_SERVER_ERROR).build());
        return Uni.createFrom()
                .completionStage(response);
    }

    @GET
    @Path("/status/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getScheduleStatus(final @PathParam("id") Long id) {

        final var response = consultStatusUseCase.consultStatusMessage(ScheduleDTO.withId(id))
                .thenApply(result -> Response.ok(result).build());
        return Uni.createFrom()
                .completionStage(response);
    }

    @DELETE
    @Path("/remove/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> deleteSchedule(final @PathParam("id") Long id) {

        final var response = removeScheduleUseCase.removeSchedule(ScheduleDTO.withId(id))
                .thenApply(result -> result? Response.noContent().build() : Response.status(Status.INTERNAL_SERVER_ERROR).build());
        return Uni.createFrom()
                .completionStage(response);
    }
}
