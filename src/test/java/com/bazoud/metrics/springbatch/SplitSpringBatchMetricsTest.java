package com.bazoud.metrics.springbatch;

import com.codahale.metrics.*;
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
import java.util.concurrent.TimeUnit;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.extractProperty;

/**
 * @author Gérald Quintana
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class SplitSpringBatchMetricsTest {
  @Autowired
  private JobLauncherTestUtils splitJobLauncherTestUtils;
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
  public void testSplit() throws Exception {
    Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();
    JobExecution jobExecution = splitJobLauncherTestUtils.launchJob(new JobParameters(parameters));
    Assert.assertEquals(jobExecution.getExitStatus().getExitDescription(), BatchStatus.COMPLETED, jobExecution.getStatus());
    Map<String, Meter> meters = metricRegistry.getMeters();
    assertThat(meters).hasSize(3);
    assertThat(meters)
        .containsKey("batch.splitJob.job.metered")
        .containsKey("batch.splitJob.step.firstSplitStep.step.metered")
        .containsKey("batch.splitJob.step.secondSplitStep.step.metered");
    assertThat(extractProperty("count", Number.class).from(meters.values())).contains(1L).doesNotContain(0L);
    Map<String, Timer> timers = metricRegistry.getTimers();
    assertThat(timers).hasSize(5);
    assertThat(timers)
        .containsKey("batch.splitJob.job.timed")
        .containsKey("batch.splitJob.step.firstSplitStep.step.timed")
        .containsKey("batch.splitJob.step.firstSplitStep.chunk.timed")
        .containsKey("batch.splitJob.step.secondSplitStep.step.timed")
        .containsKey("batch.splitJob.step.secondSplitStep.chunk.timed");
    assertThat(extractProperty("count", Number.class).from(timers.values())).contains(1L).doesNotContain(0L);
    Map<String, Gauge> gauges = metricRegistry.getGauges();
    assertThat(gauges).hasSize(0);
  }

}
