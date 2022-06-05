package com.luis.projectlocalstack.service.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.luis.projectlocalstack.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceDelete {

    private final AmazonS3 amazonS3;

    public void deleteObject(String bucketName, String objectKey) {
        try {
            amazonS3.deleteObject(bucketName, objectKey);
        } catch (AmazonServiceException e) {
           throw new ServiceException(e.getMessage());
        }
    }

    public void deleteBucket(String bucketName) {
        try {
            amazonS3.deleteBucket(bucketName);
        } catch (AmazonServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
