import { configureStore } from '@reduxjs/toolkit';
import authReducer from "../Authentication/authSlice";

export default configureStore({
  reducer: {
    auth: authReducer,
  },
})