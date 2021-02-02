package com.anzintern.awsimageupload.profile;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserProfileService {

	private final UserProfileDataAccessService userProfileDataAccessService;

	@Autowired
	public UserProfileService(
			UserProfileDataAccessService userProfileDataAccessService) {
		this.userProfileDataAccessService = userProfileDataAccessService;
	}

	List<UserProfile> getUserProfiles() {
		return userProfileDataAccessService.getUserProfiles();
	}

	public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
		// Check if image is not empty
		// Check if file is an image
		// Check whether user exists
		// Grab some metadata from file if any
		// Store image in S3 bucket and update database with S3 image link
	}
}
