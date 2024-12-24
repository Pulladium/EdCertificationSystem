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

export default function PagiListFetchWrapper({AddButtonIcon = <AddCircleOutline/>, onAddButtonClick, AddButtonLabel, ListComponent, apiEndpoint }) {
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [localData, setLocalData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);
    const [error, setError] = useState(null);
    const [isInitialized, setIsInitialized] = useState(false);


    // only for new Participant
    const addSimpleItem = (item) => {
        setLocalData(prevData => [...prevData, item]);
        setTotalCount(prevCount => prevCount + 1);
        setPage(0);
    };




    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };


    if (error) {
        return <div>Error: {error}</div>;
    }

    return (
        <Paper>
            <List sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}>
                <ListItem disablePadding>
                    <ListItemButton  onClick={() => onAddButtonClick(addSimpleItem)}>
                        <ListItemIcon>
                            <Avatar sx={{ bgcolor: blue[500] }}>
                                {AddButtonIcon}
                            </Avatar>
                        </ListItemIcon>
                        <ListItemText primary={AddButtonLabel || "Add Item"} />
                    </ListItemButton>
                </ListItem>

                <ListComponent data={localData} />
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
