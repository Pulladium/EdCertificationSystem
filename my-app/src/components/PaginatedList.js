import React, { useState } from 'react';
import {
    List, ListItem, ListItemButton, ListItemIcon, ListItemText,
    Avatar, TablePagination, Paper
} from '@mui/material';
import StarIcon from '@mui/icons-material/Star';

const data = [
    { id: 1, name: 'Chelsea Otakan', hasAvatar: true },
    { id: 2, name: 'Eric Hoffman', hasAvatar: false },
    { id: 3, name: 'John Doe', hasAvatar: true },
    { id: 4, name: 'Jane Smith', hasAvatar: false },
    { id: 5, name: 'John Smith', hasAvatar: true },
    { id: 6, name: 'Jane Doe', hasAvatar: false },
    { id: 7, name: 'Eric Hoffman', hasAvatar: true },
    { id: 8, name: 'John Doe', hasAvatar: false },
    { id: 9, name: 'Jane Smith', hasAvatar: true },
    { id: 10, name: 'John Smith', hasAvatar: false },
    // ... add more items as needed
];

export default function PaginatedList() {
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
