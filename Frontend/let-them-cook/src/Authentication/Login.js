import React, { useState } from 'react';
import cooking from './images/cooking.jpg';
import Grid from '@mui/material/Grid';
import './Login.css';
import { Box, Button, TextField, Typography } from '@mui/material';
import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import { userLogin } from './authSlice';

function Login() {
  const [email, setEmail] = useState(null);
  const [password, setPassword] = useState(null);
  const dispatch = useDispatch();

  const onChangeEmail = (e) => {
    setEmail(e.target.value);
  };

  const onChangePassword = (e) => {
    setPassword(e.target.value);
  };

  const handleLogin = async () => {
      await dispatch(userLogin({
        email: email,
        password: password
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
            <Typography marginBottom='3rem' variant='h5'>Sign In</Typography>
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
                sx={{ marginBottom: '3rem' }}
              />

              <Button variant='contained' className='actionButton' onClick={handleLogin} disabled={ email == null || password == null } >Login</Button>
              <Button sx={{ color: '#f5195b' }}>Forgot Password?</Button>
              <Link to='/cook/signup'>
                <Button fullWidth variant="contained" sx={{ marginTop: '2rem' }} className='actionButton'>Register As a Cook</Button>
              </Link>
              <Typography>OR</Typography>
              <Link to='/customer/signup'>
                <Button variant="contained" className='actionButton'>Register As a Customer</Button>
              </Link>
            </Box>
            </div>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default Login;
