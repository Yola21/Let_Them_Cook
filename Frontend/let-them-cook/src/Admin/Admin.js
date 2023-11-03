import {
  Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
} from "@mui/material";
import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { fetchCooks, getAllCooks } from "./adminSlice";

function Admin() {
  const dispatch = useDispatch();
  const cooks = useSelector(getAllCooks);

  useEffect(() => {
    const fetchAllCooks = () => {
      dispatch(fetchCooks());
    };

    fetchAllCooks();
  }, [dispatch]);

  return (
    <div>
      Pending Cooks Requests
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell>Business Name</TableCell>
              <TableCell>Action</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {cooks?.map((cook) => (
              <TableRow
                key={cook.id}
                sx={{ "&:last-child td, &:last-child th": { border: 0 } }}>
                <TableCell component="th" scope="row">
                  {cook.businessName}
                </TableCell>
                <TableCell>
                  <Button variant="contained">Accept</Button>
                  <Button>Reject</Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </div>
  );
}

export default Admin;
