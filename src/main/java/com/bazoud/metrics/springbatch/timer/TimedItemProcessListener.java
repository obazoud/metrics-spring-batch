package com.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.bazoud.metrics.springbatch.MetricsHelper.PROCESS_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */

@Component
@Order(value = 1)
public class TimedItemProcessListener implements ItemProcessListener, StepExecutionListener {
  @Autowired
  private TimerHolder timerHolder;
  private StepExecutionHolder stepExecutionHolder =new StepExecutionHolder();

  @Override
  public void beforeProcess(Object item) {
    StepExecution stepExecution = stepExecutionHolder.getCurrent();
    if (stepExecution != null) {
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      String stepName = stepExecution.getStepName();
      timerHolder.time(jobName, stepName, PROCESS_KIND);
    }
  }

  @Override
  public void afterProcess(Object item, Object result) {
    StepExecution stepExecution = stepExecutionHolder.getCurrent();
    if (stepExecution != null) {
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      String stepName = stepExecution.getStepName();
      timerHolder.stop(jobName, stepName, PROCESS_KIND);
    }
  }

  @Override
  public void onProcessError(Object item, Exception e) {
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    this.stepExecutionHolder.before(stepExecution);
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    this.stepExecutionHolder.after(stepExecution);
    return null;
  }

  public void setTimerHolder(TimerHolder timerHolder) {
    this.timerHolder = timerHolder;
  }

  public StepExecutionHolder getStepExecutionHolder() {
    return stepExecutionHolder;
  }

  public void setStepExecutionHolder(StepExecutionHolder stepExecutionHolder) {
    this.stepExecutionHolder = stepExecutionHolder;
  }
}
