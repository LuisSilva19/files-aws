package com.luis.projectlocalstack.service;

import com.luis.projectlocalstack.service.config.BaseIntegrationTest;
import com.luis.projectlocalstack.service.s3.FileServiceGet;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
class FileServiceGetTest extends BaseIntegrationTest {

    @Autowired
    FileServiceGet fileServiceGet;

    @Test
     void doesBucketExistTest(){
        var result = fileServiceGet.doesBucketExist("Local-bucket");
        var result1 = fileServiceGet.doesBucketExist("Local");

        System.out.println( "is " + result);
        System.out.println( "is " + result1);

        assertTrue(result);
        assertFalse(result1);
    }

    @Test
    void doesObjectExistTest(){
        var result = fileServiceGet.doesObjectExist("Local-bucket", "test-file.txt");
        assertTrue(result);
    }
}