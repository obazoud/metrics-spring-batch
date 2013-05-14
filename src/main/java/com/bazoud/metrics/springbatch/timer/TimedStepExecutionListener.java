package com.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.bazoud.metrics.springbatch.MetricsHelper.STEP_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
@Order(value = 1)
public class TimedStepExecutionListener implements StepExecutionListener {
  @Autowired
  private TimerHolder timerHolder;

  @Override
  public void beforeStep(StepExecution stepExecution) {
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = stepExecution.getStepName();
    timerHolder.time(jobName, stepName, STEP_KIND);
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = stepExecution.getStepName();
    timerHolder.stop(jobName, stepName, STEP_KIND);
    return null;
  }

  public void setTimerHolder(TimerHolder timerHolder) {
    this.timerHolder = timerHolder;
  }

}

