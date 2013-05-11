package org.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static org.bazoud.metrics.springbatch.MetricsHelper.PROCESS_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */

@Component
@Order(value = 1)
public class TimedItemProcessListener implements ItemProcessListener, StepExecutionListener {
  @Autowired
  private TimerHolder timerHolder;
  private StepExecution stepExecution;

  @Override
  public void beforeProcess(Object item) {
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = stepExecution.getStepName();
    timerHolder.time(jobName, stepName, PROCESS_KIND);
  }

  @Override
  public void afterProcess(Object item, Object result) {
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = stepExecution.getStepName();
    timerHolder.stop(jobName, stepName, PROCESS_KIND);
  }

  @Override
  public void onProcessError(Object item, Exception e) {
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecution = stepExecution;
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    return null;
  }

  public void setTimerHolder(TimerHolder timerHolder) {
    this.timerHolder = timerHolder;
  }

}
