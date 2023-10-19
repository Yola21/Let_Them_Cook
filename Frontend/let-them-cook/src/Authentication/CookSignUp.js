import React, { useState } from 'react';
import cooking from './images/cooking.jpg';
import Grid from '@mui/material/Grid';
import './Login.css';
import { Box, Button, Input, TextField, Typography } from '@mui/material';
import { useDispatch } from 'react-redux';
import { useLocation } from 'react-router-dom/cjs/react-router-dom.min';
import { createUser } from './authSlice';

function CookSignUp() {
  const [name, setName] = useState(null);
  const [email, setEmail] = useState(null);
  const [phoneNumber, setPhoneNumber] = useState(null);
  const [businessName, setBusinessName] = useState(null);
  const [password, setPassword] = useState(null);
  const [confirmPassword, setConfirmPassword] = useState(null);
  const dispatch = useDispatch();
  const location = useLocation();
  
  const userRole = location.pathname.split('/')[1];

  const onNameChange = (e) => {
    setName(e.target.value);
  };

  const onEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const onPhoneNumberChange = (e) => {
    setPhoneNumber(e.target.value);
  };

  const onBusinessNameChange = (e) => {
    setBusinessName(e.target.value);
  };

  const onPasswordChange = (e) => {
    setPassword(e.target.value);
  };

  const onConfirmPasswordChange = (e) => {
    setConfirmPassword(e.target.value);
  };

  const handleCookRegistration = () => {
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
        <Grid item xs={6} container className='rightItem' alignItems='flex-start' justifyContent='center'>
          <div className='signin'>
            <Typography marginBottom='0.5rem' variant='h5'>Cook Sign Up</Typography>
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
                onChange={onNameChange}
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-required"
                label="Email"
                type='email'
                value={email}
                onChange={onEmailChange}
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-disabled"
                label="Phone Number"
                type='tel'
                value={phoneNumber}
                onChange={onPhoneNumberChange}
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-disabled"
                label="Business Name"
                value={businessName}
                onChange={onBusinessNameChange}
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-disabled"
                label="Password"
                type='password'
                value={password}
                onChange={onPasswordChange}
                sx={{ marginBottom: '1rem' }}
                />
              <TextField
                required
                id="outlined-disabled"
                label="Confirm Password"
                type='password'
                value={confirmPassword}
                onChange={onConfirmPasswordChange}
                sx={{ marginBottom: '1rem' }}
              />
              <Typography>Upload Your Business Verification Document: </Typography>
              <Input type='file' disableUnderline="true" sx={{ marginBottom: '2rem' }} />
              <Button variant='contained' className='actionButton' onChange={handleCookRegistration}>Sign Up</Button>
             </Box>
            </div>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default CookSignUp;
