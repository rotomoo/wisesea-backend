package com.example.knu.common.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFileToS3(MultipartFile multipartFile, String filePath) throws IOException {

        UUID uuid = UUID.randomUUID();
        filePath += uuid;

        ObjectMetadata objectMetaData = new ObjectMetadata();
        objectMetaData.setContentType(multipartFile.getContentType());
        objectMetaData.setContentLength(multipartFile.getSize());

        amazonS3Client.putObject(bucket, filePath, multipartFile.getInputStream(), objectMetaData);
        String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + filePath;
        return fileUrl;
    }
}
