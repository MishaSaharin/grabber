package org.saccharine.grabber.service;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.saccharine.grabber.db.AppDataBaseService;
import org.saccharine.grabber.db.Post;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class GrabberJob implements Job {
    public static final Logger LOGGER = Logger.getLogger(GrabberJob.class);
    private final Properties config;

    public GrabberJob() {
        this.config = new Properties();
        try (InputStream inputStream = GrabberJob.class.getClassLoader().getResourceAsStream("application.properties")) {
            config.load(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void execute(JobExecutionContext context) {
        String sourceLink = config.getProperty("sourceLink");
        JobDataMap map = context.getJobDetail().getJobDataMap();
        AppDataBaseService appDataBase = (AppDataBaseService) map.get("appDataBase");
        SiteParser parse = (SiteParser) map.get("parse");
        try {
            List<Post> posts = parse.list(sourceLink);
            for (Post post : posts) {
                appDataBase.save(post);
                LOGGER.info("Post saved with id " + post.getId());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}