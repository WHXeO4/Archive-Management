package main.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public class Document implements Serializable {
    private Integer id;
    private String title;
    private String url;

    private String description;
    private transient LocalDateTime createTime;
    private transient LocalDateTime updateTime;

    public Document() {
    }

    public Document(String title) {
        this.title = title;
    }

    public Document(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Document(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Document(Integer id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public Document(Integer id, String title, String url, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Document(Integer id, String title, String url, String description, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.description = description;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(createTime != null ? createTime.toString() : null);
        out.writeObject(updateTime != null ? updateTime.toString() : null);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        String createTimeStr = (String) in.readObject();
        String updateTimeStr = (String) in.readObject();
        this.createTime = createTimeStr != null ? LocalDateTime.parse(createTimeStr) : null;
        this.updateTime = updateTimeStr != null ? LocalDateTime.parse(updateTimeStr) : null;
    }
}
