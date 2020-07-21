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
package br.com.communication.server.resource;

import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.application.usecase.ConsultStatusOfCommunicationMessageUseCase;
import br.com.communication.scheduling.application.usecase.RemoveScheduleCommunicationMessageUseCase;
import br.com.communication.scheduling.application.usecase.ScheduleCommunicationMessageUseCase;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/scheduling")
public class CommunicationSchedulingResource {

    private static final Logger LOGGER = Logger.getLogger(CommunicationSchedulingResource.class);

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
                .thenApply(result -> result? Response.noContent().build() : Response.status(Status.INTERNAL_SERVER_ERROR).build())
                .exceptionally(throwable -> {
                    LOGGER.error(throwable);
                    return Response.status(Status.INTERNAL_SERVER_ERROR).build();
                });

        return Uni.createFrom()
                .completionStage(response);
    }
}
