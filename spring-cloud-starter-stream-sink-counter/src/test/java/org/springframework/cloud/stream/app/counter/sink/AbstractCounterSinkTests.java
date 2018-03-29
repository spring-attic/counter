/*
 * Copyright 2015-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.stream.app.counter.sink;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.analytics.metrics.redis.RedisMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.app.test.redis.RedisTestSupport;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * @author Mark Pollack
 * @author Marius Bogoevici
 * @author Gary Russell
 * @author Artem Bilan
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {
				"spring.metrics.export.delayMillis:10",
				"spring.metrics.export.includes:"})
@DirtiesContext
public abstract class AbstractCounterSinkTests {

	@Rule
	public RedisTestSupport redisAvailableRule = new RedisTestSupport();

	@Autowired
	protected Sink sink;

	@Autowired
	@Qualifier("metricRepository")
	private RedisMetricRepository redisMetricRepository;

	@Before
	public void init() {
		assertThat(this.redisMetricRepository, instanceOf(RedisMetricRepository.class));
		redisMetricRepository.reset("counter.simpleCounter");
	}

	@After
	public void clear() {
		this.redisMetricRepository.reset("counter.simpleCounter");
	}

	protected RedisMetricRepository getRedisMetricRepository() {
		return this.redisMetricRepository;
	}

	@SpringBootApplication
	public static class CounterSinkApplication {

	}

}
