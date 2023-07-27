package com.example.knu.dto.board.request.sort;

public enum BoardUnifiedPostsSort {

    UPDATE_AT_DESC("updatedAt", "DESC")
    ;

    private String columnName;
    private String direction;

    BoardUnifiedPostsSort(String columnName, String direction) {
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
