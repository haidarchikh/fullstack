package se.kry.codetest;
import io.vertx.core.json.JsonObject;

import java.util.Date;

public class Service {
    private Integer id;
    private String name;
    // TODO: change this to some sort or url obj
    private String url;
    private String status;
    private Date createdAt;
    private Date lastPoll;

    // FIXME: lastPoll to null
    // FIXME: move default values to setters

    public Service (JsonObject row) {
        this (row.getInteger("id"), row.getString("name"), row.getString("url"), row.getString("status"), new Date(row.getString("createdAt")), new Date(row.getString("lastPoll")));
    }

    public Service (String name, String url) { this(null, name, url, null, null, null);  }

    public Service (Integer id, String name, String url, String status, Date createdAt, Date lastPoll) {
        this.setId(id);
        this.setName(name);
        this.setUrl(url);
        this.setStatus(status);
        this.setCreatedAt(createdAt);
        this.setLastPoll(lastPoll);
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // TODO: throw error if name is empty
//        if (name == null || name.isEmpty()) { throw new Exception("Service name is empty"); }
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        // TODO: throw error if url is empty
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

    public JsonObject getJSON() {
        return new JsonObject()
                .put("id", this.getId())
                .put("name", this.getName())
                .put("url", this.getUrl())
                .put("status", this.getStatus())
                .put("createdAt", this.getCreatedAt())
                .put("lastPoll", this.getLastPoll());
    }
}
