package com.gatarita.games.clientmanagementsystem.interfaces;

/**
 * Interface for objects that can be exported to different file formats.
 * Demonstrates polymorphism by defining a contract for export functionality.
 */
public interface Exportable {
    /**
     * Converts this object to CSV format.
     *
     * @return CSV representation of the object
     */
    String toCSV();

    /**
     * Converts this object to JSON format.
     *
     * @return JSON representation of the object
     */
    String toJSON();
}