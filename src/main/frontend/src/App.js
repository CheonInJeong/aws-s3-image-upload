import "./App.css";
import React, { useState, useEffect, useCallback } from "react";
import { useDropzone } from "react-dropzone";
import axios from "axios";

const UserProfiles = () => {
  const [userProfiles, setUserProfiles] = useState([]);
  const fetchUserProfiles = async () => {
    const users = await axios.get("http://localhost:8900/api/v1/user-profile");
    for (const userProfile of users.data) {
      const { userProfileId } = userProfile;
      const url = await axios.get(
        `http://localhost:8900/api/v1/user-profile/${userProfileId}/image/download`
      );
      userProfile.userProfileImageLink = url.data;
    }
    setUserProfiles(users.data);
  };

  useEffect(() => fetchUserProfiles, []);

  return (
    <div>
      {userProfiles.map((userProfile, index) => (
        <div key={index}>
          {userProfile.userProfileImageLink ? (
            <img src={userProfile.userProfileImageLink} alt="user profile" />
          ) : null}
          <h1>{userProfile.username}</h1>
          <p>{userProfile.userProfileId}</p>
          <Dropzone {...userProfile} />{" "}
          {/*it's same to userProfileId={userProfile.userProfileId} */}
          <br />
          <br />
        </div>
      ))}
    </div>
  );
};
function App() {
  return (
    <div className="App">
      <UserProfiles />
    </div>
  );
}

function Dropzone({ userProfileId }) {
  const onDrop = useCallback((acceptedFiles) => {
    const file = acceptedFiles[0];
    console.log(file);
    const formData = new FormData();
    formData.append("file", file);

    axios
      .post(
        `http://localhost:8900/api/v1/user-profile/${userProfileId}/image/upload`,
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      )
      .then(() => {
        console.log("file uploaded successfully");
      })
      .catch((e) => {
        console.log("there are error uploading file");
      });
  }, []);
  const { getRootProps, getInputProps, isDragActive } = useDropzone({ onDrop });

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {isDragActive ? (
        <p>Drop the image here ...</p>
      ) : (
        <p>
          Drag 'n' drop some profile image, or click to select profile image
        </p>
      )}
    </div>
  );
}

export default App;
