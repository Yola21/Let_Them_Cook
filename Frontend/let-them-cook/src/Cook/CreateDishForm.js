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
import React from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom/cjs/react-router-dom";
import {
  createDish,
  dishImage,
  dishLabel,
  dishName,
  dishPrice,
  openCreateDishForm,
  resetCreateDishFormValues,
  setDishImage,
  setDishLabel,
  setDishName,
  setDishPrice,
  toggleCreateDishForm,
} from "./cookSlice";

export default function CreateDishForm() {
  const open = useSelector(openCreateDishForm);
  const name = useSelector(dishName);
  const label = useSelector(dishLabel);
  const price = useSelector(dishPrice);
  const image = useSelector(dishImage);
  const dispatch = useDispatch();
  const { id } = useParams();

  const handleDishName = (e) => {
    dispatch(setDishName(e.target.value));
  };

  const handleDishLabel = (e) => {
    dispatch(setDishLabel(e.target.value));
  };

  const handleDishPrice = (e) => {
    dispatch(setDishPrice(e.target.value));
  };

  const handleDishImage = (e) => {
    dispatch(setDishImage(e.target.files));
  };

  const handleCreateDish = () => {
    dispatch(createDish({ id }));
    dispatch(toggleCreateDishForm(false));
    dispatch(resetCreateDishFormValues());
  };

  const handleDialogClose = () => {
    dispatch(toggleCreateDishForm(false));
  };

  return (
    <Dialog open={open} onClose={handleDialogClose}>
      <DialogTitle>Create a Dish</DialogTitle>
      <DialogContent style={{ display: "flex", flexDirection: "column" }}>
        <TextField
          value={name}
          onChange={handleDishName}
          margin="dense"
          id="name"
          label="Name"
          fullWidth
          variant="standard"
        />
        <TextField
          value={price}
          onChange={handleDishPrice}
          margin="dense"
          id="price"
          label="Price in CAD"
          fullWidth
          variant="standard"
          style={{ marginTop: "1rem" }}
        />
        <InputLabel id="label" style={{ marginTop: "1rem" }}>
          Dish Type
        </InputLabel>
        <Select
          labelId="label"
          id="label"
          value={label}
          label="Label"
          onChange={handleDishLabel}
        >
          <MenuItem value="Veg">Veg</MenuItem>
          <MenuItem value="Non-Veg">Non-Veg</MenuItem>
        </Select>
        <Typography style={{ marginTop: "1rem" }}>
          Upload a picture of the dish
        </Typography>
        <Input
          type="file"
          value={image?.[1]}
          onChange={handleDishImage}
          disableUnderline="true"
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleCreateDish}>Create</Button>
      </DialogActions>
    </Dialog>
  );
}
