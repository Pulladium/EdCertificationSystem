import React, { useEffect, useState } from 'react';
import {
    List, ListItem, ListItemButton, ListItemIcon, ListItemText,
    Avatar, TablePagination, Paper, CircularProgress
} from '@mui/material';
import StarIcon from '@mui/icons-material/Star';
import { AddCircleOutline } from "@mui/icons-material";
import { blue } from "@mui/material/colors";
import keycloak from "../../config/keycloak";
import CertificatesList from "../pageSubLists/CertificatesList";


//done setData  setTotalCount isInitialized setIsInitialized setError
export default function BasePaginatedList({
                                              AddButtonIcon = <AddCircleOutline/>,
                                              onAddButtonClick,
                                              AddButtonLabel,
                                              ListComponent,
                                              data,
                                              totalCount,
                                              loading,
                                              error,
                                              fetchData,
                                              addItem,
                                              getPaginatedData
                                          }) {
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    // const [isInitialized, setIsInitialized] = useState(false);
    //
    // useEffect(() => {
    //     if (!isInitialized) {
    //         setIsInitialized(true);
    //         fetchData(page, rowsPerPage);
    //     }
    // }, [isInitialized, fetchData, page, rowsPerPage]);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
        if (fetchData) {
            fetchData(newPage, rowsPerPage);
        }
    };

    const handleChangeRowsPerPage = (event) => {
        const newRowsPerPage = parseInt(event.target.value, 10);
        setRowsPerPage(newRowsPerPage);
        setPage(0);
        if (fetchData) {
            fetchData(0, newRowsPerPage);
        }
        // No need to call getPaginatedData here, it will be called when rendering
    };

    const displayData = getPaginatedData ? getPaginatedData(page, rowsPerPage) : data;

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
                    <ListItemButton onClick={() => onAddButtonClick(addItem)}>
                        <ListItemIcon>
                            <Avatar sx={{ bgcolor: blue[500] }}>
                                {AddButtonIcon}
                            </Avatar>
                        </ListItemIcon>
                        <ListItemText primary={AddButtonLabel || "Add Item"} />
                    </ListItemButton>
                </ListItem>
                <ListComponent data={displayData} />
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
