import React, { useState } from 'react';
import {
    List, ListItem, ListItemButton, ListItemIcon, ListItemText,
    Avatar, TablePagination, Paper
} from '@mui/material';
import StarIcon from '@mui/icons-material/Star';
import {AddCircleOutline} from "@mui/icons-material";



export default function PaginatedList({data = [], onAddButtonClick, AddButtonLabel}) {
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);

    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    // Slice the data array to get only the items for the current page
    const currentPageData = data.slice(page * rowsPerPage, page * rowsPerPage + rowsPerPage);

    return (
        <Paper>
            <List sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}>
                {page === 0 && (
                    <ListItem disablePadding>
                        <ListItemButton onClick={onAddButtonClick}>
                            <ListItemIcon>
                                <Avatar>
                                <AddCircleOutline />
                                </Avatar>
                            </ListItemIcon>
                            <ListItemText primary={AddButtonLabel || "Add Item"} />
                        </ListItemButton>
                    </ListItem>
                )}
                {currentPageData.map((item) => (
                    <ListItem key={item.id} disablePadding>
                        <ListItemButton>
                            {item.hasAvatar ? (
                                <ListItemIcon>
                                    <Avatar>
                                        <StarIcon />
                                    </Avatar>
                                </ListItemIcon>
                            ) : null}
                            <ListItemText primary={item.name} inset={!item.hasAvatar} />
                        </ListItemButton>
                    </ListItem>
                ))}
            </List>
            <TablePagination
                component="div"
                count={data.length}
                page={page}
                onPageChange={handleChangePage}
                rowsPerPage={rowsPerPage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </Paper>
    );
}
