package com.gatarita.games.clientmanagementsystem.interfaces;

/**
 * Interface for objects that can be searched using a query string.
 * Demonstrates polymorphism by allowing different implementations
 * of search functionality across different types.
 */
public interface Searchable {
    /**
     * Checks if this object matches the given search query.
     *
     * @param query The search string to match against
     * @return true if the object matches the query, false otherwise
     */
    boolean matches(String query);
}