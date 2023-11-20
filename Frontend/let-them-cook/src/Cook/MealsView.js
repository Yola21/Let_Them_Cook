import React, { useState } from "react";
import {
  Button,
  Card,
  CardActions,
  CardContent,
  CardMedia,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
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
  fetchDishesByMeal,
  getDishesByMeal,
  getMealForDish,
  getMeals,
  getOpenDishesByMealDialog,
  setCurrentMeal,
  setMealForDish,
  setMealId,
  toggleAddDishToMealForm,
  toggleCreateMealForm,
  toggleOpenDishesByMealDialog,
  toggleUpdateMeal,
} from "./cookSlice";
import AddDishToMealForm from "./AddDishToMealForm";

export default function MealsView() {
  const meals = useSelector(getMeals);
  const dispatch = useDispatch();
  const [deleteSelectedDish, setDeleteSelectedDish] = useState(null);
  const [openDeleteDIalog, setOpenDeleteDialog] = useState(false);
  const [removeMeal, setDeleteMeal] = useState(false);
  const { id } = useParams();

  const onUpdateMealClick = (e, id) => {
    e.stopPropagation();
    dispatch(toggleUpdateMeal(true));
    dispatch(toggleAddDishToMealForm(true));
    const selectedMeal = meals.filter((dish) => dish.id === id);
    dispatch(setCurrentMeal(selectedMeal[0]));
  };

  const onDeleteMealClick = (e, id) => {
    e.stopPropagation();
    const mealToBeDeleted = meals.filter((dish) => dish.id === id);
    setDeleteSelectedDish(mealToBeDeleted);
    setDeleteMeal(true);
    setOpenDeleteDialog(true);
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

  const handleOnClick = async (e, meal) => {
    dispatch(setMealForDish(meal));
    await dispatch(fetchDishesByMeal({ id: meal.id }));
    dispatch(toggleOpenDishesByMealDialog(true));
  };

  const handleAddDishToMeal = (e, id) => {
    e.stopPropagation();
    dispatch(toggleCreateMealForm(true));
    dispatch(setMealId(id));
  };

  return (
    <div
      style={{
        padding: "1rem",
        display: "flex",
        flexWrap: "wrap",
        justifyContent: "space-between",
      }}
    >
      {meals?.map((dish) => (
        <Card
          style={{
            marginBottom: "1rem",
            width: "40%",
            boxShadow: "5px 5px 10px #000",
          }}
          onClick={(e) => handleOnClick(e, dish)}
        >
          <CardMedia sx={{ height: 120 }} image={dish.image} />
          <CardContent>
            <Typography variant="h5">{dish.name}</Typography>
            <Typography>${dish.price}</Typography>
          </CardContent>
          <CardActions
            style={{ display: "flex", justifyContent: "space-between" }}
          >
            <Button
              variant="contained"
              style={{ backgroundColor: "#000" }}
              onClick={(e) => handleAddDishToMeal(e, dish.id)}
            >
              Add Dishes
            </Button>
            <div>
              <IconButton onClick={(e) => onUpdateMealClick(e, dish.id)}>
                <Edit style={{ color: "green" }} />
              </IconButton>
              <IconButton onClick={(e) => onDeleteMealClick(e, dish.id)}>
                <Delete style={{ color: "red" }} />
              </IconButton>
            </div>
          </CardActions>
        </Card>
      ))}
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
      <DishesByMeals />
    </div>
  );
}

export function DishesByMeals() {
  const dishes = useSelector(getDishesByMeal);
  const open = useSelector(getOpenDishesByMealDialog);
  const meal = useSelector(getMealForDish);
  const dispatch = useDispatch();

  const onClose = () => {
    dispatch(toggleOpenDishesByMealDialog(false));
  };

  return (
    <Dialog open={open} onClose={onClose}>
      <DialogTitle>{meal?.name}</DialogTitle>
      <DialogContent style={{ backgroundColor: "#f7f7f7", paddingTop: "1rem" }}>
        {dishes == null
          ? "No Dishes Added"
          : dishes?.map((dish) => (
              <Card
                style={{
                  display: "flex",
                  marginBottom: "1rem",
                  boxShadow: "3px #000",
                }}
              >
                <CardMedia
                  sx={{ width: "6.3rem", height: "6.3rem" }}
                  image={dish.image}
                />
                <CardContent>
                  <Typography variant="h5">{dish.name}</Typography>
                  <Typography
                    style={{
                      padding: "2px",
                      borderRadius: "3px",
                      width:
                        dish.type.toLowerCase() === "veg" ? "2rem" : "4rem",
                      color: "#fff",
                      backgroundColor:
                        dish.type.toLowerCase() === "veg" ? "#04df04" : "red",
                    }}
                  >
                    {dish.type}
                  </Typography>
                </CardContent>
              </Card>
            ))}
      </DialogContent>
    </Dialog>
  );
}
