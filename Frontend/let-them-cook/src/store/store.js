import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../Authentication/authSlice";
import adminReducer from "../Admin/adminSlice";
import cookReducer from "../Cook/cookSlice";

export default configureStore({
  reducer: {
    admin: adminReducer,
    auth: authReducer,
    cook: cookReducer
  },
});
