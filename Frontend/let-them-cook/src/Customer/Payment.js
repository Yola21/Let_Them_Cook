import React from "react";
import StripeCheckout from "react-stripe-checkout";
import axios from "axios";
import { config } from "../config";

const StripeButton = ({ }) => {
  const publishableKey = "pk_test_51ODzzmJY6GXGKQds7L6BPBTZn3zjDZDol5VdqmL8QQJ8CopXZuxq3ZNEIDHtfVscb9itLuqkH06duMkBDMdCWJdQ00g5Q5caqc";
  console.log(publishableKey);
  const stripePrice = 1000 * 100;

  const onToken = (token) => {
    console.log(token);
    axios
      .post(`${config.BASE_PATH}/payment`, {
        amount: stripePrice,
        token,
      })
      .then((response) => {
        console.log(response);
        alert("payment success");
      })
      .catch((error) => {
        console.log(error);
        alert("Payment failed");
      });
  };

  return (
    <StripeCheckout
      amount={stripePrice}
      label="Pay Now"
      name="Wolf Elite"
      image="https://svgshare.com/i/CUz.svg"
      description={`Your total is 100`}
      panelLabel="Pay Now"
      token={onToken}
      stripeKey={publishableKey}
      currency="CAD"
    />
  );
};

export default StripeButton;