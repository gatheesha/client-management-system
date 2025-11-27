package com.gatarita.games.clientmanagementsystem.utils;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generic utility class for filtering and sorting collections.
 * Demonstrates Collections and Generics in Java.
 *
 * @param <T> The type of items to filter/sort
 */
public class DataFilter<T> {

    /**
     * Filters a list based on a condition.
     *
     * @param items List of items to filter
     * @param condition Predicate to test each item
     * @return Filtered list
     */
    public List<T> filter(List<T> items, Predicate<T> condition) {
        return items.stream()
                .filter(condition)
                .collect(Collectors.toList());
    }

    /**
     * Sorts a list using a comparator.
     *
     * @param items List of items to sort
     * @param comparator Comparator for sorting
     * @return Sorted list
     */
    public List<T> sort(List<T> items, Comparator<T> comparator) {
        return items.stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    /**
     * Finds the first item matching a condition.
     *
     * @param items List of items to search
     * @param condition Predicate to test each item
     * @return First matching item or null
     */
    public T findFirst(List<T> items, Predicate<T> condition) {
        return items.stream()
                .filter(condition)
                .findFirst()
                .orElse(null);
    }
}