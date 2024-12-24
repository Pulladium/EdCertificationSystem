import React, { useEffect, useState } from 'react';
import {
    List, ListItem, ListItemButton, ListItemIcon, ListItemText,
    Avatar, TablePagination, Paper, CircularProgress
} from '@mui/material';
import StarIcon from '@mui/icons-material/Star';
import { AddCircleOutline } from "@mui/icons-material";
import { blue } from "@mui/material/colors";
import keycloak from "../config/keycloak";
import CertificatesList from "./pageSubLists/CertificatesList";

export default function PaginatedList({AddButtonIcon = <AddCircleOutline/>, onAddButtonClick, AddButtonLabel, ListComponent, apiEndpoint }) {
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [data, setData] = useState([]);
    const [totalCount, setTotalCount] = useState(0);




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
