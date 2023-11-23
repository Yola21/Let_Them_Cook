import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  createOrder,
  decrementOrderTotalAmount,
  getMealsInCart,
  getMealsInCartQuantity,
  getOrderTotalAmount,
  getPaymentSucceeded,
  incrementOrderTotalAmount,
  orderPayment,
  toggleMealsInCartQuantity,
} from "./customerSlice";
import {
  Avatar,
  Box,
  Button,
  Card,
  CardMedia,
  Dialog,
  DialogContent,
  Divider,
  Paper,
  Typography,
} from "@mui/material";
import CustomerAppBar from "./CustomerAppBar";
import {
  fetchDishesByMeal,
  setMealForDish,
  toggleOpenDishesByMealDialog,
} from "../Cook/cookSlice";
import { DishesByMeals } from "../Cook/MealsView";
import { useHistory, useParams } from "react-router-dom/cjs/react-router-dom";
import StripeCheckout from "react-stripe-checkout";
import { loadStripe } from "@stripe/stripe-js";
import { Elements } from "@stripe/react-stripe-js";
import CheckoutForm from "./CheckoutForm";
import ShoppingCart from "@mui/icons-material/ShoppingCart";

const stripePromise = loadStripe(
  "pk_test_51OFSQSGMYsdcyHsgz2R8va73z2ZYRcRjPZT5q8Lf1CKgh6dJ0vkR6BPvqBiyFcyYuxgG4Bb8AMFgNnc7R2Ba2H60004bSy7epX"
);

export default function MealCart() {
  const [open, setOpen] = useState(false);
  const meals = useSelector(getMealsInCart);
  const mealsQuantity = useSelector(getMealsInCartQuantity);
  const orderTotalAmount = useSelector(getOrderTotalAmount);
  const dispatch = useDispatch();
  const history = useHistory();
  const { id } = useParams();
  const paymentSucceeded = useSelector(getPaymentSucceeded);
  // const publishableKey =
  //   "pk_test_51ODzzmJY6GXGKQds7L6BPBTZn3zjDZDol5VdqmL8QQJ8CopXZuxq3ZNEIDHtfVscb9itLuqkH06duMkBDMdCWJdQ00g5Q5caqc";

  const [clientSecret, setClientSecret] = useState("");

  useEffect(() => {
    // Create PaymentIntent as soon as the page loads
    fetch("http://localhost:8080/payment", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ payment: "string" }),
    })
      .then((res) => res.json())
      .then((data) => setClientSecret(data.clientSecret));
  }, []);

  useEffect(() => {
    if (paymentSucceeded) {
      dispatch(createOrder({ id, history }));
      setOpen(false);
    }
  }, [paymentSucceeded, dispatch]);

  const appearance = {
    theme: "stripe",
  };
  const options = {
    clientSecret,
    appearance,
  };

  const handleIncrementMealQuantity = (e, meal) => {
    e.stopPropagation();
    dispatch(
      toggleMealsInCartQuantity({
        mealId: meal,
        value: mealsQuantity[meal] + 1,
      })
    );
    dispatch(incrementOrderTotalAmount(meals[meal]?.price));
  };

  const handleDecrementMealQuantity = (e, meal) => {
    e.stopPropagation();
    dispatch(
      toggleMealsInCartQuantity({
        mealId: meal,
        value: mealsQuantity[meal] - 1,
      })
    );
    dispatch(decrementOrderTotalAmount(meals[meal]?.price));
  };

  const onClick = async (e, meal) => {
    dispatch(setMealForDish(meal));
    await dispatch(fetchDishesByMeal({ id: meal.id }));
    dispatch(toggleOpenDishesByMealDialog(true));
  };

  const handleCheckoutClick = () => {
    history.push(`/customer/${id}/payment`);
  };

  const stripePrice = 1000 * 100;

  const onPlaceOrder = () => {
    setOpen(true);
    // history.push(`/customer/${id}/checkout`);
    // console.log(token);
    // dispatch(orderPayment({ token, customerId: id, amount: 100 }));
    // dispatch(createOrder({ id }));
    // dispatch(orderPayment({ token, customerId: id, amount: orderTotalAmount }));
  };

  console.log({ meals }, { mealsQuantity });

  if (Object.keys(meals).length === 0) {
    return (
      <>
        <CustomerAppBar />
        <div
          style={{
            marginTop: "10rem",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <ShoppingCart style={{ width: "30%", height: "30%" }} />
          <Typography>Your Cart is Empty!</Typography>
        </div>
      </>
    );
  }
  return (
    <div>
      <CustomerAppBar />
      <div style={{ marginTop: "4.2rem", padding: "2rem" }}>
        <div>
          {Object.keys(meals).map((meal) => (
            <Paper
              style={{ display: "flex", marginBottom: "1rem" }}
              onClick={(e) => onClick(e, meals[meal])}
            >
              <Card>
                <CardMedia
                  sx={{ width: 250, height: 140 }}
                  image={meals[meal].image}
                />
              </Card>
              <Box
                style={{
                  marginLeft: "1rem",
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "space-between",
                }}
              >
                <Box>
                  <Typography variant="h5">{meals[meal].name}</Typography>
                  <Typography>${meals[meal].price}</Typography>
                </Box>
                <Box
                  style={{
                    paddingBottom: "0.5rem",
                    display: "flex",
                    alignItems: "center",
                  }}
                >
                  <Button
                    variant="contained"
                    style={{ backgroundColor: "#000" }}
                    onClick={(e) => handleDecrementMealQuantity(e, meal)}
                  >
                    -
                  </Button>
                  <Typography style={{ margin: "0 0.5rem" }}>
                    {mealsQuantity[meal]}
                  </Typography>
                  <Button
                    variant="contained"
                    style={{ backgroundColor: "#000" }}
                    onClick={(e) => handleIncrementMealQuantity(e, meal)}
                  >
                    +
                  </Button>
                </Box>
              </Box>
            </Paper>
          ))}
        </div>
        <Typography variant="h4" style={{ marginTop: "2rem" }}>
          Order Details
        </Typography>
        <Divider />
        <div style={{ marginTop: "1rem" }}>
          {Object.keys(meals).map((meal) => (
            <Box style={{ display: "flex", justifyContent: "space-between" }}>
              <Typography>
                {meals[meal].name} * {mealsQuantity[meal]}
              </Typography>
              <Typography>
                $ {meals[meal].price * mealsQuantity[meal]}
              </Typography>
            </Box>
          ))}
        </div>
        <Divider />
        <div style={{ display: "flex", justifyContent: "space-between" }}>
          <Typography variant="h6">Total</Typography>
          <Typography>$ {orderTotalAmount}</Typography>
        </div>
        <div
          style={{
            marginTop: "1rem",
            display: "flex",
            justifyContent: "center",
          }}
        >
          <Button variant="contained" onClick={onPlaceOrder}>
            Proceed to Checkout
          </Button>
          <Dialog
            open={open}
            onClose={() => setOpen(false)}
            // style={{ width: "90%" }}
          >
            {paymentSucceeded ? (
              <DialogContent>
                <Typography>Your order is being placed...</Typography>
              </DialogContent>
            ) : (
              <DialogContent>
                {clientSecret && (
                  <Elements options={options} stripe={stripePromise}>
                    <CheckoutForm />
                  </Elements>
                )}
              </DialogContent>
            )}
          </Dialog>
          {/* <Button variant="contained" onClick={onPlaceOrder}>
            Proceed to Checkout
          </Button> */}
        </div>
        <DishesByMeals />
      </div>
    </div>
  );
}
