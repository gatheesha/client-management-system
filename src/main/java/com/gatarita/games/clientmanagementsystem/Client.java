package com.gatarita.games.clientmanagementsystem;
/* client class, lots of privet variables, getters and setters for them
 */
import java.io.Serializable;

public class Client implements Serializable {
    /*because of 'implements Serializable' part obojects of client class can  be Serialized
    (Serializable is an interface which make an object into byte stream so it can be stored or sent.)
    */
    private int id;
    private String name;
    private String company;
    private String jobTitle;
    private String email;
    private String mobile;
    private String notes;

    //constructor of client class
    public Client(String name, String company, String jobTitle, String email, String mobile, String notes) {
        this.name = name;
        this.company = company;
        this.jobTitle = jobTitle;
        this.email = email;
        this.mobile = mobile;
        this.notes = notes;
    }
//getters and setters as the variables are private and can be accessed within the class only
    public int getId() {return id;}

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
    //toString() method used when "object" is printed. return type string
    //will return name company name(if there is a company name mentioned)
    @Override
    public String toString() {
        return name + (company != null && !company.isEmpty() ? " - " + company : "");
    }
}

/*
* warnings: method not used in the project
* fix: can be deleted
* */