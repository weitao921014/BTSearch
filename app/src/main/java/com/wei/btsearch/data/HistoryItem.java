package com.wei.btsearch.data;

/**
 * Created by wei on 17-3-31.
 */
public class HistoryItem {
    int id;
    String content;

    public HistoryItem(int id, String content) {
        this.id = id;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "HistoryItem{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
