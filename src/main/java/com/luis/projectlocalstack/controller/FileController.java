package com.luis.projectlocalstack.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.luis.projectlocalstack.domain.Attachment;
import com.luis.projectlocalstack.service.s3.FileServiceDelete;
import com.luis.projectlocalstack.service.s3.FileServiceGet;
import com.luis.projectlocalstack.service.s3.FileServicePost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileServicePost fileServicePost;
    private final FileServiceGet fileServiceGet;
    private final FileServiceDelete fileServiceDelete;

    @PostMapping("bucket/{keyName}")
    public Attachment recordFileS3(@RequestParam("file") MultipartFile file,
                                   @PathVariable("keyName") String keyName){
        return fileServicePost.write(file, keyName);
    }

    @PostMapping("/{nameBucket}")
    public Attachment createBucket(@PathVariable("nameBucket") String nameBucket){
        return fileServicePost.createBucket(nameBucket);
    }

    @GetMapping("/save/{nameBucket}/{keyName}")
    public void saveObjectS3(@PathVariable("nameBucket") String nameBucket,
                           @PathVariable("keyName") String keyName){
        fileServiceGet.doesBucketExist(nameBucket);
        fileServiceGet.doesObjectExist(nameBucket, keyName);
        fileServiceGet.saveObjectS3(nameBucket,keyName);
    }

    @GetMapping("/{nameBucket}/{keyName}")
    public ResponseEntity<byte[]> getObject(@PathVariable("nameBucket") String nameBucket,
                                            @PathVariable("keyName") String keyName){
        var object = fileServiceGet.getObject(nameBucket, keyName);
        return new ResponseEntity<>(object, headers(keyName, MediaType.APPLICATION_OCTET_STREAM),OK);
    }

    @GetMapping()
    public List<Bucket> listBuckets(){
        return fileServiceGet.listBuckets();
    }

    @DeleteMapping("/{nameBucket}/{keyName}")
    public void deleteObject(@PathVariable("nameBucket") String nameBucket,
                             @PathVariable("keyName") String keyName){
        fileServiceDelete.deleteObject(nameBucket, keyName);
    }

    @DeleteMapping("/{nameBucket}")
    public void deleteBucket(@PathVariable("nameBucket")String nameBucket){
        fileServiceDelete.deleteBucket(nameBucket);
    }

    private HttpHeaders headers(String fileName, MediaType type){
        HttpHeaders h = new HttpHeaders();
        h.setContentType(type);
        h.setContentDispositionFormData(fileName, fileName);
        h.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return h;
    }
}
