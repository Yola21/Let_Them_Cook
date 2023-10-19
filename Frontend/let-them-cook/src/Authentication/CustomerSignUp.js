import React, { useState } from 'react';
import cooking from './images/cooking.jpg';
import Grid from '@mui/material/Grid';
import './Login.css';
import { Box, Button, TextField, Typography } from '@mui/material';
import { useDispatch } from 'react-redux';
import { createUser } from './authSlice';
import { useLocation } from 'react-router-dom/cjs/react-router-dom.min';

function CustomerSignUp() {
  const [name, setName] = useState(null);
  const [email, setEmail] = useState(null);
  const [password, setPassword] = useState(null);
  const [confirmPassword, setConfirmPassword] = useState(null);
  const dispatch = useDispatch();
  const location = useLocation();
  
  const userRole = location.pathname.split('/')[1];

  const onChangeName = (e) => {
    setName(e.target.value);
  };

  const onChangeEmail = (e) => {
    setEmail(e.target.value);
  };

  const onChangePassword = (e) => {
    setPassword(e.target.value);
  };

  const onChangeConfirmPassword = (e) => {
    setConfirmPassword(e.target.value);
  };

  const handleCustomerRegistration = () => {
    console.log({name}, {email}, {password}, {confirmPassword});
    dispatch(createUser({
      name ,
      email,
      password,
      role: userRole
    }));
  };

  return (
    <Grid container className='mainContainer'>
      <Grid item xs={12} container justifyContent='center' alignItems='center' marginTop={2}>
        <div className='logoDiv'>
          <img src="/logo.png" alt="Let Them Cook" className='logo' />
        </div>
        <div>
          <Typography variant='h5'>Let Them</Typography>
          <Typography variant='h3'>Cook</Typography>
        </div>
      </Grid>
      <Grid container padding='0 1rem' className='container'>
        <Grid item xs={6} container className='leftItem' alignItems='center' justifyContent='center'>
          <Grid item>
            <Typography variant='h4'>Late To School?</Typography>
            <Typography>Order food from favorite cooks near by.</Typography> 
          </Grid>
          <Grid item>
            <img src={cooking} alt='Let Them Cook' className='cookingPicture'/>
          </Grid>
        </Grid>
        <Grid item xs={6} container className='rightItem' alignItems='center' justifyContent='center'>
          <div className='signin'>
            <Typography marginBottom='3rem' variant='h5'>Customer Sign Up</Typography>
            <Box
              component="form"
              sx={{
                display: 'flex',
                flexDirection: 'column'
              }}
              noValidate
              autoComplete="off"
            >
              <TextField
                required
                id="outlined-required"
                label="Username"
                value={name}
                onChange={onChangeName}
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-required"
                label="Email"
                type='email'
                value={email}
                onChange={onChangeEmail}
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-disabled"
                label="Password"
                type='password'
                value={password}
                onChange={onChangePassword}
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-disabled"
                label="Confirm Password"
                type='password'
                value={confirmPassword}
                onChange={onChangeConfirmPassword}
                sx={{ marginBottom: '3rem' }}
              />

              <Button variant='contained' className='actionButton' onClick={handleCustomerRegistration}>Sign Up</Button>
             </Box>
            </div>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default CustomerSignUp;