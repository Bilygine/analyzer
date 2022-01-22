package com.bilygine.analyzer.analyze.result;

import java.util.LinkedList;
import java.util.List;

public class ResultColumn<T> {

    private String name;
    private List<T> values = new LinkedList<>();

    /**
     *
     * @param
     */
    public ResultColumn(String name) {
        this.name = name;
    }

    /**
     * @return name of column
     */
    public String getName() {
        return this.name;
    }

    /**
     * add value in list
     * @param value
     */
    public void add(T value) {
        this.values.add(value);
    }

    /**
     * add value at index in list
     * @param index
     * @param value
     */
    public void add(int index, T value) {
        this.values.add(index, value);
    }

    /**
     * Gets all values
     * @return values
     */
    public List<T> values() {
        return this.values;
    }

    /**
     * Get value at index
     * @return value at Index or null
     */
    public T valueAt(int index) {
        return (index >= this.values.size()) ? null : this.values.get(index);
    }

    /**
     * Gets size
     */
    public int size() {
        return this.values.size();
    }
}
