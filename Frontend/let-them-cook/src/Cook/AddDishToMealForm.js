import React from "react";
import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  InputLabel,
  MenuItem,
  Select,
} from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import {
  addDishToMeal,
  getMealForDish,
  getMeals,
  getOpenCreateMealForm,
  setMealForDish,
  toggleCreateMealForm,
} from "./cookSlice";

export default function AddDishToMealForm() {
  const meals = useSelector(getMeals);
  const mealForDish = useSelector(getMealForDish);
  const openCreateMealForm = useSelector(getOpenCreateMealForm);
  const dispatch = useDispatch();

  const handleMealChange = (e) => {
    dispatch(setMealForDish(e.target.value));
  };

  const onMealFormClose = () => {
    dispatch(toggleCreateMealForm(false));
  };

  const handleAddDishToMeal = () => {
    const mealId = meals.find((meal) => meal.name === mealForDish);
    dispatch(addDishToMeal({ mealId: mealId?.id }));
  };

  return (
    <Dialog open={openCreateMealForm} onClose={onMealFormClose}>
      <DialogTitle>Add Dish To Meal</DialogTitle>
      <DialogContent>
        <InputLabel id="meal">Meal</InputLabel>
        <Select
          labelId="meal"
          id="meal"
          value={mealForDish}
          onChange={handleMealChange}
          label="Meal"
        >
          {meals.map((meal) => (
            <MenuItem key={meal.id} value={meal.name}>
              {meal.name}
            </MenuItem>
          ))}
        </Select>
      </DialogContent>
      <DialogActions>
        <Button
          variant="contained"
          style={{ backgroundColor: "#000" }}
          onClick={handleAddDishToMeal}
        >
          Add
        </Button>
      </DialogActions>
    </Dialog>
  );
}
