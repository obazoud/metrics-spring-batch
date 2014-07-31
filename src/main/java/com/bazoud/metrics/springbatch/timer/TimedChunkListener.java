package com.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.bazoud.metrics.springbatch.MetricsHelper.CHUNK_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */

@Component
@Order(value = 1)
public class TimedChunkListener implements ChunkListener, StepExecutionListener {
  @Autowired
  private TimerHolder timerHolder;
  private StepExecutionHolder stepExecutionHolder = new StepExecutionHolder();

  @Override
  public void beforeChunk(ChunkContext context) {
    StepExecution stepExecution = stepExecutionHolder.getCurrent();
    if (stepExecution != null) {
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      String stepName = stepExecution.getStepName();
      timerHolder.time(jobName, stepName, CHUNK_KIND);
    }
  }

  @Override
  public void afterChunk(ChunkContext context) {
    StepExecution stepExecution = stepExecutionHolder.getCurrent();
    if (stepExecution != null) {
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      String stepName = stepExecution.getStepName();
      timerHolder.stop(jobName, stepName, CHUNK_KIND);
    }
  }

  @Override
  public void afterChunkError(ChunkContext context) {
    StepExecution stepExecution = stepExecutionHolder.getCurrent();
    if (stepExecution != null) {
      String jobName = stepExecution.getJobExecution().getJobInstance().getJobName();
      String stepName = stepExecution.getStepName();
      timerHolder.stop(jobName, stepName, CHUNK_KIND);
    }
  }

  @Override
  public void beforeStep(StepExecution stepExecution) {
    stepExecutionHolder.before(stepExecution);
  }

  @Override
  public ExitStatus afterStep(StepExecution stepExecution) {
    stepExecutionHolder.after(stepExecution);
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
