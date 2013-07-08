package com.bazoud.metrics.springbatch.timer;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.codahale.metrics.MetricRegistry.name;
import static com.bazoud.metrics.springbatch.MetricsHelper.METRICS_BATCH_GROUP;
import static com.bazoud.metrics.springbatch.MetricsHelper.STEP_KIND;
import static com.bazoud.metrics.springbatch.MetricsHelper.TIMED_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
public class TimerHolder implements DisposableBean {
  @Autowired
  private MetricRegistry metricRegistry;
  private String group = METRICS_BATCH_GROUP;
  private Map<String, Timer.Context> timerContexts = new ConcurrentHashMap();

  public void time(String jobName, String stepName, String kind) {
    String metricName = stepName == null ?
        name(group, jobName, kind, TIMED_KIND) :
        name(group, jobName, STEP_KIND, stepName, kind, TIMED_KIND);
    Timer timer = metricRegistry.timer(metricName);
    Timer.Context timerContext = timer.time();
    timerContexts.put(metricName, timerContext);
  }

  public void stop(String jobName, String stepName, String kind) {
    String metricName = stepName == null ?
        name(group, jobName, kind, TIMED_KIND) :
        name(group, jobName, STEP_KIND, stepName, kind, TIMED_KIND);
    Timer.Context timerContext = timerContexts.get(metricName);
    if (timerContext != null) {
      timerContext.stop();
      timerContexts.remove(metricName);
    }
  }

  public void setMetricRegistry(MetricRegistry metricsRegistry) {
    this.metricRegistry = metricsRegistry;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  @Override
  public void destroy() throws Exception {
    timerContexts.clear();
  }

}
