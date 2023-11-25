import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { getSelectedStore } from "./customerSlice";
import { fetchSchedulesByCook } from "../Cook/cookSlice";
import { Button, Card, CardMedia, Typography } from "@mui/material";
import CustomerAppBar from "./CustomerAppBar";
import PlaceIcon from "@mui/icons-material/Place";
import StoreMealSchedule from "./StoreMealSchedule";

export default function TiffinServiceStorePage() {
  const store = useSelector(getSelectedStore);
  const dispatch = useDispatch();

  useEffect(() => {
    dispatch(fetchSchedulesByCook({ cookId: store?.id, isCustomer: true }));
  }, [dispatch, store]);

  console.log(store);

  return (
    <div>
      <CustomerAppBar />
      <div style={{ marginTop: "4rem" }}>
        <Card>
          <CardMedia sx={{ height: 200 }} image={store?.bannerImage} />
        </Card>
        <div style={{ padding: "1rem" }}>
          <Typography variant="h4">
            {store?.businessName.toUpperCase()}
          </Typography>
          <div style={{ display: "flex", alignItems: "center" }}>
            <PlaceIcon />
            <Typography>{store?.address}</Typography>
          </div>
        </div>
      </div>
      <div style={{ padding: "2rem" }}>
        <Button
          variant="contained"
          style={{ backgroundColor: "#000", marginBottom: "1rem" }}
        >
          Subscribe
        </Button>
        <StoreMealSchedule />
      </div>
    </div>
  );
}
