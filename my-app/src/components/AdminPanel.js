import * as React from 'react';
import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import { styled } from '@mui/material/styles';
import Button from "@mui/material/Button";
import PopperPopupState from "./PopperPopupState";
import ButtonWithPopper from "./ButtonWithPopper";
import keycloak from "../config/keycloak";
import Grid from "@mui/material/Grid2";

const BasePaper = styled(Paper)(({ theme }) => ({
    width: '45vw',
    height: 'auto',
    minHeight: '30vh',
    padding: theme.spacing(2),
    ...theme.typography.body2,
    textAlign: 'center',
}));

export default function AdminPanel() {
    const handleIsAuthenticated = () => {
        keycloak.authenticated ? console.log("User is authenticated") : console.log("User is not authenticated");
        return keycloak.authenticated ? "User is authenticated : TRUE" : "User is authenticated : FALSE";
    };

    const handleShowAccessToken = () => {
        return keycloak.token;
    };
    const handleShowParsedAccessToken = () => {
        return JSON.stringify(keycloak.tokenParsed);
    };
    const handleShowRealmRoles = () => {
        return keycloak.realmAccess.roles.map((role) => {
            return role + " ";
        });
    };
    const sendPingRequest = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/data/dev/ping', {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${keycloak.token}`
                },
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.text(); // Use text() instead of json()
            console.log('Success:', data);
            return `Success: ${data}`;
        } catch (error) {
            console.error('Error:', error);
            return `Error: ${error.message}`;
        }
    };

    const handleSendHttp2ResourceServer = () => {
        return sendPingRequest();
    };
    const handleRedirect2AdminConsole = () => {
        window.open("http://localhost:8484/admin/master/console/#/CertEdu", "_blank");
    }
    return (

            <BasePaper  square={false} >
                <Stack spacing={2}>
                <h2>Admin Panel</h2>
                    <ButtonWithPopper  buttonLabel="Is Authenticated"
                                       onClick={handleIsAuthenticated}
                                       popperContent={handleIsAuthenticated()}
                    />
                    <ButtonWithPopper buttonLabel="Show Access Token"
                                        onClick={handleShowAccessToken}
                                        popperContent={handleShowAccessToken()}
                    />
                    <ButtonWithPopper buttonLabel="Show Parsed Access token"
                                        onClick={handleShowParsedAccessToken}
                                        popperContent={handleShowParsedAccessToken()}
                    />
                    <ButtonWithPopper buttonLabel="Show Realm Roles"
                                        onClick={handleShowRealmRoles}
                                        popperContent={handleShowRealmRoles()}
                    />
                    <ButtonWithPopper
                        buttonLabel="Send Http 2 Resource Server"
                        onClick={handleSendHttp2ResourceServer}
                        popperContent={handleSendHttp2ResourceServer()}
                    />

                    <Button variant="outlined" onClick={handleRedirect2AdminConsole}
                    >Go To Keycloak Admin Console</Button>
                    </Stack>
            </BasePaper>


    );
}
