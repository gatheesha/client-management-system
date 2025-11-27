package com.gatarita.games.clientmanagementsystem.models;

import com.gatarita.games.clientmanagementsystem.interfaces.Exportable;
import com.gatarita.games.clientmanagementsystem.interfaces.Searchable;

/**
 * Client class demonstrating OOP principles:
 * - Inheritance: extends Person
 * - Polymorphism: implements Searchable and Exportable
 * - Encapsulation: private fields with getters/setters
 */
public class Client extends Person implements Searchable, Exportable {
    private String company;
    private String jobTitle;
    private String notes;

    public Client(String name, String company, String jobTitle, String email, String mobile, String notes) {
        super(name, email, mobile);
        this.company = company;
        this.jobTitle = jobTitle;
        this.notes = notes;
    }

    @Override
    public String getDisplayInfo() {
        return name + " (" + company + " - " + jobTitle + ")";
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String lowerQuery = query.toLowerCase();
        return (name != null && name.toLowerCase().contains(lowerQuery)) ||
                (company != null && company.toLowerCase().contains(lowerQuery)) ||
                (email != null && email.toLowerCase().contains(lowerQuery)) ||
                (jobTitle != null && jobTitle.toLowerCase().contains(lowerQuery));
    }

    @Override
    public String toCSV() {
        return String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"",
                id, name, company, jobTitle, email, mobile, notes);
    }

    @Override
    public String toJSON() {
        return String.format("{\"id\":%d,\"name\":\"%s\",\"company\":\"%s\",\"jobTitle\":\"%s\"}",
                id, name, company, jobTitle);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}