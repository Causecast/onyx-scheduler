/*
 * Copyright (C) 2015
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onyxscheduler.quartz;

import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@ConfigurationProperties(prefix = "quartz")
public class QuartzProperties {

  private Integer threadCount;

  private JobStoreProperties jobstore = new JobStoreProperties();

  public void setThreadCount(int threadCount) {
    this.threadCount = threadCount;
  }

  public JobStoreProperties getJobstore() {
    return jobstore;
  }

  public static class JobStoreProperties {

    private Boolean isClustered;

    public void setClustered(boolean isClustered) {
      this.isClustered = isClustered;
    }

    public Properties buildQuartzProperties() {
      Properties props = new Properties();
            /* using setProperty with string parameters, since quartz uses getProperty,
            which gets nulls when getting non string values
             */
      //using properties to avoid serialization issues of objects
      props.setProperty(StdSchedulerFactory.PROP_JOB_STORE_USE_PROP, Boolean.toString(true));
      if (isClustered != null) {
        props.setProperty(StdSchedulerFactory.PROP_JOB_STORE_PREFIX + ".isClustered",
                          isClustered.toString());
      }
      return props;
    }
  }

  public Properties buildQuartzProperties() {
    Properties props = new Properties();
    //skip the check to don't bother with quartz updates
    props.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, Boolean.toString(true));
    props.setProperty(StdSchedulerFactory.PROP_SCHED_INSTANCE_ID,
                      StdSchedulerFactory.AUTO_GENERATE_INSTANCE_ID);
    if (threadCount != null) {
      props.setProperty(SchedulerFactoryBean.PROP_THREAD_COUNT, threadCount.toString());
    }
    return props;
  }

}
