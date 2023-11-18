import React, { useEffect } from "react";
import {
  AppBar,
  Avatar,
  Button,
  IconButton,
  Toolbar,
  Tooltip,
  Typography,
} from "@mui/material";
import LogoutIcon from "@mui/icons-material/Logout";
import AddCircleOutline from "@mui/icons-material/AddCircleOutline";
import {
  Link,
  useHistory,
  useParams,
} from "react-router-dom/cjs/react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  createDish,
  getCurrentDishurrentDish,
  deleteDish,
  dishId,
  dishImage,
  dishLabel,
  dishName,
  dishPrice,
  dishesByCook,
  fetchDishesByCook,
  openCreateDishForm,
  setCurrentDish,
  setDishImage,
  setDishLabel,
  setDishName,
  setDishPrice,
  toggleCreateDishForm,
  updateDish,
  toggleAddDishToMealForm,
  resetCreateDishFormValues,
} from "./cookSlice";
import AddDishToMealForm from "./AddDishToMealForm";
import CreateDishForm from "./CreateDishForm";
import UpdateDishForm from "./UpdateDishForm";
import CookDashboardTabs from "./CookDashboardTabs";

export default function Cook() {
  const dishes = useSelector(dishesByCook);
  const history = useHistory();
  const dispatch = useDispatch();
  const { id } = useParams();

  const handleLogout = () => {
    localStorage.removeItem("token");
    history.push("/login");
  };

  const handleCreateDish = () => {
    dispatch(resetCreateDishFormValues());
    dispatch(toggleCreateDishForm(true));
  };

  useEffect(() => {
    dispatch(fetchDishesByCook({ cookId: id }));
  }, [dispatch]);

  return (
    <div>
      <AppBar style={{ backgroundColor: "rgb(87 87 87)" }} position="sticky">
        <Toolbar style={{ display: "flex", justifyContent: "space-between" }}>
          <Typography variant="h6" component="div">
            Let Them Cook
          </Typography>
          <div style={{ display: "flex" }}>
            {dishes != null && (
              <Button
                variant="contained"
                onClick={handleCreateDish}
                style={{ marginRight: "1rem" }}
              >
                Create Dish
              </Button>
            )}
            <Tooltip title="User Profile">
              <Link to={`/cook/${id}/profile`}>
                <Avatar alt="User" />
              </Link>
            </Tooltip>
            <Tooltip title="Logout">
              <IconButton onClick={handleLogout}>
                <LogoutIcon style={{ color: "#fff" }} />
              </IconButton>
            </Tooltip>
          </div>
        </Toolbar>
      </AppBar>
      {dishes == null ? <NoDishView /> : <CookDashboardTabs />}
      <CreateDishForm />
      <UpdateDishForm />
      <AddDishToMealForm />
    </div>
  );
}

const NoDishView = () => {
  const dispatch = useDispatch();

  const toggleModal = () => {
    dispatch(toggleCreateDishForm(true));
  };

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        marginTop: "10rem",
      }}
    >
      <Typography>No Dishes have been added. Create a New Dish</Typography>
      <IconButton>
        <AddCircleOutline
          onClick={toggleModal}
          style={{ width: "5rem", height: "5rem" }}
        />
      </IconButton>
    </div>
  );
};
