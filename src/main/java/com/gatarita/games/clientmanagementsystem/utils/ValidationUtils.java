package com.gatarita.games.clientmanagementsystem.utils;

import com.gatarita.games.clientmanagementsystem.models.Client;
import com.gatarita.games.clientmanagementsystem.models.Project;

import java.time.LocalDate;

/**
 * Utility class for validating data.
 * Demonstrates exception handling and best practices.
 *
 * @author Your Name
 * @version 1.0
 */
public class ValidationUtils {

    /**
     * Validates an email address format.
     *
     * @param email The email to validate
     * @return true if email is valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Validates a phone number format.
     *
     * @param phone The phone number to validate
     * @return true if phone is valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return phone.matches("^[0-9]{10,15}$");
    }

    /**
     * Validates a client object and returns error message if invalid.
     *
     * @param client The client to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateClient(Client client) {
        if (client == null) {
            return "Client cannot be null";
        }

        if (client.getName() == null || client.getName().trim().isEmpty()) {
            return "Name is required";
        }

        if (client.getName().length() > 100) {
            return "Name must be less than 100 characters";
        }

        if (client.getEmail() != null && !client.getEmail().trim().isEmpty() && !isValidEmail(client.getEmail())) {
            return "Invalid email format";
        }

        if (client.getMobile() == null || client.getMobile().trim().isEmpty()) {
            return "Mobile number is required";
        }

        if (!isValidPhone(client.getMobile())) {
            return "Invalid phone number format (10-15 digits required)";
        }

        return null;
    }

    /**
     * Validates a project object and returns error message if invalid.
     *
     * @param project The project to validate
     * @return Error message if invalid, null if valid
     */
    public static String validateProject(Project project) {
        if (project == null) {
            return "Project cannot be null";
        }

        if (project.getName() == null || project.getName().trim().isEmpty()) {
            return "Project name is required";
        }

        if (project.getName().length() > 200) {
            return "Project name must be less than 200 characters";
        }

        if (project.getCost() < 0) {
            return "Cost cannot be negative";
        }

        if (project.getStartedOn() != null && project.getDueDate() != null) {
            if (project.getStartedOn().isAfter(project.getDueDate())) {
                return "Start date cannot be after due date";
            }
        }

        if (project.getDueDate() != null && project.getDueDate().isBefore(LocalDate.now())) {
            return "Due date should not be in the past for new projects";
        }

        if (project.getClientId() <= 0) {
            return "Valid client must be selected";
        }

        return null;
    }

    /**
     * Sanitizes a string by trimming and removing dangerous characters.
     *
     * @param input The string to sanitize
     * @return Sanitized string
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.trim().replaceAll("[<>\"']", "");
    }
}