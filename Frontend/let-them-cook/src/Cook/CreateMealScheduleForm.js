import React from "react";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Typography,
  Input,
} from "@mui/material";
import {
  createSchedule,
  getOpenCreateMealScheduleForm,
  getScheduleName,
  getScheduleStartDate,
  setScheduleName,
  setScheduleStartDate,
  toggleCreateMealScheduleForm,
} from "./cookSlice";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom/cjs/react-router-dom";

const currentDate = new Date();
const calendarDays = Array.from({ length: 7 }, (_, index) => {
  const startDate = new Date(currentDate);
  startDate.setDate(currentDate.getDate() - currentDate.getDay());
  const day = new Date(startDate);
  day.setDate(startDate.getDate() + index);
  return day;
});

const getFullDate = (date) => {
  const fullDate = `${date.getFullYear()}-${
    date.getMonth() + 1
  }-${date.getDate()}`;
  return fullDate;
};

export default function CreateMealScheduleForm() {
  const open = useSelector(getOpenCreateMealScheduleForm);
  const scheduleName = useSelector(getScheduleName);
  const scheduleStartDate = useSelector(getScheduleStartDate);
  const dispatch = useDispatch();
  const { id } = useParams();

  const toggleModal = () => {
    dispatch(toggleCreateMealScheduleForm(false));
  };

  const handleScheduleNameChange = (e) => {
    dispatch(setScheduleName(e.target.value));
  };

  const handleScheduleStartDateChange = (e) => {
    console.log(e);
    dispatch(setScheduleStartDate(e.target.value));
  };

  const handleCreateSchedule = () => {
    dispatch(createSchedule({ cookId: id }));
    dispatch(toggleCreateMealScheduleForm(false));
  };

  return (
    <Dialog open={open} onClose={toggleModal}>
      <DialogTitle>Create Meal Schedule</DialogTitle>
      <DialogContent style={{ display: "flex", flexDirection: "column" }}>
        <TextField
          value={scheduleName}
          onChange={handleScheduleNameChange}
          id="name"
          label="Name"
          fullWidth
          variant="standard"
        />

        <Typography style={{ marginTop: "1rem" }}>
          Select Schedule Week Start Date
        </Typography>
        {/* <Input
          type="date"
          min={getFullDate(calendarDays[0])}
          value={scheduleStartDate}
          onChange={handleScheduleStartDateChange}
        /> */}
        <input
          type="date"
          // type="datetime-local"
          min={getFullDate(calendarDays[0])}
          //   max={getFullDate(calendarDays[6])}
          value={scheduleStartDate}
          onChange={handleScheduleStartDateChange}
        />
      </DialogContent>
      <DialogActions>
        <Button onClick={handleCreateSchedule}>Create</Button>
      </DialogActions>
    </Dialog>
  );
}
