package br.com.communication.scheduling.domain.service;

import io.quarkus.scheduler.ScheduledExecution;

/**
 * @author Guilherme Alves Silveira
 */
public interface SchedulerCommunicationSenderService {

	void jobSend(ScheduledExecution execution);
}
