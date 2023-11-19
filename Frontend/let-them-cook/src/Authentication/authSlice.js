import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { config } from "../config";
import { toast } from "react-toastify";

export const createUser = createAsyncThunk(
  "auth/createUser",
  async (args, thunkApi) => {
    const data = {
      name: args.name,
      email: args.email,
      password: args.password,
      role: args.role,
    };

    // const endpoint = `${config.BASE_PATH}${config.USER_REGISTER}`;
    const response = await fetch(
      `${config.BASE_PATH}/register`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      }
    );
    const userInfo = await response.json();

    if (userInfo != null && userInfo.role) {
      if (userInfo.role === "cook") {
        thunkApi.dispatch(
          createCookProfile({ id: userInfo.id, history: args.history })
        );
      } else {
        args.history.push("/login");
      }
    }

    return userInfo;
  }
);

export const userLogin = createAsyncThunk("auth/userLogin", async (args) => {
  const data = {
    email: args.email,
    password: args.password,
  };

  // const endpoint = `${config.BASE_PATH}${config.USER_LOGIN}`;
  const response = await fetch(
    `${config.BASE_PATH}/login`,
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    }
  );
  const userInfo = await response.json();
  return userInfo;
});

export const createCookProfile = createAsyncThunk(
  "auth/createCookProfile",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const businessName = getCookBusinessName(state);
    const address = getCookBusinessAddress(state);
    const profilePicture = getCookProfilePicture(state);
    const businessDocument = getCookBusinessDocument(state);
    const bannerImage = getCookBannerImage(state);

    const formData = new FormData();
    formData.append("userId", args.id);
    formData.append("businessName", businessName);
    formData.append("businessDocument", businessDocument[0]);
    formData.append("address", address);
    formData.append("profilePhoto", profilePicture[0]);
    formData.append("bannerImage", bannerImage[0]);

    // const endpoint = `${config.BASE_PATH}${config.COOKS}${config.COOK_CREATE_PROFILE}`;
    const response = await fetch(
      `${config.BASE_PATH}/cooks/createProfile`,
      {
        method: "POST",
        body: formData,
      }
    );
    const cookInfo = await response.json();
    args.history.push("/login");
    return cookInfo;
  }
);

export const fetchCookById = createAsyncThunk(
  "auth/fetchCookById",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const token = currentUserToken(state);

    const response = await fetch(
      `${config.BASE_PATH}${config.COOKS}/${args.id}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    const cookInfo = await response.json();
    return cookInfo;
  }
);

export const fetchCookProfilePhotoById = createAsyncThunk(
  "auth/fetchCookProfilePhotoById",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const token = currentUserToken(state);

    const response = await fetch(
      `${config.BASE_PATH}${config.COOKS}/profilephoto/${args.id}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    const t = await response.arrayBuffer();
    const t1 = new Blob([t], { type: "image/png" });
    const t3 = URL.createObjectURL(t1);
    return t3;
  }
);

export const fetchCookBannerPhotoById = createAsyncThunk(
  "auth/fetchCookBannerPhotoById",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const token = currentUserToken(state);

    const response = await fetch(
      `${config.BASE_PATH}${config.COOKS}/bannerimage/${args.id}`,
      {
        headers: {
          Authorization: `Bearer ${args.token}`,
        },
      }
    );
    const t = await response.arrayBuffer();
    const t1 = new Blob([t], { type: "image/png" });
    const t3 = URL.createObjectURL(t1);
    return t3;
  }
);

export const updateCookProfile = createAsyncThunk(
  "auth/updateCookProfile",
  async (args, thunkApi) => {
    const response = await fetch(
      `${config.BASE_PATH}${config.COOKS}/updateProfile`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${args.token}`,
        },
        body: args.formData,
      }
    );
    const cookInfo = await response.json();
    // thunkApi.dispatch(
    //   fetchCookBannerPhotoById({ id: args.id, token: args.token })
    // );
    // thunkApi.dispatch(
    //   fetchCookProfilePhotoById({ id: args.id, token: args.token })
    // );
    args.history.push("/cooks");
    return cookInfo;
  }
);

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    currentUserInfo: {},
    currentUserRole: null,
    userToken: null,
    cookBusinessName: null,
    cookBusinessAddress: null,
    cookProfilePicture: null,
    cookBannerImage: null,
    cookBusinessDocument: null,
    cookInfo: {},
    photo1: null,
    photo2: null,
  },
  reducers: {
    setCookBusinessName(state, action) {
      state.cookBusinessName = action.payload;
    },
    setCookBusinessAddress(state, action) {
      state.cookBusinessAddress = action.payload;
    },
    setCookProfilePicture(state, action) {
      state.cookProfilePicture = action.payload;
    },
    setCookBannerImage(state, action) {
      state.cookBannerImage = action.payload;
    },
    setCookBusinessDocument(state, action) {
      state.cookBusinessDocument = action.payload;
    },
  },
  extraReducers: {
    //Create User
    [createUser.fulfilled]: (state, action) => {
      if (action.payload.role) {
        action.payload.role !== "cook" &&
          toast.success("USER CREATED SUCCESSFULLY!");
      } else {
        toast.error(action.payload.message.toUpperCase() + "!");
      }
    },
    [createUser.rejected]: (state, action) => {
      console.log(action.payload.message);
    },

    //User Login
    [userLogin.fulfilled]: (state, action) => {
      localStorage.setItem("token", action.payload.token);
      state.userToken = action.payload.token;
      if (action.payload.userInfo) {
        state.currentUserInfo = action.payload.userInfo;
        state.currentUserRole = action.payload.userInfo.role;
        toast.success("USER LOGGED IN SUCCESSFULLY!");
      } else {
        toast.error(action.payload.message.toUpperCase() + "!");
      }
    },
    [userLogin.rejected]: (state, action) => {
      console.log(action.payload);
    },

    //Create Cook Profile
    [createCookProfile.fulfilled]: (state, action) => {
      console.log("Cook profile created", action.payload);
      toast.success("COOK REGISTERED SUCCESSFULLY!");
    },
    [createCookProfile.rejected]: (state, action) => {
      console.log(action.payload);
    },

    //Fetch Cook By id
    [fetchCookById.fulfilled]: (state, action) => {
      console.log(action.payload);
      state.cookInfo = action.payload;
      state.cookBusinessAddress = action.payload.address;
    },
    [fetchCookById.rejected]: (state, action) => {
      console.log(action.payload);
    },

    //fetchprofile profilePhoto
    [fetchCookProfilePhotoById.fulfilled]: (state, action) => {
      state.photo1 = action.payload;
    },
    [fetchCookProfilePhotoById.rejected]: (state, action) => {
      console.log(action.payload);
    },

    //fetchprofile bannerPhoto
    [fetchCookBannerPhotoById.fulfilled]: (state, action) => {
      state.photo2 = action.payload;
    },
    [fetchCookBannerPhotoById.rejected]: (state, action) => {
      console.log(action.payload);
    },

    //updateCookProfile
    [updateCookProfile.fulfilled]: (state, action) => {
      if (action.payload.id) {
        state.cookInfo = action.payload;
        toast.success("Profile Updated Suucessfully!");
      } else {
        toast.error(action.payload.message);
      }
    },
    [updateCookProfile.rejected]: (state, action) => {
      console.log(action.payload);
    },
  },
});

export const {
  setCookBusinessName,
  setCookBusinessAddress,
  setCookProfilePicture,
  setCookBusinessDocument,
  setCookBannerImage,
} = authSlice.actions;

export const getCurrentUserInfo = (state) => state.auth.currentUserInfo;
export const getCurrentUserRole = (state) => state.auth.currentUserRole;
export const currentUserToken = (state) => state.auth.userToken;
export const getCookBusinessName = (state) => state.auth.cookBusinessName;
export const getCookBusinessAddress = (state) => state.auth.cookBusinessAddress;
export const getCookBusinessDocument = (state) =>
  state.auth.cookBusinessDocument;
export const getCookProfilePicture = (state) => state.auth.cookProfilePicture;
export const alertType = (state) => state.auth.alertType;
export const alertMessage = (state) => state.auth.alertMessage;
export const cookInfo = (state) => state.auth.cookInfo;
export const photo1 = (state) => state.auth.photo1;
export const photo2 = (state) => state.auth.photo2;
export const getCookBannerImage = (state) => state.auth.cookBannerImage;
export default authSlice.reducer;
