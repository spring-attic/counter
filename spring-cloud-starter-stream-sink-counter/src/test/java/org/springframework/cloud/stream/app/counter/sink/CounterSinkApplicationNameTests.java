/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.counter.sink;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.integration.test.matcher.EqualsResultMatcher.equalsResult;
import static org.springframework.integration.test.matcher.EventuallyMatcher.eventually;

import org.junit.Test;

import org.springframework.boot.actuate.metrics.Metric;
import org.springframework.integration.test.matcher.EqualsResultMatcher;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Artem Bilan
 */
@TestPropertySource(properties = "spring.application.name:counter-sink")
public class CounterSinkApplicationNameTests extends AbstractCounterSinkTests {

	@Test
	public void testIncrement() throws InterruptedException {
		getRedisMetricRepository().reset("counter.counter-sink");
		assertNotNull(this.sink.input());
		Message<String> message = MessageBuilder.withPayload("...").build();
		sink.input().send(message);

		// Note: If the name of the counter does not start with 'counter' or 'metric' the
		// 'counter.' prefix is added
		// by the DefaultCounterService and BufferCounterService
		assertThat(1L, eventually(equalsResult(new EqualsResultMatcher.Evaluator<Long>() {

			@Override
			public Long evaluate() {
				Metric<?> metric = getRedisMetricRepository().findOne("counter.counter-sink");
				return metric != null ? metric.getValue().longValue() : null;
			}

		})));
	}

}
