package org.bazoud.metrics.springbatch;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.fest.assertions.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SpringBatchMetricsTest {
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired
  private MetricRegistry metricRegistry;
  private ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry)
      .build();

  @Before
  public void before_reporter() {
    reporter.start(1, TimeUnit.SECONDS);
  }

  @After
  public void after_reporter() {
    reporter.stop();
  }

  @Test
  public void test() throws Exception {
    Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
    JobExecution jobExecution = jobLauncherTestUtils.launchJob(new JobParameters(parameters));
    Assert.assertEquals(jobExecution.getExitStatus().getExitDescription(), BatchStatus.COMPLETED, jobExecution.getStatus());
    Map<String, Meter> meters = metricRegistry.getMeters();
    assertThat(meters).hasSize(3);
    assertThat(meters)
        .containsKey("batch.sampleJob.job.metered")
        .containsKey("batch.sampleJob.step.firstStep.step.metered")
        .containsKey("batch.sampleJob.step.secondStep.step.metered");
    Map<String, Timer> timers = metricRegistry.getTimers();
    assertThat(timers).hasSize(5);
    assertThat(timers)
        .containsKey("batch.sampleJob.job.timed")
        .containsKey("batch.sampleJob.step.firstStep.step.timed")
        .containsKey("batch.sampleJob.step.firstStep.chunk.timed")
        .containsKey("batch.sampleJob.step.secondStep.step.timed")
        .containsKey("batch.sampleJob.step.secondStep.chunk.timed");
    Map<String, Gauge> gauges = metricRegistry.getGauges();
    assertThat(gauges).hasSize(0);
  }

}
