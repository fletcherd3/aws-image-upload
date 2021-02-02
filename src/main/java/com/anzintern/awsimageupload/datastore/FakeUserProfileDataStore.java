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
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "fletcher_dick", null));
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "josh_gomez", null));
		USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "vasili_aliashkevich", null));
	}

	public List<UserProfile> getUserProfiles() {
		return USER_PROFILES;
	}
}
