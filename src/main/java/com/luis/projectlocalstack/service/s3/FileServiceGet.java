package com.luis.projectlocalstack.service.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.luis.projectlocalstack.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileServiceGet {

    private final AmazonS3 amazonS3;

    public boolean doesObjectExist(String nameBucket, String keyName){
        return amazonS3.doesObjectExist(nameBucket, keyName);
    }

    public boolean doesBucketExist(String nameBucket){
        return amazonS3.doesBucketExistV2(nameBucket);
    }

    public List<String> listObjects(String bucketName) {
       return amazonS3.listObjects(new ListObjectsRequest()
                .withBucketName(bucketName))
                .getObjectSummaries().stream()
                .map(S3ObjectSummary::getBucketName)
                .collect(Collectors.toList());
    }

    public void getObjectS3(String nameBucket, String keyName){
        S3Object object = amazonS3.getObject(nameBucket, keyName);
        try(S3ObjectInputStream objectContent = object.getObjectContent();
            FileOutputStream fos = new FileOutputStream(new File(keyName))) {

            byte[] bytes = new byte[1024];
            int readLen = 0;
            while ((readLen = objectContent.read(bytes)) > 0) {
                fos.write(bytes, 0, readLen);
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    public List<Bucket> listBuckets(){
       return amazonS3.listBuckets();
    }

}
