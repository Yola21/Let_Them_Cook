import React, { useState } from "react";
import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Divider,
  IconButton,
  Typography,
} from "@mui/material";
import Delete from "@mui/icons-material/Delete";
import Edit from "@mui/icons-material/Edit";
import { useParams } from "react-router-dom/cjs/react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  deleteDish,
  deleteMeal,
  dishesByCook,
  getMeals,
  setCurrentDish,
  setCurrentMeal,
  setDishId,
  toggleAddDishToMealForm,
  toggleCreateDishForm,
  toggleCreateMealForm,
  toggleUpdateDish,
  toggleUpdateDishForm,
  toggleUpdateMeal,
} from "./cookSlice";
import AddDishToMealForm from "./AddDishToMealForm";

export default function MealsView() {
  const dishes = useSelector(dishesByCook);
  const meals = useSelector(getMeals);
  const dispatch = useDispatch();
  const [deleteSelectedDish, setDeleteSelectedDish] = useState(null);
  const [openDeleteDIalog, setOpenDeleteDialog] = useState(false);
  const [removeMeal, setDeleteMeal] = useState(false);
  const { id } = useParams();

  const onUpdateDishClick = (e, id) => {
    dispatch(toggleUpdateDish(true));
    dispatch(toggleCreateDishForm(true));
    const selectedDish = dishes.filter((dish) => dish.id === id);
    dispatch(setCurrentDish(selectedDish[0]));
  };

  const onDeleteDishClick = (e, id) => {
    const dishToBeDeleted = dishes.filter((dish) => dish.id === id);
    setDeleteSelectedDish(dishToBeDeleted);
    setDeleteMeal(false);
    setOpenDeleteDialog(true);
  };

  const onUpdateMealClick = (e, id) => {
    dispatch(toggleUpdateMeal(true));
    dispatch(toggleAddDishToMealForm(true));
    const selectedMeal = meals.filter((dish) => dish.id === id);
    dispatch(setCurrentMeal(selectedMeal[0]));
  };

  const onDeleteMealClick = (e, id) => {
    const mealToBeDeleted = meals.filter((dish) => dish.id === id);
    setDeleteSelectedDish(mealToBeDeleted);
    setDeleteMeal(true);
    setOpenDeleteDialog(true);
  };

  const onAddToMealClick = (e, id) => {
    dispatch(toggleCreateMealForm(true));
    dispatch(setDishId(id));
    // dispatch(toggleAddDishToMealForm(true));
    // const selectedDish = dishes.filter((dish) => dish.id === id);
    // dispatch(setCurrentDish(selectedDish[0]));
  };

  const handleCloseDeleteDialog = () => {
    setOpenDeleteDialog(false);
  };

  const handleDeleteConfirm = () => {
    if (removeMeal) {
      dispatch(deleteMeal({ id: deleteSelectedDish[0].id, cookId: id }));
    } else {
      dispatch(deleteDish({ id: deleteSelectedDish[0].id, cookId: id }));
    }
    setOpenDeleteDialog(false);
  };

  const handleOnClick = () => {
    // dispatch(fetchDishe)
  };
  return (
    <div style={{ padding: "3rem" }}>
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          marginBottom: "3rem",
        }}
      >
        {/* <Typography variant="h4">Dishes Created</Typography>
        <Divider style={{ marginBottom: "1rem" }} />
        <Box
          style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "space-between",
          }}
        >
          {dishes?.map((dish) => (
            <Card style={{ backgroundColor: "#f7f5f5", marginBottom: "1rem" }}>
              <CardContent>
                <Typography variant="h5">{dish.name}</Typography>
              </CardContent>
              <CardActions>
                <Button
                  variant="contained"
                  onClick={(e) => onAddToMealClick(e, dish.id)}
                >
                  Add to Meal
                </Button>
                <IconButton onClick={(e) => onUpdateDishClick(e, dish.id)}>
                  <Edit style={{ color: "green" }} />
                </IconButton>
                <IconButton onClick={(e) => onDeleteDishClick(e, dish.id)}>
                  <Delete style={{ color: "red" }} />
                </IconButton>
              </CardActions>
            </Card>
          ))}
        </Box> */}

        <Typography variant="h4">Meals Created</Typography>
        <Divider style={{ marginBottom: "1rem" }} />
        <Box
          style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "space-between",
          }}
        >
          {meals?.map((dish) => (
            <Card
              style={{
                backgroundColor: "#eee",
                marginBottom: "1rem",
                width: "40%",
              }}
              onClick={handleOnClick}
            >
              <CardContent>
                <Typography variant="h5">{dish.name}</Typography>
              </CardContent>
              <CardActions>
                {/* <Button
                  variant="contained"
                  onClick={(e) => onAddToMealClick(e, dish.id)}
                >
                  Add to Meal
                </Button> */}
                <IconButton onClick={(e) => onUpdateMealClick(e, dish.id)}>
                  <Edit style={{ color: "green" }} />
                </IconButton>
                <IconButton onClick={(e) => onDeleteMealClick(e, dish.id)}>
                  <Delete style={{ color: "red" }} />
                </IconButton>
              </CardActions>
            </Card>
          ))}
        </Box>
      </div>
      <Dialog open={openDeleteDIalog} onClose={handleCloseDeleteDialog}>
        <DialogTitle>Delete</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete {deleteSelectedDish?.[0]?.name}{" "}
            dish?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteConfirm}>Delete</Button>
          <Button onClick={handleCloseDeleteDialog}>Cancel</Button>
        </DialogActions>
      </Dialog>
      <AddDishToMealForm />
    </div>
  );
}
