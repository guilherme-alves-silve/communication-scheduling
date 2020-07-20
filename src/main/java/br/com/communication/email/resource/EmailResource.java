package br.com.communication.email.resource;

import br.com.communication.email.dto.EmailDTO;
import br.com.communication.email.service.EmailService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/email")
public class EmailResource {

    private final EmailService service;

    @Inject
    public EmailResource(EmailService service) {
        this.service = service;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String sendEmailToClient(final EmailDTO dto) {
        return service.sendEmail(dto);
    }
}
