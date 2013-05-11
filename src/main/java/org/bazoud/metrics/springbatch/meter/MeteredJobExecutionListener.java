package org.bazoud.metrics.springbatch.meter;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.bazoud.metrics.springbatch.MetricsHelper.METERED_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
@Order(value = 1)
public class MeteredJobExecutionListener implements JobExecutionListener {
  @Autowired
  private MeterHolder meterHolder;

  @Override
  public void beforeJob(JobExecution jobExecution) {
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    String jobName = jobExecution.getJobInstance().getJobName();
    meterHolder.mark(jobName);
  }

  public void setMeterHolder(MeterHolder meterHolder) {
    this.meterHolder = meterHolder;
  }

}
