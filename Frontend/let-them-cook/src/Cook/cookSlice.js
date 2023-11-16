import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { config } from "../config";
import { toast } from "react-toastify";

export const createDish = createAsyncThunk(
  "cook/createDish",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const token = localStorage.getItem("token");
    const name = dishName(state);
    const label = dishLabel(state);
    const price = dishPrice(state);
    const image = dishImage(state);

    const formData = new FormData();
    formData.append("name", name);
    formData.append("label", label);
    formData.append("price", price);
    formData.append("image", image[0]);
    formData.append("cookId", args.id);

    const response = await fetch(
      `${config.BASE_PATH}${config.MENU}${config.MENU_CREATE_DISH}`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      }
    );

    const menu = await response.json();
    thunkApi.dispatch(fetchDishesByCook({ cookId: args.id }));
    return menu;
  }
);

export const updateDish = createAsyncThunk(
  "cook/updateDish",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const token = localStorage.getItem("token");
    const name = dishName(state);
    const label = dishLabel(state);
    const price = dishPrice(state);

    const formData = new FormData();
    formData.append("id", args.id);
    formData.append("name", name);
    formData.append("label", label);
    formData.append("price", price);

    const response = await fetch(
      `${config.BASE_PATH}${config.MENU}${config.MENU_UPDATE_DISH}`,
      {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      }
    );

    const menu = await response.json();
    thunkApi.dispatch(fetchDishesByCook({ cookId: args.cookId }));
    return menu;
  }
);

export const deleteDish = createAsyncThunk(
  "cook/deleteDish",
  async (args, thunkApi) => {
    const token = localStorage.getItem("token");

    const response = await fetch(
      `${config.BASE_PATH}${config.MENU}${config.MENU_DELETE_DISH}/${args.id}`,
      {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );

    const menu = await response.json();
    thunkApi.dispatch(fetchDishesByCook({ cookId: args.cookId }));
    return menu;
  }
);

export const fetchDishesByCook = createAsyncThunk(
  "cook/fetchDishesByCook",
  async (args, thunkApi) => {
    const token = localStorage.getItem("token");

    const response = await fetch(
      `${config.BASE_PATH}${config.MENU}${config.COOK}/${args.cookId}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    );
    const menu = await response.json();
    return menu;
  }
);

export const addDishToMeal = createAsyncThunk(
  "cook/addDishToMeal",
  async (args, thunkApi) => {
    const state = thunkApi.getState();
    const token = localStorage.getItem("token");
    const menuId = dishId(state);

    const data = {
      maxOrderLimit: args.mealMaxOrderLimit,
      slot: args.mealSlot,
      orderDeadline: args.mealOrderDeadline,
      mealDate: args.mealDate,
      menuId,
    };

    const response = await fetch(
      `${config.BASE_PATH}${config.MENU}${config.MENU_ADD_DISH_TO_MEAL}`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      }
    );
    const menu = await response.json();
    return menu;
  }
);

export const cookSlice = createSlice({
  name: "cook",
  initialState: {
    dishesByCook: null,
    currentDish: null,
    dishId: null,
    dishName: null,
    dishLabel: null,
    dishPrice: null,
    dishImage: null,
    openCreateDishForm: false,
    openUpdateDishForm: false,
    openAddDishToMealForm: false,
    mealMaxOrderLimit: null,
    mealSlot: null,
    mealDate: null,
    mealOrderDeadline: null,
  },
  reducers: {
    setDishName(state, action) {
      state.dishName = action.payload;
    },
    setDishLabel(state, action) {
      state.dishLabel = action.payload;
    },
    setDishPrice(state, action) {
      state.dishPrice = action.payload;
    },
    setDishImage(state, action) {
      state.dishImage = action.payload;
    },
    toggleCreateDishForm(state, action) {
      state.openCreateDishForm = action.payload;
    },
    toggleUpdateDishForm(state, action) {
      state.openUpdateDishForm = action.payload;
    },
    toggleAddDishToMealForm(state, action) {
      state.openAddDishToMealForm = action.payload;
    },
    setCurrentDish(state, action) {
      state.dishName = action.payload.name;
      state.dishLabel = action.payload.label;
      state.dishPrice = action.payload.price;
      state.dishId = action.payload.id;
    },
    setMealMaxOrderLimit(state, action) {
      state.mealMaxOrderLimit = action.payload;
    },
    setMealSlot(state, action) {
      state.mealSlot = action.payload;
    },
    setMealDate(state, action) {
      state.mealDate = action.payload;
    },
    setMealOrderDeadline(state, action) {
      state.mealOrderDeadline = action.payload;
    },
    resetCreateDishFormValues(state, action) {
      state.dishName = null;
      state.dishLabel = null;
      state.dishPrice = null;
      state.dishImage = null;
    },
  },
  extraReducers: {
    // fetchDishesByCook
    [fetchDishesByCook.fulfilled]: (state, action) => {
      console.log("Fulfilled", action.payload);
      state.dishesByCook = action.payload?.length === 0 ? null : action.payload;
    },
    [fetchDishesByCook.rejected]: (state, action) => {
      console.log(action.payload);
    },

    // createDish
    [createDish.fulfilled]: (state, action) => {
      console.log("Fulfilled", action.payload);
    },
    [createDish.rejected]: (state, action) => {
      console.log(action.payload);
    },

    // updateDish
    [updateDish.fulfilled]: (state, action) => {
      console.log("Fulfilled", action.payload);
      toast.success("Dish Updated Successfully!");
    },
    [updateDish.rejected]: (state, action) => {
      console.log(action.payload);
    },

    // deleteDish
    [deleteDish.fulfilled]: (state, action) => {
      console.log("Fulfilled", action.payload);
    },
    [deleteDish.rejected]: (state, action) => {
      console.log(action.payload);
    },

    // addDishToMeal
    [addDishToMeal.fulfilled]: (state, action) => {
      console.log("Fulfilled", action.payload);
    },
    [addDishToMeal.rejected]: (state, action) => {
      console.log("Rejected", action.payload);
    },
  },
});

export const {
  setDishName,
  setDishLabel,
  setDishPrice,
  setDishImage,
  toggleCreateDishForm,
  toggleUpdateDishForm,
  setCurrentDish,
  setMealMaxOrderLimit,
  setMealSlot,
  setMealOrderDeadline,
  setMealDate,
  toggleAddDishToMealForm,
  resetCreateDishFormValues,
} = cookSlice.actions;

export const dishesByCook = (state) => state.cook.dishesByCook;
export const getCurrentDish = (state) => state.cook.currentDish;
export const dishName = (state) => state.cook.dishName;
export const dishLabel = (state) => state.cook.dishLabel;
export const dishPrice = (state) => state.cook.dishPrice;
export const dishImage = (state) => state.cook.dishImage;
export const dishId = (state) => state.cook.dishId;
export const openCreateDishForm = (state) => state.cook.openCreateDishForm;
export const getOpenUpdateDishForm = (state) => state.cook.openUpdateDishForm;
export const getOpenAddDishToMealForm = (state) =>
  state.cook.openAddDishToMealForm;
export const getMealMaxOrderLimit = (state) => state.cook.mealMaxOrderLimit;
export const getMealSlot = (state) => state.cook.mealSlot;
export const getMealOrderDeadline = (state) => state.cook.mealOrderDeadline;
export const getMealDate = (state) => state.cook.mealDate;

export default cookSlice.reducer;
