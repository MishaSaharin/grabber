# Grabber
## Small-scale service
It is on-line links aggregator. Service runs on a schedule specified in app.properties. For example,  it extracts Java-related vacancies from career.habr.com and stores them in a database. Access is via REST API.

### Tech Stack
```
    Java 17
    JUnit 5.8.2
    Quartz Scheduler 2.3.2    
    Log4j 1.2.17
    SLF4J Log4j 1.7.30
    PostgreSQL JDBC Driver 42.3.3
    JSoup 1.12.1
    Liquibase 4.22
    Checkstyle 3.2
    Jacoco 0.8.8   
```
### Install 
Install on computer [JDK](https://jdk.java.net/java-se-ri/17-MR1), [Maven](https://maven.apache.org/), [Git](https://git-scm.com/) and [Docker](https://docs.docker.com/get-docker/).</br>
In your database create schema ***post***
```
CREATE SCHEMA IF NOT EXISTS post;
ALTER SCHEMA post OWNER TO postgres;
```
After that create table ***post***
```
CREATE TABLE IF NOT EXISTS post.post
(
    id          TEXT      NOT NULL UNIQUE PRIMARY KEY,
    title       TEXT      NOT NULL DEFAULT 'undefined',
    link        TEXT      NOT NULL UNIQUE DEFAULT 'undefined',
    description TEXT      NOT NULL DEFAULT 'undefined',
    created     TIMESTAMP NOT NULL DEFAULT now()
);
```
### Start
```
sudo compose docker compose-docker.yaml up
```
### View
```
localhost:9000
```
### For other sites
you should change the values in these fields:
* interval=your number (for example 1000)
* sourceLink=your website address (for example https://career.habr.com)
* prefix=part of your website address (for example/vacancies?page=)
* suffix=part of your website address (for example &q=Java%20developer&type=all)
* pageNumber=your page number count (for example 5)
* description=part of your website's workspace website (for example .vacancy-description__text)
* inner=part of your website's workspace website (for example .vacancy-card__inner)
* title=part of your website's workspace website (for example .vacancy-card__title)
* date=part of your website's workspace website (for example .vacancy-card__date)