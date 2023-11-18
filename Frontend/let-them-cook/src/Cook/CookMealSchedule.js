import React from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Avatar,
} from "@mui/material";
import "./CookDash.css";
import cooking from "../Authentication/images/cooking.jpg";

const DISHES = [
  {
    name: "Pav Bhaji",
    price: "CAD10",
    label: "Veg",
    image: cooking,
  },
  {
    name: "Paneer Butter Masala",
    price: "CAD18",
    label: "Veg",
    image: cooking,
  },
  {
    name: "Butter Chicken",
    price: "CAD20",
    label: "Non-Veg",
    image: cooking,
  },
  {
    name: "Chicken Bhurji",
    price: "CAD15",
    label: "Non-Veg",
    image: cooking,
  },
  {
    name: "Rajma Chawal",
    price: "CAD15",
    label: "Veg",
    image: cooking,
  },
  {
    name: "Poha",
    price: "CAD5",
    label: "Veg",
    image: cooking,
  },
  {
    name: "Aloo Paratha",
    price: "CAD11",
    label: "Veg",
    image: cooking,
  },
];
const daysOfWeek = [
  "Sunday",
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday",
  "Saturday",
];
const currentDate = new Date();

const getFormattedDate = (date) => {
  const day = date.getDate();
  const daySuffix =
    day > 3 && day < 21 ? "th" : ["st", "nd", "rd"][(day % 10) - 1] || "th";
  return `${day}${daySuffix}`;
};

const calendarDays = Array.from({ length: 7 }, (_, index) => {
  const startDate = new Date(currentDate); // Clone the current date
  startDate.setDate(currentDate.getDate() - currentDate.getDay()); // Set to the start of the current week
  const day = new Date(startDate);
  day.setDate(startDate.getDate() + index);
  return day;
});

export default function CookMealSchedule() {
  return (
    <TableContainer>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell colSpan={8}>
              <Typography variant="h5">
                {currentDate.toLocaleString("en-US", {
                  month: "long",
                  year: "numeric",
                })}
              </Typography>
            </TableCell>
          </TableRow>
          <TableRow>
            <TableCell></TableCell>
            {calendarDays.map((day) => (
              <TableCell
                key={day.toDateString()}
                colSpan={1}
                className={
                  day.toDateString() === currentDate.toDateString()
                    ? "todayCell"
                    : ""
                }
                align="center"
              >
                <Typography variant="body1">
                  {getFormattedDate(day)} {daysOfWeek[day.getDay()]}
                </Typography>
              </TableCell>
            ))}
          </TableRow>
        </TableHead>
        <TableBody>
          <TableRow>
            <TableCell>LUNCH</TableCell>
            {calendarDays.map((day, index) => (
              <>
                <TableCell
                  key={day.toDateString()}
                  className={
                    day.toDateString() === currentDate.toDateString()
                      ? "todayCell"
                      : ""
                  }
                >
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                      flexWrap: "wrap",
                    }}
                  >
                    <Avatar
                      src={DISHES[index].image}
                      style={{ width: "5rem", height: "5rem" }}
                    />
                    <Typography>{DISHES[index].name}</Typography>
                  </div>
                </TableCell>
              </>
            ))}
          </TableRow>
          <TableRow>
            <TableCell>DINNER</TableCell>
            {calendarDays.map((day, index) => (
              <>
                <TableCell
                  key={day.toDateString()}
                  className={
                    day.toDateString() === currentDate.toDateString()
                      ? "todayCell"
                      : ""
                  }
                >
                  <div
                    style={{
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                      flexWrap: "wrap",
                    }}
                  >
                    <Avatar
                      src={DISHES[index].image}
                      style={{ width: "5rem", height: "5rem" }}
                    />
                    <Typography>{DISHES[index].name}</Typography>
                  </div>
                </TableCell>
              </>
            ))}
          </TableRow>
        </TableBody>
      </Table>
    </TableContainer>
  );
}
