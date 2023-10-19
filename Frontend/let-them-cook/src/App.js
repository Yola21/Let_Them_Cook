import './App.css';
import CookSignUp from './Authentication/CookSignUp';
import CustomerSignUp from './Authentication/CustomerSignUp';
import Login from './Authentication/Login';
import {
  BrowserRouter as Router,
  Switch,
  Route,
} from "react-router-dom";
import Cook from './Cook/Cook';
import Customer from './Customer/Customer';
import Admin from './Admin/Admin';
import { Redirect } from 'react-router-dom/cjs/react-router-dom';
import { useSelector } from 'react-redux';
import { getCurrentUserRole } from './Authentication/authSlice';

function App() {
  const loggedInUserRole = useSelector(getCurrentUserRole);
  
  return (
    <Router>
      <Switch>
        <Route path="/cook/signup"> <CookSignUp /> </Route>
        <Route path="/customer/signup"> <CustomerSignUp /> </Route>
        <Route path="/cook"> <Cook /> </Route>
        <Route path="/customer"> <Customer /> </Route>
        <Route path="/admin"> <Admin /> </Route>
        { loggedInUserRole != null && <Redirect from="/" to={loggedInUserRole === 'admin' ? '/admin' : loggedInUserRole === 'cook' ? '/cook' : '/customer' } /> }
        <Route path="/"> <Login /> </Route>
      </Switch>
    </Router>
  );
}

export default App;
