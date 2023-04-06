import React, {useEffect, useState} from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import Button from "@mui/material/Button";
import api from "../../services/api";
import {useNavigate} from "react-router-dom";

export default function AllPatients() {
    const [patients, setPatients] = useState<any>([]);
    let navigate = useNavigate();

    useEffect(() => {
        const getPatients = async () => {
            try {
                const result = await api.get("/patients");
                setPatients(result.data);
                console.log(result.data);
            } catch (error) {
                console.log(error);
            }
        };

        getPatients();
    }, []);

    console.log(patients);

    return (
        <>
            <h1>All patients</h1>
            <TableContainer component={Paper}>
                <Table sx={{minWidth: 650}} aria-label="simple table">
                    <TableHead>
                        <TableRow>
                            <TableCell align="center">
                                <strong>Name</strong>
                            </TableCell>
                            <TableCell align="center">
                                <strong>Last name</strong>
                            </TableCell>
                            <TableCell align="center">
                            </TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {patients.map((patient: any) => (
                            <TableRow
                                key={patient.email}
                                sx={{"&:last-child td, &:last-child th": {border: 0}}}
                            >
                                <TableCell align="center">{patient.name}</TableCell>
                                <TableCell align="center">{patient.lastName}</TableCell>
                                <TableCell align="left">
                                    {
                                        <Button
                                            variant="contained"
                                            onClick={() => navigate(`/patientList/${patient.email}`)}
                                        >
                                            Details
                                        </Button>
                                    }
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </>
    );
}
