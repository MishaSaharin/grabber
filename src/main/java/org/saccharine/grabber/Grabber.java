package org.saccharine.grabber;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.saccharine.grabber.db.AppDataBaseService;
import org.saccharine.grabber.service.Grab;
import org.saccharine.grabber.service.GrabberJob;
import org.saccharine.grabber.service.SiteParser;
import org.saccharine.grabber.utils.SiteAppDateTimeParser;
import org.saccharine.grabber.web.WebViewer;
import sun.misc.Signal;

import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final SiteParser parse;
    private final AppDataBaseService appDataBase;
    private final Scheduler scheduler;
    private final int time;

    public Grabber(SiteParser parse, AppDataBaseService appDataBase, Scheduler scheduler, int time) {
        this.parse = parse;
        this.appDataBase = appDataBase;
        this.scheduler = scheduler;
        this.time = time;
    }

    @Override
    public void init() throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("appDataBase", appDataBase);
        data.put("parse", parse);
        JobDetail job = newJob(GrabberJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(time)
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static void main(String[] args) throws Exception {
        Signal.handle(new Signal("INT"),
                signal -> {
                    System.out.println("Interrupted by Ctrl+C, terminate");
                    System.out.println("threads terminated exit");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException();
                    }
                    System.exit(1);
                });
        Properties config = new Properties();
        try (InputStream input = Grabber.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            config.load(input);
        }
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        AppDataBaseService appDataBaseService = new AppDataBaseService(config);
        SiteParser parse = new SiteParser(new SiteAppDateTimeParser(), config);
        int time = Integer.parseInt(config.getProperty("interval"));
        int port = Integer.parseInt(config.getProperty("port"));
        WebViewer webViewer = new WebViewer(appDataBaseService, port);
        new Grabber(parse, appDataBaseService, scheduler, time).init();
        webViewer.web();
    }
}