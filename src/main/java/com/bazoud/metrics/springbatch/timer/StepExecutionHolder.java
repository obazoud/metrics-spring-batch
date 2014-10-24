package com.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.StepExecution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Keeps the running StepExecution per thread
 * @author Gérald Quintana
 */
public class StepExecutionHolder {
  /**
   * Thread Id to Running Step Execution
   */
  private ConcurrentMap<Long, StepExecution> stepExecutions = new ConcurrentHashMap<Long, StepExecution>();
  private long getCurrentThreadId() {
    return Thread.currentThread().getId();
  }
  /**
   * Called when step starts
   */
  public void before(StepExecution stepExecution) {
    stepExecutions.putIfAbsent(getCurrentThreadId(), stepExecution);
  }
  public StepExecution getCurrent() {
    return stepExecutions.get(getCurrentThreadId());
  }
  /**
   * Called when step stops
   */
  public boolean after(StepExecution stepExecution) {
    StepExecution localStepExecution = getCurrent();
    if (stepExecution != null && localStepExecution.getId() == stepExecution.getId()) {
      stepExecutions.remove(getCurrentThreadId());
      return true;
    } else {
      return false;
    }
  }
}
