package com.bilygine.analyzer.analyze.result;

import java.util.*;

public class Result {

    
    /** All columns from each steps */
    private List<ResultColumn> columns = new ArrayList<>();

    public Result() {}

    public Result(List<ResultColumn> columns) {
        this.columns = columns;
    }

    /**
     * Add few columns to result
     * @param columns
     */
    public void addColumns(List<ResultColumn> columns) {
        if (columns != null) {
            for (ResultColumn column : columns) {
                this.addColumn(column);
            }
        }
    }

    /**
     * Add a column to result
     * @param column
     */
    public void addColumn(ResultColumn column) {
        this.columns.add(column);
    }

    /**
     * @return Gets all columns.
     */
    public List<ResultColumn> getResultColumns() {
        return this.columns;
    }

    /**
     * @return Largest column
     */
    public ResultColumn getHiggestColumn() {
        return this.columns.stream()
                .max(Comparator.comparing(ResultColumn::size))
                .orElse(null);
    }

    public int getMaxColumnSize() {
        ResultColumn higgest = this.getHiggestColumn();
        if (higgest == null)
            return 0;
        return  higgest.size();
    }
}
