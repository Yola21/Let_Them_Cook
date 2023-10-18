import React from 'react';
import cooking from './images/cooking.jpg';
import Grid from '@mui/material/Grid';
import './Login.css';
import { Box, Button, TextField, Typography } from '@mui/material';

function CustomerSignUp() {
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
                label="Email"
                type='email'
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-disabled"
                label="Password"
                type='password'
                sx={{ marginBottom: '1rem' }}
              />
              <TextField
                required
                id="outlined-disabled"
                label="Confirm Password"
                type='password'
                sx={{ marginBottom: '3rem' }}
              />

              <Button variant='contained' className='actionButton'>Sign Up</Button>
             </Box>
            </div>
        </Grid>
      </Grid>
    </Grid>
  );
}

export default CustomerSignUp;