import logo from './logo.svg';
import './App.css';
import ButtonAppBar from "./components/ButtonAppBar";
import PaginatedList from "./components/PaginatedList";

import Grid from "@mui/material/Grid2";
import Button from "@mui/material/Button";
import {useEffect, useState} from "react";
import {httpClient} from "./config/HttpClient";
import keycloak, {initKeycloak} from "./config/keycloak";
import AdminPanel from "./components/AdminPanel";
import {Home} from "@mui/icons-material";
import HomePanel from "./components/HomePanel";
// import { httpClient } from './HttpClient.js';



// async function fetchData() {
//     let response = await fetch('https://api.example.com/data');
//     let data = await response.json();
//     console.log(data);
// }


function App() {
    const [isInitialized, setIsInitialized] = useState(false);
    const [isAuthenticated, setIsAuthenticated] = useState(false);

    useEffect(() => {
        console.log("[App useEffect] About to init Keycloak...");

        initKeycloak({
            onLoad: 'check-sso',
            checkLoginIframe: true,
            pkceMethod: 'S256',
        })
            .then(auth => {
                console.log(`[App useEffect] Keycloak init done. auth = ${auth}`);
                setIsInitialized(true);
                if (auth) {
                    console.info("Authenticated");
                    httpClient.defaults.headers.common['Authorization'] = `Bearer ${keycloak.token}`;
                    keycloak.onTokenExpired = () => console.log('Token expired');
                } else {
                    console.info("Not authenticated");
                }
            })
            .catch(() => {
                console.error("Authentication Failed");
                setIsInitialized(true);
            });

        const authSuccessHandler = () => {
            console.log("Auth success event received");
            setIsAuthenticated(true);
        };
        const authLogoutHandler = () => {
            console.log("Auth logout event received");
            setIsAuthenticated(false);
        };

        window.addEventListener('keycloakAuthSuccess', authSuccessHandler);
        window.addEventListener('keycloakAuthLogout', authLogoutHandler);

        return () => {
            window.removeEventListener('keycloakAuthSuccess', authSuccessHandler);
            window.removeEventListener('keycloakAuthLogout', authLogoutHandler);
        };

    }, []);

    const [infoMessage, setInfoMessage] = useState('');
    /* To demonstrate : http client adds the access token to the Authorization header */
    const callBackend = () => {
        httpClient.get('https://localhost:8080/api/data/categories/1')

    };



  return (
    <div className="App">

        {/*
        isInitialized — признак, что Keycloak успел «пройти» init.
        Передаём сам kc, чтобы в AppBar.js работать с keycloak.login(), keycloak.logout(),
        а также isInitialized, чтобы внутри AppBar можно было проверять (если нужно).
      */}
        <ButtonAppBar keycloak={keycloak} isInitialized={isInitialized} isAuthenticated={isAuthenticated} />

        <HomePanel keycloak={keycloak} isAuthenticated={isAuthenticated} setInfoMessage={setInfoMessage} infoMessage={infoMessage} />

    </div>

  );
}

export default App;
