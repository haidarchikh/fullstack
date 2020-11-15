package se.kry.codetest;

import java.util.Date;

public class Service {
    private String name;
    // TODO: change this to some sort or url obj
    private String url;
    private String status;
    private Date createdAt;
    private Date lastPoll;

    // FIXME: lastPoll to null
    // FIXME: move default values to setters

    public Service (String name, String url) { this(name, url, "NOT_POLLED", new Date(), new Date());  }

    public Service (String name, String url, String status, Date createdAt, Date lastPoll) {
        this.setName(name);
        this.setUrl(url);
        this.setStatus(status);
        this.setCreatedAt(createdAt);
        this.setLastPoll(lastPoll);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastPoll() {
        return lastPoll;
    }

    public void setLastPoll(Date lastPoll) {
        this.lastPoll = lastPoll;
    }
}
