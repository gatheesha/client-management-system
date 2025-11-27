package com.gatarita.games.clientmanagementsystem.models;

import com.gatarita.games.clientmanagementsystem.interfaces.Exportable;
import com.gatarita.games.clientmanagementsystem.interfaces.Searchable;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Project class demonstrating OOP principles:
 * - Polymorphism: implements Searchable and Exportable
 * - Encapsulation: private fields with getters/setters
 */
public class Project implements Serializable, Searchable, Exportable {
    public enum Status { PENDING, ONGOING, COMPLETED, CANCELLED }

    private int id;
    private String name;
    private LocalDate dueDate;
    private LocalDate startedOn;
    private Status status;
    private double cost;
    private String notes;
    private int clientId;

    public Project(String name, LocalDate dueDate, LocalDate startedOn, Status status, double cost, String notes, int clientId) {
        this.name = name;
        this.dueDate = dueDate;
        this.startedOn = startedOn;
        this.status = status;
        this.cost = cost;
        this.notes = notes;
        this.clientId = clientId;
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.trim().isEmpty()) {
            return true;
        }
        String lowerQuery = query.toLowerCase();
        return (name != null && name.toLowerCase().contains(lowerQuery)) ||
                (notes != null && notes.toLowerCase().contains(lowerQuery));
    }

    @Override
    public String toCSV() {
        return String.format("%d,\"%s\",\"%s\",\"%s\",\"%s\",%.2f,%d",
                id, name,
                dueDate != null ? dueDate.toString() : "",
                startedOn != null ? startedOn.toString() : "",
                status, cost, clientId);
    }

    @Override
    public String toJSON() {
        return String.format("{\"id\":%d,\"name\":\"%s\",\"status\":\"%s\",\"cost\":%.2f}",
                id, name, status, cost);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getStartedOn() { return startedOn; }
    public Status getStatus() { return status; }
    public double getCost() { return cost; }
    public String getNotes() { return notes; }
    public int getClientId() { return clientId; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setStartedOn(LocalDate startedOn) { this.startedOn = startedOn; }
    public void setStatus(Status status) { this.status = status; }
    public void setCost(double cost) { this.cost = cost; }
    public void setNotes(String notes) { this.notes = notes; }
}