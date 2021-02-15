package com.anzintern.awsimageupload.profile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.anzintern.awsimageupload.bucket.BucketName;
import com.anzintern.awsimageupload.filestore.FileStore;

@Service
public class UserProfileService {

	private final UserProfileDataAccessService userProfileDataAccessService;
	private final FileStore fileStore;

	@Autowired
	public UserProfileService(
			UserProfileDataAccessService userProfileDataAccessService,
			FileStore fileStore) {
		this.userProfileDataAccessService = userProfileDataAccessService;
		this.fileStore = fileStore;
	}

	List<UserProfile> getUserProfiles() {
		return userProfileDataAccessService.getUserProfiles();
	}

	public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) throws IOException {
		// Check if image is not empty

		isFileEmpty(file);

		// Check if file is an image
		isImage(file);

		// Check whether user exists
		UserProfile user = getUserProfileOrThrow(userProfileId);

		// Grab some metadata from file if any
		Map<String, String> metadata = extractMetadata(file);

		// Store image in S3 bucket and update database with S3 image link
		storeImageToS3(file, user, metadata);
	}

	public byte[] downloadUserProfileImage(UUID userProfileId) {
		UserProfile user = getUserProfileOrThrow(userProfileId);
		String path = String.format("%s/%s",
				BucketName.PROFILE_IMAGE.getBucketName(),
				user.getUserProfileId());

		String key = user.getUserProfileImageLink();

		if (key != null) {
			return fileStore.download(path, key);
		}
		return new byte[0];
	}

	private void storeImageToS3(MultipartFile file, UserProfile user, Map<String, String> metadata) throws IOException {
		String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
		String filename = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
		fileStore.save(path, filename, Optional.of(metadata), file.getInputStream());
		// Update User Profiles image link
		user.setUserProfileImageLink(filename);
	}

	private Map<String, String> extractMetadata(MultipartFile file) {
		Map<String, String> metadata = new HashMap<>();
		metadata.put("Content-Type", file.getContentType());
		metadata.put("Content-Length", String.valueOf(file.getSize()));
		return metadata;
	}

	private UserProfile getUserProfileOrThrow(UUID userProfileId) {
		return userProfileDataAccessService
				.getUserProfiles()
				.stream()
				.filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException(
						String.format("User profile %s not found", userProfileId))
				);
	}

	private void isImage(MultipartFile file) throws IOException {
		if (ImageIO.read(file.getInputStream()) == null) {
			throw new IllegalStateException("Received file is not an image");
		}
	}

	private void isFileEmpty(MultipartFile file) {
		if (file.isEmpty()) {
			throw new IllegalStateException("Received file is empty");
		}
	}


}
