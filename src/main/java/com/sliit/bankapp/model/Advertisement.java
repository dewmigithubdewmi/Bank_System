package com.sliit.bankapp.model;

public class Advertisement {
    private int id, adminId;
    private String title, rules, description;

    /* getters & setters */
    public int getId()              { return id; }
    public void setId(int id)       { this.id = id; }

    public int getAdminId()         { return adminId; }
    public void setAdminId(int v)   { this.adminId = v; }

    public String getTitle()        { return title; }
    public void setTitle(String v)  { this.title = v; }

    public String getRules()        { return rules; }
    public void setRules(String v)  { this.rules = v; }

    public String getDescription()      { return description; }
    public void setDescription(String v){ this.description = v; }
}
