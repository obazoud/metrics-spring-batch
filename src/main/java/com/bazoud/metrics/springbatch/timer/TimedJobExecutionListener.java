package com.bazoud.metrics.springbatch.timer;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import static com.bazoud.metrics.springbatch.MetricsHelper.JOB_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
@Order(value = 1)
public class TimedJobExecutionListener implements JobExecutionListener {
  @Autowired
  private TimerHolder timerHolder;

  @Override
  public void beforeJob(JobExecution jobExecution) {
    String jobName = jobExecution.getJobInstance().getJobName();
    timerHolder.time(jobName, null, JOB_KIND);
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    String jobName = jobExecution.getJobInstance().getJobName();
    timerHolder.stop(jobName, null, JOB_KIND);
  }

  public void setTimerHolder(TimerHolder timerHolder) {
    this.timerHolder = timerHolder;
  }

}
