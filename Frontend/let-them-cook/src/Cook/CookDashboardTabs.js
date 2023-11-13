import * as React from "react";
import Tabs from "@mui/material/Tabs";
import Tab from "@mui/material/Tab";
import CookDishesView from "./CookDishesView";
import CookMealSchedule from "./CookMealSchedule";
import { Box, Typography } from "@mui/material";

function CustomTabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`tab-${index}`}
      aria-labelledby={`tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

export default function CookDashboardTabs() {
  const [value, setValue] = React.useState(0);

  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    <>
      <Tabs value={value} onChange={handleChange}>
        <Tab label="Dishes" id="tab-0" />
        <Tab label="Meal Schedule" id="tab-1" />
      </Tabs>
      <CustomTabPanel value={value} index={0}>
        <CookDishesView />
      </CustomTabPanel>
      <CustomTabPanel value={value} index={1}>
        <CookMealSchedule />
      </CustomTabPanel>
    </>
  );
}
