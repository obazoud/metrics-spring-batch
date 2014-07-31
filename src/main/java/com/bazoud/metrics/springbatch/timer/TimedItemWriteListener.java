package com.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.bazoud.metrics.springbatch.MetricsHelper.WRITE_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
@Order(value = 1)
public class TimedItemWriteListener implements ItemWriteListener, StepExecutionListener {
  @Autowired
  private TimerHolder timerHolder;
  private StepExecutionHolder stepExecutionHolder = new StepExecutionHolder();

  @Override
  public void beforeWrite(List items) {
    StepExecution stepExecution = stepExecutionHolder.getCurrent();
    if (stepExecution != null) {
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      String stepName = stepExecution.getStepName();
      timerHolder.time(jobName, stepName, WRITE_KIND);
    }
  }

  @Override
  public void afterWrite(List items) {
    StepExecution stepExecution = stepExecutionHolder.getCurrent();
    if (stepExecution != null) {
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      String stepName = stepExecution.getStepName();
      timerHolder.stop(jobName, stepName, WRITE_KIND);
    }
  }

  @Override
  public void onWriteError(Exception exception, List items) {
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
