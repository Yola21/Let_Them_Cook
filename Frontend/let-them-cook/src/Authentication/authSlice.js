import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

export const createUser = createAsyncThunk(
  'auth/createUser',
  async (args) => {
    const data = {
      "name": args.name,
      "email": args.email,
      "password": args.password,
      "role": args.role
    };

    const response = await fetch('http://localhost:8080/register', {
      method: 'POST',
      headers: {
        "Content-Type": 'application/json',
      },
      body: JSON.stringify(data),
    });
    const userInfo = await response.json();
    return userInfo;
  }
);

export const userLogin = createAsyncThunk(
  'auth/userLogin',
  async (args) => {
    const data = {
      "email": args.email,
      "password": args.password,
    };

    const response = await fetch('http://localhost:8080/login', {
      method: 'POST',
      headers: {
        "Content-Type": 'application/json',
      },
      body: JSON.stringify(data),
    });
    const userInfo = await response.json();
    return userInfo;
  }
);

export const authSlice = createSlice({
  name: 'auth',
  initialState: {
    currentUserInfo: {},
    currentUserRole: null,
    userToken: null,
  },
  reducers: {},
  extraReducers: {
    //Create User
    [createUser.pending]: (state, action) => {
      console.log('Create User Request Pending');
    },
    [createUser.fulfilled]: (state, action) => {
      console.log('Create User Request Fulfilled');
    },
    [createUser.rejected]: (state, action) => {
      console.log(action.payload);
    },

    //User Login
    [userLogin.pending]: (state, action) => {
      console.log('Create User Request Pending');
    },
    [userLogin.fulfilled]: (state, action) => {
      state.userToken = action.payload.token;
      state.currentUserInfo = action.payload.userInfo;
      state.currentUserRole = action.payload.userInfo.role;
    },
    [userLogin.rejected]: (state, action) => {
      console.log(action.payload);
    }
  }
});

export const getCurrentUserInfo = (state) => state.auth.currentUserInfo;
export const getCurrentUserRole = (state) => state.auth.currentUserRole;

export default authSlice.reducer;