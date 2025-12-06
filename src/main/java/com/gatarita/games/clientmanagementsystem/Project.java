package com.gatarita.games.clientmanagementsystem;

import java.io.Serializable;
import java.time.LocalDate;

public class Project implements Serializable {
    public enum Status {
        PENDING("Not Started"),
        ONGOING("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }

        public static Status fromDisplayName(String displayName) {
            if (displayName == null) {
                return PENDING; // Default to PENDING if status is unexpectedly null
            }
            for (Status status : Status.values()) {
                if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                    return status;
                }
            }
            // If the display name is completely unknown, return a safe default
            return PENDING;
        }
    }
    private int id;
    private String name;
    private LocalDate dueDate;
    private LocalDate startedOn;
    private LocalDate completedOn;
    private LocalDate cancelledOn;
    private Status status;
    private double cost;
    private String notes;
    private String cancellationReason;
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getStartedOn() {
        return startedOn;
    }

    public LocalDate getCompletedOn() {
        return completedOn;
    }

    public LocalDate getCancelledOn() {
        return cancelledOn;
    }

    public Status getStatus() {
        return status;
    }

    public double getCost() {
        return cost;
    }

    public String getNotes() {
        return notes;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public int getClientId() {
        return clientId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setStartedOn(LocalDate startedOn) {
        this.startedOn = startedOn;
    }

    public void setCompletedOn(LocalDate completedOn) {
        this.completedOn = completedOn;
    }

    public void setCancelledOn(LocalDate cancelledOn) {
        this.cancelledOn = cancelledOn;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
}
