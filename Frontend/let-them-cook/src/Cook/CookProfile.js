import React, { useEffect, useState } from "react";
import {
  Avatar,
  Box,
  Button,
  Divider,
  Input,
  TextField,
  Typography,
} from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import {
  cookInfo,
  fetchCookBannerPhotoById,
  fetchCookById,
  fetchCookProfilePhotoById,
  getCookBusinessAddress,
  getCurrentUserInfo,
  photo1,
  photo2,
  setCookBusinessAddress,
  updateCookProfile,
} from "../Authentication/authSlice";
import { toast } from "react-toastify";
import { useHistory, useParams } from "react-router-dom/cjs/react-router-dom";

function CookProfile() {
  const userInfo = useSelector(getCurrentUserInfo);
  const cook = useSelector(cookInfo);
  const p1 = useSelector(photo1);
  const p2 = useSelector(photo2);
  const dispatch = useDispatch();
  const [profilePhoto, setProfilePhoto] = useState(null);
  const [bannerImage, setBannerImage] = useState(null);
  const address = useSelector(getCookBusinessAddress);
  const history = useHistory();
  const { id } = useParams();

  const handleUpdateProfile = () => {
    const token = localStorage.getItem("token");
    const formData = new FormData();
    formData.append("id", cook?.id);
    formData.append("address", address);
    bannerImage && formData.append("bannerImage", bannerImage[0]);
    profilePhoto && formData.append("profilePhoto", profilePhoto[0]);
    if (address !== cook?.address || profilePhoto || bannerImage) {
      dispatch(
        updateCookProfile({
          formData,
          token,
          history,
        })
      );
    } else {
      toast.warning("Please update Address or Profile Photo or Banner Image!");
    }
  };

  useEffect(() => {
    const token = localStorage.getItem("token");
    dispatch(fetchCookById({ id, token }));
    dispatch(
      fetchCookProfilePhotoById({
        id,
        token,
      })
    );
    dispatch(
      fetchCookBannerPhotoById({
        id,
        token,
      })
    );
  }, [dispatch, id]);

  return (
    <div>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          position: "relative",
        }}
      >
        <img style={{ width: "100vw", height: "50vh" }} src={p2} alt="Banner" />
        <Avatar
          sx={{ width: "12rem", height: "12rem" }}
          src={p1}
          alt="Profile"
          style={{
            position: "absolute",
            top: "18rem",
            border: "3px solid #fff",
          }}
        />
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            marginLeft: "13rem",
          }}
        >
          <Typography variant="h4">{userInfo?.name}</Typography>
          <Typography variant="h6">{userInfo?.email}</Typography>
        </div>
      </div>
      <div style={{ padding: "0 10rem", marginTop: "5rem" }}>
        <Typography>Business Details</Typography>
      </div>
      <Divider style={{ color: "#000" }} />
      <div
        style={{
          padding: "1rem 10rem",
        }}
      >
        <Box className="register">
          <Typography style={{ marginBottom: "1rem" }}>
            Business Name: {cook.businessName}
          </Typography>
          <TextField
            required
            id="outlined-disabled"
            label="Pickup Address"
            value={address}
            onChange={(e) => dispatch(setCookBusinessAddress(e.target.value))}
            multiline
            sx={{ marginBottom: "1rem" }}
          />
          <Typography>Update Your Profile Photo? </Typography>
          <Input
            value={profilePhoto?.[1]}
            onChange={(e) => setProfilePhoto(e.target.files)}
            type="file"
            disableUnderline="true"
            sx={{ marginBottom: "1rem" }}
          />
          <Typography>Update Your Banner Image? </Typography>
          <Input
            value={bannerImage?.[1]}
            onChange={(e) => setBannerImage(e.target.files)}
            type="file"
            disableUnderline="true"
            sx={{ marginBottom: "1rem" }}
          />

          <Button onClick={handleUpdateProfile}>Update</Button>
        </Box>
      </div>
    </div>
  );
}

export default CookProfile;
