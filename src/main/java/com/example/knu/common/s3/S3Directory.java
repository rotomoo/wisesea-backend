package com.example.knu.common.s3;

public enum S3Directory {

    BOARD("boards/"),
    FILES("/files/"),
    USER_PROFILE("user/profile/")
    ;

    String path;

    S3Directory(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
