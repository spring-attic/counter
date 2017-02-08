/*
 * Copyright 2015-2016 the original author or authors.
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.repository.MetricRepository;
import org.springframework.boot.actuate.metrics.repository.redis.RedisMetricRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.Bindings;
import org.springframework.cloud.stream.app.test.redis.RedisTestSupport;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

/**
 * @author Mark Pollack
 * @author Marius Bogoevici
 * @author Gary Russell
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest({ "server.port:0", "spring.metrics.export.delayMillis:10", "classes:AbstractCounterSinkTests.CounterSinkApplication.class" })
@DirtiesContext
public abstract class AbstractCounterSinkTests {

	@Rule
	public RedisTestSupport redisAvailableRule = new RedisTestSupport();

	protected int sleepTime = 100;

	@Autowired
	@Bindings(CounterSinkConfiguration.class)
	protected Sink sink;

	@Autowired
	@Qualifier("metricRepository")
	protected MetricRepository redisMetricRepository;

	@Before
	public void init() {
		assertThat(this.redisMetricRepository, instanceOf(RedisMetricRepository.class));
		redisMetricRepository.reset("counter.simpleCounter");
	}

	@After
	public void clear() {
		this.redisMetricRepository.reset("counter.simpleCounter");
	}

	@SpringBootApplication
	public static class CounterSinkApplication {

	}

}
