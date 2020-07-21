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
package br.com.communication.scheduling.application.dto;

import br.com.communication.scheduling.domain.entity.MessageType;
import java.time.LocalDateTime;
import java.util.StringJoiner;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Guilherme Alves Silveira
 */
@Setter
@Getter
@EqualsAndHashCode
@Accessors(chain = true)
public class ScheduleDTO {

	private Long id;
	private String to;
	private String message;
	private MessageType type;
	private LocalDateTime timeToSend;

	public static ScheduleDTO withId(final Long id) {
		return new ScheduleDTO().setId(id);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", ScheduleDTO.class.getSimpleName() + "[", "]")
				.add("to='" + to + "'")
				.add("message='" + message + "'")
				.add("type=" + type)
				.add("timeToSend=" + timeToSend)
				.toString();
	}
}
