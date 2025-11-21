package com.gatarita.games.clientmanagementsystem;

import java.io.Serializable;
import java.time.LocalDate;

public class Project implements Serializable {
    public enum Status { PENDING, ONGOING, COMPLETED, CANCELLED }

    private int id;
    private String name;
    private LocalDate dueDate;
    private LocalDate startedOn;
    private Status status;
    private double cost;
    private String notes;
    private int clientId;
    private static int idCounter = 1;

    public Project(String name, LocalDate dueDate, LocalDate startedOn, Status status, double cost, String notes, int clientId) {
        this.id = idCounter++;
        this.name = name;
        this.dueDate = dueDate;
        this.startedOn = startedOn;
        this.status = status;
        this.cost = cost;
        this.notes = notes;
        this.clientId = clientId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDueDate() { return dueDate; }
    public LocalDate getStartedOn() { return startedOn; }
    public Status getStatus() { return status; }
    public double getCost() { return cost; }
    public String getNotes() { return notes; }
    public int getClientId() { return clientId; }

    public void setId(int id) {
        this.id = id;
    }
    public void setName(String name) { this.name = name; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setStartedOn(LocalDate startedOn) { this.startedOn = startedOn; }
    public void setStatus(Status status) { this.status = status; }
    public void setCost(double cost) { this.cost = cost; }
    public void setNotes(String notes) { this.notes = notes; }
}