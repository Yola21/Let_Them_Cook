import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { currentUserToken } from "../Authentication/authSlice";
import { config } from "../config";

export const fetchCooks = createAsyncThunk(
  "admin/fetchCooks",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const token = currentUserToken(state);

    const response = await fetch(`${config.BASE_PATH}${config.COOKS}`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
    const cooks = await response.json();
    return cooks;
  }
);

export const adminSlice = createSlice({
  name: "admin",
  initialState: {
    cooks: null,
    pendingCooks: null,
  },
  reducers: {},
  extraReducers: {
    //Fetch Cooks
    // [fetchCooks.pending]: (state, action) => {
    //   console.log("Fetch Cooks Request Pending");
    // },
    [fetchCooks.fulfilled]: (state, action) => {
      state.cooks = action.payload;
      state.pendingCooks = action.payload.filter(
        (cook) => cook.status === "pending"
      );
    },
    [fetchCooks.rejected]: (state, action) => {
      console.log(action.payload);
    },
  },
});

export const getAllCooks = (state) => state.admin.cooks;
export const getPendingCooks = (state) => state.admin.pendingCooks;

export default adminSlice.reducer;
