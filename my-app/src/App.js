import logo from './logo.svg';
import './App.css';
import ButtonAppBar from "./components/Appbar";
import PaginatedList from "./components/PaginatedList";

import Grid from "@mui/material/Grid2";
import Button from "@mui/material/Button";
import Keycloak from "keycloak-js";
import {useState} from "react";
import {httpClient} from "./config/HttpClient";
// import { httpClient } from './HttpClient.js';



// async function fetchData() {
//     let response = await fetch('https://api.example.com/data');
//     let data = await response.json();
//     console.log(data);
// }
let initOptions = {
    url: 'http://localhost:8484/',
    realm: 'CertEdu',
    clientId: 'cert-edu-api',
}

let kc = new Keycloak(initOptions);

kc.init({
    onLoad: 'login-required', // Supported values: 'check-sso' , 'login-required'
    checkLoginIframe: true,
    pkceMethod: 'S256'
}).then((auth) => {
    if (!auth) {
        window.location.reload();
    } else {
        /* Remove below logs if you are using this on production */
        console.info("Authenticated");
        console.log('auth', auth)
        console.log('Keycloak', kc)
        console.log('Access Token', kc.token)

        /* http client will use this header in every request it sends */
        httpClient.defaults.headers.common['Authorization'] = `Bearer ${kc.token}`;

        kc.onTokenExpired = () => {
            console.log('token expired')
        }
    }
}, () => {
    /* Notify the user if necessary */
    console.error("Authentication Failed");
});

function App() {

    const [infoMessage, setInfoMessage] = useState('');
    /* To demonstrate : http client adds the access token to the Authorization header */
    const callBackend = () => {
        httpClient.get('https://localhost:8080/api/data/categories/1')

    };

    const userCertsData = [
        { id: 1, name: 'Machine Learning', hasAvatar: true },
        { id: 2, name: 'Data Science', hasAvatar: false },
        { id: 3, name: 'Web Development', hasAvatar: true },
        { id: 4, name: 'Cyber Security', hasAvatar: false },
        { id: 5, name: 'Cloud Computing', hasAvatar: true },
        { id: 6, name: 'Artificial Intelligence', hasAvatar: false },
        { id: 7, name: 'Blockchain', hasAvatar: true },
        { id: 8, name: 'Internet of Things', hasAvatar: false },
        { id: 9, name: 'DevOps', hasAvatar: true },
        { id: 10, name: 'Big Data', hasAvatar: false },
    ];
    const userOrgsData = [
        { id: 1, name: 'Missouri State University', hasAvatar: true },
        { id: 2, name: 'University of Missouri', hasAvatar: false },
        { id: 3, name: 'University of Kansas', hasAvatar: true },
        { id: 4, name: 'University of Nebraska', hasAvatar: false },
        { id: 5, name: 'University of Iowa', hasAvatar: true },
        { id: 6, name: 'University of Illinois', hasAvatar: false },
        { id: 7, name: 'University of Arkansas', hasAvatar: true },
        { id: 8, name: 'University of Oklahoma', hasAvatar: false },
        { id: 9, name: 'University of Texas', hasAvatar: true },
        { id: 10, name: 'University of Colorado', hasAvatar: false },
    ];

  return (
    <div className="App">
        <ButtonAppBar />
        <Grid container spacing={2} >
            <Grid margin={2} size={{ xs: 10, md: 5 }}>
                <Button variant="outlined" color="primary">Add Certificate</Button>
                <PaginatedList data={userCertsData} />
            </Grid>
            <Grid margin={2} size={{ xs: 10, md: 5}}>
                <Button variant="outlined" color="primary">Add Organization</Button>
                <PaginatedList data={userOrgsData} />
            </Grid>
            <Grid marginY={6} size={{ xs: 2, md: 1}}>
                <Button variant="outlined" color="primary">Verify Certificate</Button>
            </Grid>
            <Grid marginY={6} size={{ xs: 2, md: 1}}>
                <Button
                    onClick={() => { setInfoMessage(kc.authenticated ? 'Authenticated: TRUE' : 'Authenticated: FALSE') }}
                    variant="outlined" color="primary" >Authed?</Button>

                <p>{infoMessage}</p>
            </Grid>
        </Grid>

    </div>

  );
}

export default App;
