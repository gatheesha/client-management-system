package com.gatarita.games.clientmanagementsystem.models;

import java.io.Serializable;

/**
 * Abstract base class representing a person.
 * Demonstrates inheritance and abstraction in OOP.
 * All person-related entities should extend this class.
 *
 * @author Your Name
 * @version 1.0
 */
public abstract class Person implements Serializable {
    protected int id;
    protected String name;
    protected String email;
    protected String mobile;

    /**
     * Constructor for Person.
     *
     * @param name The person's full name
     * @param email The person's email address
     * @param mobile The person's mobile number
     */
    public Person(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    /**
     * Abstract method that must be implemented by subclasses.
     * Returns a display-friendly representation of the person.
     *
     * @return String representation for display purposes
     */
    public abstract String getDisplayInfo();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}