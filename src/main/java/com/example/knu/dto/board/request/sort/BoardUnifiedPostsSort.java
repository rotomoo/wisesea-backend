package com.example.knu.dto.board.request.sort;

public enum BoardUnifiedPostsSort {

    BOARD_POST_ID_DESC("id", "DESC")
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
