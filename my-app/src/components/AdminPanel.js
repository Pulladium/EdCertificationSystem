import * as React from 'react';
import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';
import Button from "@mui/material/Button";
import PopperPopupState from "./PopperPopupState";

const BasePaper = styled(Paper)(({ theme }) => ({
    width: '45vw',
    height: 'auto',
    minHeight: '30vh',
    padding: theme.spacing(2),
    ...theme.typography.body2,
    textAlign: 'center',
}));

export default function AdminPanel() {
    return (

            <BasePaper  square={false} >
                <Stack spacing={2}>
                <h2>Admin Panel</h2>
                    <Button variant="outlined" color="primary">Is Authenticated</Button>
                    <Button variant="outlined" color="primary">Show Access Token</Button>
                    <Button variant="outlined" color="primary">Show Parsed Access token</Button>
                    <Button variant="outlined" color="primary">Check Token expired</Button>
                    <Button variant="outlined" color="primary">Send Http 2 Resource Server</Button>
                    <Button variant="outlined" color="primary">Show Realm Roles</Button>
                    <PopperPopupState />
                    </Stack>
            </BasePaper>


    );
}
