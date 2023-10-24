import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { config } from "../config";

export const createUser = createAsyncThunk(
  "auth/createUser",
  async (args, thunkApi) => {
    const data = {
      name: args.name,
      email: args.email,
      password: args.password,
      role: args.role,
    };

    const response = await fetch(`${config.BASE_PATH}${config.USER_REGISTER}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    });
    const userInfo = await response.json();

    if (userInfo != null) {
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

  const response = await fetch(`${config.BASE_PATH}${config.USER_LOGIN}`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });
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

    const formData = new FormData();
    formData.append("userId", args.id);
    formData.append("businessName", businessName);
    formData.append("businessDocument", businessDocument[0]);
    formData.append("address", address);
    formData.append("profilePhoto", profilePicture[0]);

    const response = await fetch(
      `${config.BASE_PATH}${config.COOKS}${config.COOK_CREATE_PROFILE}`,
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

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    currentUserInfo: {},
    currentUserRole: null,
    userToken: null,
    cookBusinessName: null,
    cookBusinessAddress: null,
    cookProfilePicture: null,
    cookBusinessDocument: null,
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
    setCookBusinessDocument(state, action) {
      state.cookBusinessDocument = action.payload;
    },
  },
  extraReducers: {
    //Create User
    // [createUser.pending]: (state, action) => {
    //   console.log("Create User Request Pending");
    // },
    [createUser.fulfilled]: (state, action) => {
      console.log(action.payload);
    },
    [createUser.rejected]: (state, action) => {
      console.log(action.payload.message);
    },

    //User Login
    // [userLogin.pending]: (state, action) => {
    //   console.log("Create User Request Pending");
    // },
    [userLogin.fulfilled]: (state, action) => {
      state.userToken = action.payload.token;
      state.currentUserInfo = action.payload.userInfo;
      state.currentUserRole = action.payload.userInfo.role;
      if (!action.payload.userInfo) {
        state.alertType = "error";
        state.alertMessage = action.payload.message;
      }
      state.alertType = "success";
      state.alertMessage = "User has been logged in successfully!";
    },
    [userLogin.rejected]: (state, action) => {
      console.log(action.payload);
    },

    //Create Cook Profile
    // [createCookProfile.pending]: (state, action) => {
    //   console.log("Create Cook Profile Request Pending");
    // },
    [createCookProfile.fulfilled]: (state, action) => {
      console.log("Cook profile created", action.payload);
      // history.push("/login");
      // state.userToken = action.payload.token;
      // state.currentUserInfo = action.payload.userInfo;
      // state.currentUserRole = action.payload.userInfo.role;
    },
    [createCookProfile.rejected]: (state, action) => {
      console.log(action.payload);
    },
  },
});

export const {
  setCookBusinessName,
  setCookBusinessAddress,
  setCookProfilePicture,
  setCookBusinessDocument,
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

export default authSlice.reducer;
