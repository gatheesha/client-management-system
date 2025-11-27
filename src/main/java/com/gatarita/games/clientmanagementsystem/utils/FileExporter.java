package com.gatarita.games.clientmanagementsystem.utils;

import com.gatarita.games.clientmanagementsystem.interfaces.Exportable;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for exporting data to files.
 * Demonstrates File Handling in Java.
 */
public class FileExporter {

    /**
     * Exports a list of Exportable objects to CSV format.
     *
     * @param items List of items to export
     * @param filename Output filename
     * @throws IOException if file writing fails
     */
    public static void exportToCSV(List<? extends Exportable> items, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Exportable item : items) {
                writer.write(item.toCSV());
                writer.newLine();
            }
        }
    }

    /**
     * Exports a list of Exportable objects to JSON format.
     *
     * @param items List of items to export
     * @param filename Output filename
     * @throws IOException if file writing fails
     */
    public static void exportToJSON(List<? extends Exportable> items, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("[\n");
            for (int i = 0; i < items.size(); i++) {
                writer.write("  " + items.get(i).toJSON());
                if (i < items.size() - 1) {
                    writer.write(",");
                }
                writer.newLine();
            }
            writer.write("]");
        }
    }
}