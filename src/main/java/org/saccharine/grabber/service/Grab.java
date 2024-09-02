package org.saccharine.grabber.service;

import org.quartz.SchedulerException;

public interface Grab {
    void init() throws SchedulerException;
}