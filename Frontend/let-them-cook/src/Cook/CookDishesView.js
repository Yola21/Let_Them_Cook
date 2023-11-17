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
  dishesByCook,
  setCurrentDish,
  toggleAddDishToMealForm,
  toggleUpdateDishForm,
} from "./cookSlice";

export default function CookDishesView() {
  const dishes = useSelector(dishesByCook);
  const dispatch = useDispatch();
  const [deleteSelectedDish, setDeleteSelectedDish] = useState(null);
  const [openDeleteDIalog, setOpenDeleteDialog] = useState(false);
  const { id } = useParams();

  const onUpdateDishClick = (e, id) => {
    dispatch(toggleUpdateDishForm(true));
    const selectedDish = dishes.filter((dish) => dish.id === id);
    dispatch(setCurrentDish(selectedDish[0]));
  };

  const onDeleteDishClick = (e, id) => {
    const dishToBeDeleted = dishes.filter((dish) => dish.id === id);
    setDeleteSelectedDish(dishToBeDeleted);
    setOpenDeleteDialog(true);
  };

  const onAddToMealClick = (e, id) => {
    dispatch(toggleAddDishToMealForm(true));
    const selectedDish = dishes.filter((dish) => dish.id === id);
    dispatch(setCurrentDish(selectedDish[0]));
  };

  const handleCloseDeleteDialog = () => {
    setOpenDeleteDialog(false);
  };

  const handleDeleteConfirm = () => {
    dispatch(deleteDish({ id: deleteSelectedDish[0].id, cookId: id }));
    setOpenDeleteDialog(false);
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
        <Typography variant="h4">Dishes Created</Typography>
        <Divider style={{ marginBottom: "1rem" }} />
        <Box
          style={{
            display: "flex",
            flexWrap: "wrap",
            justifyContent: "space-between",
          }}
        >
          {dishes.map((dish) => (
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
    </div>
  );
}
