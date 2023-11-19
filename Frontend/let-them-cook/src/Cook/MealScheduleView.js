import React, { useState } from "react";
import {
  Accordion,
  AccordionActions,
  AccordionDetails,
  AccordionSummary,
  Button,
  CircularProgress,
  Typography,
} from "@mui/material";
import CookMealSchedule from "./CookMealSchedule";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchMealsBySchedule,
  getMealsBySchedule,
  getScheduleLoading,
  getSchedules,
  setScheduleCalendarDays,
} from "./cookSlice";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { calendarDays } from "../utils/config";

export default function MealScheduleView() {
  const [accordianId, setAccordianId] = useState(null);
  const schedules = useSelector(getSchedules);
  const loading = useSelector(getScheduleLoading);
  const mealsBySchedule = useSelector(getMealsBySchedule);
  const dispatch = useDispatch();

  const handleOnChange = (e, schedule) => {
    setAccordianId(schedule.id);
    const scheduleStartDate = new Date(schedule.start_date);
    const calendar = calendarDays(scheduleStartDate);
    dispatch(setScheduleCalendarDays(calendar));
    console.log({ calendar });
    dispatch(fetchMealsBySchedule({ scheduleId: schedule.id }));
  };

  console.log({ mealsBySchedule });
  return schedules.map((schedule) => (
    <Accordion
      key={schedule.id}
      expanded={schedule.id === accordianId}
      onChange={(e) => handleOnChange(e, schedule)}
      style={{ backgroundColor: "#eee" }}
    >
      <AccordionSummary expandIcon={<ExpandMoreIcon />}>
        <Typography>{schedule.name}</Typography>
      </AccordionSummary>
      <AccordionDetails>
        {loading ? (
          <div style={{ display: "flex", justifyContent: "center" }}>
            <CircularProgress style={{ color: "#000" }} />
          </div>
        ) : (
          <CookMealSchedule />
        )}
      </AccordionDetails>
      {/* <AccordionActions>
        <Button variant="contained" style={{ backgroundColor: "#000" }}>
          Edit
        </Button>
        <Button variant="contained" style={{ backgroundColor: "#000" }}>
          Delete
        </Button>
      </AccordionActions> */}
    </Accordion>
  ));
}
