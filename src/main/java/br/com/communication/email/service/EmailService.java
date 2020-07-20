package br.com.communication.email.service;

import br.com.communication.email.dto.EmailDTO;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailService.class);

    public String sendEmail(final EmailDTO dto) {
        LOGGER.info("Received dto: " + dto);
        return "{\"sent\": true}";
    }
}
