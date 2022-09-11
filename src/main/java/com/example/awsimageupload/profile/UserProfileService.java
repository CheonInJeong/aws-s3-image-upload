package com.example.awsimageupload.profile;

import com.amazonaws.services.backupstorage.model.IllegalArgumentException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sagemakeredgemanager.AmazonSagemakerEdgeManager;
import com.example.awsimageupload.bucket.BucketName;
import com.example.awsimageupload.filestore.FileStore;
import com.example.awsimageupload.repository.UserRepository;
import org.apache.catalina.User;
import org.apache.http.ContentTooLongException;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {
    private final UserRepository userRepository;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserRepository userRepository,
                              FileStore fileStore) {
        //this.userProfileDataAccessService = userProfileDataAccessService;
        this.userRepository = userRepository;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles() {
        return userRepository.findAll();
    }

    public void uploadUserProfileImage(Long userProfileId, MultipartFile file) {
        //1. check if image is not empty
        if (file.isEmpty()) {
            throw new IllegalStateException("can't not upload empty file");
        }
        //2. if file is an image
        List<String> imageTypes = Arrays.asList(ContentType.IMAGE_GIF.getMimeType(),ContentType.IMAGE_PNG.getMimeType(), ContentType.IMAGE_JPEG.getMimeType());
        if (!imageTypes.contains(file.getContentType())) {
            throw new IllegalStateException("file must be an image");
        }
        //3. the user exist in our db
        UserProfile user = userRepository.findAll().stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(()-> new IllegalStateException(String.format("User profile %s not found", userProfileId)));

        //4. grap some metadata from file if any
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        //5. store the image in s3 and update db with s3 image link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String fileName = String.format("%s-%s", UUID.randomUUID(),file.getOriginalFilename());

        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(fileName);
            userRepository.save(user);
            //user.setUserProfileImageLink(fileName);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }

    public String downloadUserProfileImage(Long userProfileId) {

        UserProfile user = getUserProfiles().stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(()->new IllegalStateException("no user profile"));
        String path = String.format("%s/%s",BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        return user.getUserProfileImageLink().map(key-> fileStore.download(path, key)).orElse("");

    }

    public UserProfile saveUserProfile(UserProfile userProfile) {
        return userRepository.save(userProfile);
    }
}
