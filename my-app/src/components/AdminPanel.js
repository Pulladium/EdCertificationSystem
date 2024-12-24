import * as React from 'react';
import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';
import Button from "@mui/material/Button";
import PopperPopupState from "./PopperPopupState";
import ButtonWithPopper from "./ButtonWithPopper";

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
                    <ButtonWithPopper buttonLabel="Is Authenticated"/>
                    <ButtonWithPopper buttonLabel="Show Access Token"/>
                    <ButtonWithPopper buttonLabel="Show Parsed Access token"/>
                    <ButtonWithPopper buttonLabel="Check Token expired"/>
                    <ButtonWithPopper buttonLabel="Send Http 2 Resource Server"/>
                    <ButtonWithPopper buttonLabel="Show Realm Roles"/>
                    </Stack>
            </BasePaper>


    );
}
