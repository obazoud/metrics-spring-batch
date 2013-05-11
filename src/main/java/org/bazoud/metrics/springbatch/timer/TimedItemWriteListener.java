package org.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.bazoud.metrics.springbatch.MetricsHelper.WRITE_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
@Order(value = 1)
public class TimedItemWriteListener implements ItemWriteListener, StepExecutionListener {
  @Autowired
  private TimerHolder timerHolder;
  private StepExecution stepExecution;

  @Override
  public void beforeWrite(List items) {
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = stepExecution.getStepName();
    timerHolder.time(jobName, stepName, WRITE_KIND);
  }

  @Override
  public void afterWrite(List items) {
    String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
    String stepName = stepExecution.getStepName();
    timerHolder.stop(jobName, stepName, WRITE_KIND);
  }

  @Override
  public void onWriteError(Exception exception, List items) {
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
