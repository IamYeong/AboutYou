package com.iamyeong.aboutyou.dto;

public class Memo implements Comparable<Memo> {

    private String title;
    private String content;
    private long date;
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int compareTo(Memo o) {

        if (o.getDate() > this.getDate()) {
            return 1;
        } else if (o.getDate() < this.getDate()) {
            return -1;
        } else {
            return 0;
        }

    }
}
