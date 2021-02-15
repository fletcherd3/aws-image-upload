package com.anzintern.awsimageupload.filestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class FileStore {

	private final AmazonS3 s3;

	@Autowired
	public FileStore(AmazonS3 s3) {
		this.s3 = s3;
	}

	/**
	 * Download the selecter image from AWS S3 bucket
	 * @param path Path to the users bucket (format: {bucketName}/{userProfileId})
	 * @param key Name of the specific image in path
	 * @return image in byte array format
	 */
	public byte[] download(String path, String key) {
		try {
			S3Object object = s3.getObject(path, key);
			S3ObjectInputStream inputStream = object.getObjectContent();
			return IOUtils.toByteArray(inputStream);
		}catch (AmazonServiceException | IOException e) {
			throw new IllegalStateException("Failed to download image from s3 bucket", e);
		}
	}

	public void save(String path,
			String fileName,
			Optional<Map<String, String>> optionalMetadata,
			InputStream inputStream) {

		ObjectMetadata metadata = new ObjectMetadata();

		optionalMetadata.ifPresent(map -> {
			if (!map.isEmpty()) {
				map.forEach(metadata::addUserMetadata);
			}
		});

		try {
			s3.putObject(path, fileName, inputStream, metadata);
		} catch (AmazonServiceException e) {
			throw new IllegalStateException("Failed to save image stream to s3 bucket", e);
		}
	}

}
