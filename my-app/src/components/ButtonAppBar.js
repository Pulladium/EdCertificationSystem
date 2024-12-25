import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';



export default function ButtonAppBar({ keycloak , isInitialized, isAuthenticated}) {


    return (
        <Box sx={{ flexGrow: 1 }}>
            <AppBar position="static">
                <Toolbar>
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{ mr: 2 }}
                    >
                        <MenuIcon />
                    </IconButton>
                    <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                        Certification Edu
                    </Typography>

                    {/*
            Если Keycloak не инициализирован, можно временно показывать заглушку/пусто,
            чтобы страница не дёргалась при рендере. Или просто оставить как есть.
          */}
                    {isInitialized && !isAuthenticated && (
                        <Button color="inherit" onClick={() => keycloak.login()}>
                            /Login
                        </Button>
                    )}
                    {isInitialized && isAuthenticated && (
                        <Button color="inherit" onClick={() => keycloak.logout()}>
                            /Logout
                        </Button>
                    )}

                </Toolbar>
            </AppBar>
        </Box>
    );
}
