package com.anzintern.awsimageupload.datastore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.anzintern.awsimageupload.profile.UserProfile;

@Repository
public class FakeUserProfileDataStore {

	private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

	static {
		USER_PROFILES.add(new UserProfile(UUID.nameUUIDFromBytes("fletcher".getBytes()), "fletcher_dick", null));
		USER_PROFILES.add(new UserProfile(UUID.nameUUIDFromBytes("user1".getBytes()), "user_1", null));
		USER_PROFILES.add(new UserProfile(UUID.nameUUIDFromBytes("user2".getBytes()), "user_2", null));
	}

	public List<UserProfile> getUserProfiles() {
		return USER_PROFILES;
	}
}
