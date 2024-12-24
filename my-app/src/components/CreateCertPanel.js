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
import BasePaginatedList from "./paginateList/BasePaginatedList";
import ParticipantsList from "./pageSubLists/ParticipantsList";
import OrganizationsList from "./pageSubLists/OrganizationsList";

import DeleteOutlinedIcon from '@mui/icons-material/DeleteOutlined';
import {useState} from "react";

const BasePaper = styled(Paper)(({ theme }) => ({
    width: 'auto',
    height: 'auto',
    minHeight: '30vh',
    padding: theme.spacing(2),
    ...theme.typography.body2,
    textAlign: 'center',
}));

export default function CreateCertPanel() {


    // const [participants, setParticipants] = useState([]);


    const handleAddParticipant = (addSimpleItem) => {
        const newParticipant = { id: Date.now(), name: 'New Participant', hasAvatar: false };
        addSimpleItem(newParticipant);
    };


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
                        <Stack spacing={2} direction={"row"}>
                            <BasePaginatedList onAddButtonClick={handleAddParticipant} AddButtonLabel={"Add new participant"}
                                               apiEndpoint={null} ListComponent={ParticipantsList} />
                           <Paper>
                               <Stack spacing={2} direction={"row"}>
                            <BasePaginatedList AddButtonIcon={<DeleteOutlinedIcon/>} onAddButtonClick={()=>{}} AddButtonLabel={"Clean All"} apiEndpoint={null} ListComponent={OrganizationsList} />
                            <BasePaginatedList onAddButtonClick={()=>{}} AddButtonLabel={"Manage your organizations"} apiEndpoint={null} ListComponent={OrganizationsList} />
                                 </Stack>
                           </Paper>
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
