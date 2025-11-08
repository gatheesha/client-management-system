package com.gatarita.games.clientmanagementsystem;
import java.io.Serializable;

public class Client implements Serializable {
    private int id;
    private String name;
    private String company;
    private String jobTitle;
    private String email;
    private String mobile;
    private String notes;
    private static int idCounter = 1;

    public Client(String name, String company, String jobTitle, String email, String mobile, String notes) {
        this.id = idCounter++;
        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.email = email;
        this.mobile = mobile;
        this.notes = notes;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCompany() { return company; }
    public String getJobTitle() { return jobTitle; }
    public String getEmail() { return email; }
    public String getMobile() { return mobile; }
    public String getNotes() { return notes; }

    public void setName(String name) { this.name = name; }
    public void setCompany(String company) { this.company = company; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public void setEmail(String email) { this.email = email; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public void setNotes(String notes) { this.notes = notes; }
}