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

    public Client(String name, String company, String jobTitle, String email, String mobile, String notes) {
        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.email = email;
        this.mobile = mobile;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getNotes() {
        return notes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return name + (company != null && !company.isEmpty() ? " - " + company : "");
    }



}
