import * as React from 'react';
import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';
import Button from "@mui/material/Button";
import PopperPopupState from "./PopperPopupState";
import ButtonWithPopper from "./ButtonWithPopper";
import keycloak from "../config/keycloak";
import Grid from "@mui/material/Grid2";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import {FormControl, OutlinedInput} from "@mui/material";
import PaginatedList from "./PaginatedList";

const BasePaper = styled(Paper)(({ theme }) => ({
    width: 'auto',
    height: 'auto',
    minHeight: '30vh',
    padding: theme.spacing(2),
    ...theme.typography.body2,
    textAlign: 'center',
}));

export default function CreateCertPanel() {

    const participants = [
        { id: 1, name: 'Alice', hasAvatar: true },
        { id: 2, name: 'Bob', hasAvatar: false },
        { id: 3, name: 'Charlie', hasAvatar: true },
        { id: 4, name: 'David', hasAvatar: false },
        { id: 5, name: 'Eve', hasAvatar: true },
        { id: 6, name: 'Frank', hasAvatar: false },
        { id: 7, name: 'Grace', hasAvatar: true },
        { id: 8, name: 'Hank', hasAvatar: false },
        { id: 9, name: 'Ivy', hasAvatar: true },
        { id: 10, name: 'Jill', hasAvatar: false },
    ];

    return (

        <BasePaper  square={false} >
                <Box
                    component="form"
                    sx={{'& .MuiTextField-root': {m: 1, width: '25vw'}}}
                    noValidate
                    autoComplete="on"
                >
                    <Stack spacing={2} >
                    <Stack spacing={2} direction={"row"}>
                    <Stack spacing={2}>
                    <TextField
                        required
                        fullWidth
                        id="certTextField"
                        label="Title"
                        helperText="Please enter certificate title"

                    />
                    <TextField
                        required
                        fullWidth
                        id="dicriptionTextField"
                        label="Discription"
                        helperText="Please enter certificate discription"

                    />
                    </Stack>
                        <Stack spacing={2}>
                            <PaginatedList data={participants} />
                            <TextField
                                required
                                fullWidth
                                id="dicriptionTextField"
                                label="Discription"
                                helperText="Please enter certificate discription"

                            />
                        </Stack>
                    </Stack  >
                        <Box maxWidth={"30vw"}>
                        <Button variant="contained" color="primary">Create Certificate</Button>
                        </Box>
                    </Stack>
                </Box>
        </BasePaper>


    );
}
// import * as React from 'react';
// import Box from '@mui/material/Box';
// import TextField from '@mui/material/TextField';
//
// export default function FormPropsTextFields() {
//     return (
//
//     );
// }
