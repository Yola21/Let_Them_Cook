import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../Authentication/authSlice";
import adminReducer from "../Admin/adminSlice";

export default configureStore({
  reducer: {
    admin: adminReducer,
    auth: authReducer,
  },
});
