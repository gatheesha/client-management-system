package com.gatarita.games.clientmanagementsystem;
/* project class, lots of privet variables, getters and setters for them
 have a link to client class
 * */
import java.io.Serializable;
import java.time.LocalDate;// import local date from time API(what is API though??????)
// what if we get null for LocalDate?????

public class Project implements Serializable {

    public enum Status {
        PENDING("Not Started"),
        ONGOING("In Progress"),
        COMPLETED("Completed"),
        CANCELLED("Cancelled");
/*
enum : used to define a fixed list of possible values for something(in this case possible values for "status")
(in this the fixed constants are PENDING,ONGOING,COMPLETED,CANCELLED)
why enum: its make code cleaner,
and we can only use the values defined in the enum so it will reduse errors
*
 so basically, Status is a data type, not a variable.
 we can create variabes from data type status (ex:Status taskStatus;)
 status stores enum type data (ex; Status taskStatus = Status.ONGOING;)
 but can't have value like: Status invalidTask = "Not Started";
 */
        private final String displayName;//private variable inside Status
        // need a getter and setter which declared bellow

        //constructor for Status and store displayName
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

    //constructor for project class
    public Project(String name, LocalDate dueDate, LocalDate startedOn, Status status, double cost, String notes, int clientId) {
        this.name = name;
        this.dueDate = dueDate;
        this.startedOn = startedOn;
        this.status = status;
        this.cost = cost;
        this.notes = notes;
        this.clientId = clientId;
    }
    //getters and setters for project class
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
