import React, { useEffect, useState } from 'react';
import {
    List, ListItem, ListItemButton, ListItemIcon, ListItemText,
    Avatar, TablePagination, Paper, CircularProgress
} from '@mui/material';
import StarIcon from '@mui/icons-material/Star';
import { AddCircleOutline } from "@mui/icons-material";
import { blue } from "@mui/material/colors";
import keycloak from "../config/keycloak";
import CertificatesList from "./CertificatesList";

export default function PaginatedList({ onAddButtonClick, AddButtonLabel, ListComponent, apiEndpoint }) {
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchData = async (page, rowsPerPage) => {
        setLoading(true);
        setError(null);
        try {
            const response = await fetch(`${apiEndpoint}?page=${page}&size=${rowsPerPage}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${keycloak.token}`
                },
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const result = await response.json();
            setData(result.content);
            setTotalCount(result.totalElements);
        } catch (error) {
            setError(error.message);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchData(page, rowsPerPage);
    }, [page, rowsPerPage]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    if (loading) {
        return <CircularProgress />;
    }

    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <Paper>
            <List sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}>
                <ListItem disablePadding>
                    <ListItemButton onClick={onAddButtonClick}>
                        <ListItemIcon>
                            <Avatar sx={{ bgcolor: blue[500] }}>
                                <AddCircleOutline />
                            </Avatar>
                        </ListItemIcon>
                        <ListItemText primary={AddButtonLabel || "Add Item"} />
                    </ListItemButton>
                </ListItem>

               <ListComponent data={data} />
                {/*<CertificatesList data={data} />*/}

            </List>
            <TablePagination
                component="div"
                count={totalCount}
                page={page}
                onPageChange={handleChangePage}
                rowsPerPage={rowsPerPage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
}
