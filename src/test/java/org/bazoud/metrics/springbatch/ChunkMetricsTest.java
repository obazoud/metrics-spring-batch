package org.bazoud.metrics.springbatch;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
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
 * @author @obazoud (Olivier Bazoud)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ChunkMetricsTest {
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
    assertThat(meters).hasSize(2);
    assertThat(meters)
        .containsKey("batch.sampleJob.job.metered")
        .containsKey("batch.sampleJob.step.chunkStep.step.metered");
    assertThat(extractProperty("count", Number.class).from(meters.values())).contains(1L).doesNotContain(0L);
    Map<String, Timer> timers = metricRegistry.getTimers();
    assertThat(timers).hasSize(6);
    assertThat(timers)
        .containsKey("batch.sampleJob.job.timed")
        .containsKey("batch.sampleJob.step.chunkStep.chunk.timed")
        .containsKey("batch.sampleJob.step.chunkStep.step.timed")
        .containsKey("batch.sampleJob.step.chunkStep.read.timed")
        .containsKey("batch.sampleJob.step.chunkStep.process.timed")
        .containsKey("batch.sampleJob.step.chunkStep.write.timed");
    assertThat(extractProperty("count", Number.class).from(timers.values())).contains(1L, 3L, 4L).doesNotContain(0L);
    Map<String, Gauge> gauges = metricRegistry.getGauges();
    assertThat(gauges).hasSize(0);
  }

}
