/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License")
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.scheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tdclighthouse.prototype.utils.Configuration;

/**
 * @author Ebrahim Aharpour
 * 
 */
public class Scheduler {
    private static final String INTERVAL_LENGTH_PROPERTY = "interval.length";
    private static final String START_TIME_PROPERTY = "start.time";
    private static final Logger LOG = LoggerFactory.getLogger(Scheduler.class);
    private final Timer timer;
    private ExecutorService executor;

    public Scheduler() {
        timer = new Timer(true);
    }

    public synchronized void addTask(final Runnable runnable, Configuration configuration) {
        long initialDelayInMin = getInitialDelayInMilliseconds(configuration);
        long delayInMin = getDelayInMilliseconds(configuration);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (executor != null && !executor.isShutdown() && !executor.isTerminated()) {
                    executor.execute(runnable);
                } else {
                    runnable.run();
                }
            }
        }, initialDelayInMin, delayInMin);
        LOG.info("a new task was schedule with the initailDelay of " + initialDelayInMin
                + " miliseconds and delays of " + delayInMin + " miliseconds");
    }

    protected long getInitialDelayInMilliseconds(Configuration configuration) {
        DateTime dateTime = new DateTime(configuration.getTime(START_TIME_PROPERTY, getMidnight()));
        Seconds secondsBetween = Seconds.secondsBetween(new DateTime(), dateTime);
        return TimeUnit.SECONDS.toMillis((long) secondsBetween.getSeconds() + 30);
    }

    protected long getDelayInMilliseconds(Configuration configuration) {
        double hours = configuration.getItem(INTERVAL_LENGTH_PROPERTY, 8.0, Double.class);
        return Math.round(hours * 3600000);
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    protected Date getMidnight() {
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.AM_PM, Calendar.AM);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        timer.cancel();
    }

}
