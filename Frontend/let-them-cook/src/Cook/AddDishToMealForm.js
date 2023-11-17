import React from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Input,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import {
  addDishToMeal,
  getMealDate,
  getMealMaxOrderLimit,
  getMealOrderDeadline,
  getMealSlot,
  getOpenAddDishToMealForm,
  openCreateDishForm,
  setMealDate,
  setMealMaxOrderLimit,
  setMealOrderDeadline,
  setMealSlot,
  toggleAddDishToMealForm,
  toggleCreateDishForm,
} from "./cookSlice";

const currentDate = new Date();
const calendarDays = Array.from({ length: 7 }, (_, index) => {
  const startDate = new Date(currentDate);
  startDate.setDate(currentDate.getDate() - currentDate.getDay());
  const day = new Date(startDate);
  day.setDate(startDate.getDate() + index);
  return day;
});

export default function AddDishToMealForm() {
  const open = useSelector(getOpenAddDishToMealForm);
  const mealMaxOrderLimit = useSelector(getMealMaxOrderLimit);
  const mealOrderDeadline = useSelector(getMealOrderDeadline);
  const mealSlot = useSelector(getMealSlot);
  const mealDate = useSelector(getMealDate);
  const dispatch = useDispatch();
  const currentDate = new Date();

  const toggleModal = () => {
    dispatch(toggleAddDishToMealForm(!open));
  };

  const getFullDate = (date) => {
    const fullDate = `${date.getFullYear()}-${
      date.getMonth() + 1
    }-${date.getDate()}`;
    return fullDate;
  };

  const handleMealMaxOrderLimitChange = (e) => {
    dispatch(setMealMaxOrderLimit(e.target.value));
  };

  const handleMealOrderDeadlineChange = (e) => {
    dispatch(setMealOrderDeadline(e.target.value));
  };

  const handleMealDateChange = (e) => {
    dispatch(setMealDate(e.target.value));
  };

  const handleMealSlotChange = (e) => {
    dispatch(setMealSlot(e.target.value));
  };

  const handleAddToMeal = () => {
    const date = new Date(mealDate);
    dispatch(
      addDishToMeal({ mealMaxOrderLimit, mealOrderDeadline, date, mealSlot })
    );
  };

  return (
    <Dialog open={open} onClose={toggleModal}>
      <DialogTitle>Add Dish to Meal</DialogTitle>
      <DialogContent style={{ display: "flex", flexDirection: "column" }}>
        <TextField
          value={mealMaxOrderLimit}
          onChange={handleMealMaxOrderLimitChange}
          id="maxOrderLimit"
          label="Max Order Limit"
          fullWidth
          variant="standard"
        />

        <Typography style={{ marginTop: "1rem" }}>Meal Date</Typography>
        <input
          type="date"
          min={getFullDate(
            currentDate.toDateString() === calendarDays[0].toDateString()
              ? calendarDays[0]
              : currentDate
          )}
          max={getFullDate(calendarDays[6])}
          value={mealDate}
          onChange={handleMealDateChange}
        />
        <Typography style={{ marginTop: "1rem" }}>Order Deadline</Typography>
        <Input
          type="time"
          value={mealOrderDeadline}
          onChange={handleMealOrderDeadlineChange}
        />

        <InputLabel id="slot" style={{ marginTop: "1rem" }}>
          Meal Slot
        </InputLabel>
        <Select
          labelId="slot"
          id="slot"
          value={mealSlot}
          onChange={handleMealSlotChange}
          label="Slot"
        >
          <MenuItem value="Lunch">Lunch</MenuItem>
          <MenuItem value="Dinner">Dinner</MenuItem>
        </Select>
      </DialogContent>
      <DialogActions>
        <Button onClick={handleAddToMeal}>Add</Button>
      </DialogActions>
    </Dialog>
  );
}
