package com.anzintern.awsimageupload.profile;

import java.util.UUID;
import lombok.Data;

@Data
public class UserProfile {

	private final UUID userProfileId;
	private final String userName;
	private String userProfileImageLink;  // S3 key

	public UserProfile(UUID userProfileId,
			String userName,
			String userProfileImageLink) {
		this.userProfileId = userProfileId;
		this.userName = userName;
		this.userProfileImageLink = userProfileImageLink;
	}
}
