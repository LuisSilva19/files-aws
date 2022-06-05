package com.luis.projectlocalstack.service.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.luis.projectlocalstack.domain.Attachment;
import com.luis.projectlocalstack.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileServicePost {

	private final AmazonS3 amazonS3;

	@Value("${bucket.name}")
	private String BUCKET;

	@Value("${bucket.region}")
	private String REGION;

	public Attachment write(MultipartFile file, String keyName) {
		byte[] content = new byte[0];

		try {
			content = file.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return createAttachment(content, file.getOriginalFilename());
	}

	public Attachment createBucket(String bucketName){
		Bucket bucket = null;
		if (amazonS3.doesBucketExistV2(bucketName)) {
			throw new ServiceException(String.format("Bucket %s already exists.", bucketName));
		}
		try {
			bucket = amazonS3.createBucket(createBucketRequest(bucketName, REGION));
			amazonS3.createBucket(bucketName);
		} catch (AmazonS3Exception e) {
			e.getMessage();
		}
		return attachment(bucket.getName());
	}

	private CreateBucketRequest createBucketRequest(String nameBucket, String region) {
		return new CreateBucketRequest(nameBucket, region);
	}

	private Attachment createAttachment(byte[] bytes, String defaultFileName) {
		String contentType = "application/txt";
		upload(request(stream(bytes), BUCKET, defaultFileName, contentType));

		return attachment(defaultFileName, defaultFileName, contentType);
	}

	private ByteArrayOutputStream stream(byte[] bytes) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream(bytes.length);
		try {
			stream.write(bytes);
		} catch (IOException e) {
			throw new ServiceException(e.getMessage());
		}
		return stream;
	}

	private InputStream stream(ByteArrayOutputStream bytes) {
		return new ByteArrayInputStream(bytes.toByteArray());
	}

	private PutObjectRequest request(ByteArrayOutputStream bytes, String keyName, String key, String contentType) {
		return new PutObjectRequest(keyName, key, stream(bytes), metadata(bytes, contentType));
	}

	private void upload(PutObjectRequest request) {
		try {
			amazonS3.putObject(request);
		} catch (AmazonClientException e) {
			throw new ServiceException(e.getMessage());
		}
	}

	private ObjectMetadata metadata(ByteArrayOutputStream bytes, String contentType) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(bytes.size());
		metadata.setContentType(contentType);
		return metadata;
	}

	private Attachment attachment(String bucketNam) {
		return Attachment.builder()
				.bucket(bucketNam)
				.build();
	}

	private Attachment attachment(String fileName, String key, String contentType) {
		return Attachment.builder()
				.key(key)
				.bucket(BUCKET)
				.contentType(contentType)
				.fileName(fileName)
				.build();
	}

}









