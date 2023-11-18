import "./App.css";
import CookSignUp from "./Authentication/CookSignUp";
import CustomerSignUp from "./Authentication/CustomerSignUp";
import Login from "./Authentication/Login";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Cook from "./Cook/Cook";
import Customer from "./Customer/Customer";
import Admin from "./Admin/Admin";
import { Redirect } from "react-router-dom/cjs/react-router-dom";
import { useSelector } from "react-redux";
import {
  getCurrentUserInfo,
  getCurrentUserRole,
} from "./Authentication/authSlice";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import CookProfile from "./Cook/CookProfile";

function App() {
  const loggedInUserRole = useSelector(getCurrentUserRole);
  const loggedInUserInfo = useSelector(getCurrentUserInfo);

  return (
    <Router>
      <Switch>
        <Route path="/cook/signup">
          {" "}
          <CookSignUp />{" "}
        </Route>
        <Route path="/customer/signup">
          {" "}
          <CustomerSignUp />{" "}
        </Route>
        <Route path="/cook/:id/profile">
          {" "}
          <CookProfile />{" "}
        </Route>
        <Route path="/cook/:id">
          {" "}
          <Cook />{" "}
        </Route>
        <Route path="/customer">
          {" "}
          <Customer />{" "}
        </Route>
        <Route path="/admin">
          {" "}
          <Admin />{" "}
        </Route>
        {loggedInUserRole != null && (
          <Redirect
            from="/"
            to={
              loggedInUserRole === "admin"
                ? "/admin"
                : loggedInUserRole === "cook"
                ? `/cook/${loggedInUserInfo.id}`
                : "/customer"
            }
          />
        )}
        <Route path="/">
          <Login />
        </Route>
      </Switch>
      <ToastContainer autoClose={3000} position="bottom-center" theme="dark" />
    </Router>
  );
}

export default App;
