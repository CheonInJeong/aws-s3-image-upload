package com.example.awsimageupload.profile;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name="USER")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userProfileId;
    private String username;
    private String userProfileImageLink; //S3 key

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(userProfileId,that.userProfileId)
                && Objects.equals(username,that.username)
                &&  Objects.equals(userProfileImageLink,that.userProfileImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userProfileId, username, userProfileImageLink);
    }

    public Long getUserProfileId() {
        return userProfileId;
    }


    public String getUsername() {
        return username;
    }


    public Optional<String> getUserProfileImageLink() {
        return Optional.ofNullable(userProfileImageLink);
    }

    public void setUserProfileImageLink(String userProfileImageLink) {
        this.userProfileImageLink = userProfileImageLink;
    }
}
