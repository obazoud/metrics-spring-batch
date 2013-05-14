Metrics for Spring Batch
-------

[Spring Batch extension](http://static.springsource.org/spring-batch/) for [Yammer's Metrics](http://metrics.codahale.com/) library

[![Build Status](https://buildhive.cloudbees.com/job/obazoud/job/metrics-spring-batch/badge/icon)](https://buildhive.cloudbees.com/job/obazoud/job/metrics-spring-batch/)

About
-------

The `metrics-spring-batch` module integrates [Yammer Metrics](http://metrics.codahale.com/) with [Spring Batch](http://static.springsource.org/spring-batch/) listeners.


Maven
-------

```xml
<dependency>
	<groupId>com.bazoud.metrics</groupId>
	<artifactId>metrics-spring-batch</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

Usage
-------

Spring Context XML:

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
	   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	   xmlns:context="http://www.springframework.org/schema/context"
	   xsi:schemaLocation="
	        http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
            http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
            http://www.springframework.org/schema/batch http://www.springframework.org/schema/batch/spring-batch.xsd">

	<context:annotation-config/>
    <context:component-scan base-package="com.bazoud.metrics.springbatch"/>

    <bean id="metricsRegistry" class="com.codahale.metrics.MetricRegistry" />

    <job id="instrumentedJob" abstract="true" xmlns="http://www.springframework.org/schema/batch">
      <listeners merge="true">
        <listener ref="timedJobExecutionListener"/>
        <listener ref="meteredJobExecutionListener"/>
      </listeners>
    </job>

    <step id="instrumentedStep" abstract="true" xmlns="http://www.springframework.org/schema/batch">
      <tasklet>
        <listeners merge="true">
          <listener ref="timedStepExecutionListener" />
          <listener ref="timedChunkListener" />
          <listener ref="timedItemReadListener" />
          <listener ref="timedItemProcessListener" />
          <listener ref="timedItemWriteListener" />
          <listener ref="meteredStepExecutionListener" />
        </listeners>
      </tasklet>
    </step>

    <!-- Your own job -->
    <job id="jobExemple" parent="instrumentedJob">
      <step id="initialStep" parent="instrumentedStep">
        <listeners merge="true">
          <listener ref="myApplicationListener"/>
        </listeners>
      </step>
    </job>

    (...)

</beans>
```

Contribute
-------

Here's the most direct way to get your work merged into the project.

1. Fork the project
2. Clone down your fork
3. Create a feature branch
4. Hack away and add tests, not necessarily in that order
5. Make sure everything still passes by running tests
6. If necessary, rebase your commits into logical chunks without errors
7. Push the branch up to your fork
8. Send a pull request for your branch

License
-------

This software is licensed under the Apache 2 license, quoted below.

	Copyright 2013 Olivier Bazoud
	
	Licensed under the Apache License, Version 2.0 (the "License"); you may not
	use this file except in compliance with the License. You may obtain a copy of
	the License at
	
	    http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
	WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
	License for the specific language governing permissions and limitations under
	the License.
