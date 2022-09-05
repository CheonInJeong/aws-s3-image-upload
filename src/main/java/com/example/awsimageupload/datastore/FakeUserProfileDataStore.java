package com.example.awsimageupload.datastore;

import com.example.awsimageupload.profile.UserProfile;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {
    private static final List<UserProfile> USER_PROFILES = new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "ij", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "yj", null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(), "dn", null));
    }

    public List<UserProfile> getUserProfiles() {
        return USER_PROFILES;
    }
}
