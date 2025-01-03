package com.crypto.crypt.model.tiers;

import org.entityframework.tools.Primary;

public class Feedback {
    @Primary(auto = true)
    private int id_feedback;
    private String subject;
    private String content;
    private int id_sender;

    public int getId_sender() {
        return id_sender;
    }
    public void setId_sender(int id_sender) {
        this.id_sender = id_sender;
    }
    public int getId_feedback() {
        return id_feedback;
    }
    public void setId_feedback(int id_feedback) {
        this.id_feedback = id_feedback;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
