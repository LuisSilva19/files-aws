package com.luis.projectlocalstack.service;

import com.luis.projectlocalstack.domain.Attachment;
import com.luis.projectlocalstack.service.s3.FileServiceGet;
import com.luis.projectlocalstack.service.s3.FileServicePost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FileServicePostTest {

    @Autowired
    private FileServicePost fileServicePost;

    @Autowired
    private FileServiceGet fileServiceGet;

    @Test
    void createBucket(){
        var bucketFurious = fileServicePost.createBucket("bucketdestroy");
        var result = fileServiceGet.doesBucketExist("bucketdestroy");

        assertTrue(result);
    }

}
