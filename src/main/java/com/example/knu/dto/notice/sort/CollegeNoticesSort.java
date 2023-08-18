package com.example.knu.dto.notice.sort;

import org.hibernate.boot.model.source.spi.Sortable;

public enum CollegeNoticesSort {
    VIEW_DESC("view", "DESC"),
//    LIKE_DESC("like", "DESC"),
    DATE_DESC("date", "DESC")
    ;

    private String columnName;
    private String direction;

    CollegeNoticesSort(String columnName, String direction) {
        this.columnName = columnName;
        this.direction = direction;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDirection() {
        return direction;
    }
}
