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
package br.com.communication.scheduling.application.usecase;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import br.com.communication.scheduling.application.converter.ScheduleDTOConverter;
import br.com.communication.scheduling.application.dto.ScheduleDTO;
import br.com.communication.scheduling.domain.repository.ScheduleRepository;
import java.util.concurrent.CompletableFuture;

/**
 * @author Guilherme Alves Silveira
 */
@ApplicationScoped
public class ScheduleCommunicationMessageUseCase {

	private final ScheduleRepository repository;
	private final ScheduleDTOConverter converter;

	@Inject
	public ScheduleCommunicationMessageUseCase (
			final ScheduleRepository repository,
			final ScheduleDTOConverter converter) {
		this.repository = repository;
		this.converter = converter;
	}

	@Transactional(Transactional.TxType.REQUIRED)
	public CompletableFuture<Boolean> sendSchedule(final ScheduleDTO dto) {

		final var schedule = converter.convert(dto);
		return repository.saveAsync(schedule);
	}
}
