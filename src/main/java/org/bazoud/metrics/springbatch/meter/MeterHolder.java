package org.bazoud.metrics.springbatch.meter;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.codahale.metrics.MetricRegistry.name;
import static org.bazoud.metrics.springbatch.MetricsHelper.JOB_KIND;
import static org.bazoud.metrics.springbatch.MetricsHelper.METERED_KIND;
import static org.bazoud.metrics.springbatch.MetricsHelper.METRICS_BATCH_GROUP;
import static org.bazoud.metrics.springbatch.MetricsHelper.STEP_KIND;

/**
 * @author @obazoud (Olivier Bazoud)
 */
@Component
public class MeterHolder implements DisposableBean {
  @Autowired
  private MetricRegistry metricRegistry;
  private String group = METRICS_BATCH_GROUP;

  private Map<String, Meter> meters = new ConcurrentHashMap<>();

  public void mark(String jobName) {
    mark(jobName, null);
  }

  public void mark(String jobName, String stepName) {
    String metricName = stepName == null ?
        name(group, jobName, JOB_KIND, METERED_KIND) :
        name(group, jobName, STEP_KIND, stepName, STEP_KIND, METERED_KIND);

    Meter meter = metricRegistry.meter(metricName);
    meter.mark();
  }

  @Override
  public void destroy() throws Exception {
    meters.clear();
  }

  public void setMetricRegistry(MetricRegistry metricRegistry) {
    this.metricRegistry = metricRegistry;
  }

  public void setGroup(String group) {
    this.group = group;
  }

}
