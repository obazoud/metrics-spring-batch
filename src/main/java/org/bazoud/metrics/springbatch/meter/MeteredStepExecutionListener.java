package org.bazoud.metrics.springbatch.meter;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.bazoud.metrics.springbatch.MetricsHelper.METERED_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
@Order(value = 1)
public class MeteredStepExecutionListener implements StepExecutionListener {
  @Autowired
  private MeterHolder meterHolder;

  @Override
  public void beforeStep(StepExecution stepExecution) {
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = stepExecution.getStepName();
    meterHolder.mark(jobName, stepName);
    return null;
  }

  public void setMeterHolder(MeterHolder meterHolder) {
    this.meterHolder = meterHolder;
  }
}
