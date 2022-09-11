package com.example.awsimageupload.repository;

import com.example.awsimageupload.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserProfile, Long> {
}
