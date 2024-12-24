import logo from './logo.svg';
import './App.css';
import ButtonAppBar from "./components/ButtonAppBar";

import {useEffect, useState} from "react";
import {httpClient} from "./config/HttpClient";
import keycloak, {initKeycloak} from "./config/keycloak";

import HomePanel from "./components/HomePanel";
import {Route, BrowserRouter, Routes} from "react-router-dom";
import CreateCertPanel from "./components/CreateCertPanel";




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
      <BrowserRouter>
    <div className="App">

        {/*
        isInitialized — признак, что Keycloak успел «пройти» init.
        Передаём сам kc, чтобы в AppBar.js работать с keycloak.login(), keycloak.logout(),
        а также isInitialized, чтобы внутри AppBar можно было проверять (если нужно).
      */}
        <ButtonAppBar keycloak={keycloak} isInitialized={isInitialized} isAuthenticated={isAuthenticated} />

        {isAuthenticated && (
            // <Routes>
            //     <Route exact path="/">
            //     <HomePanel keycloak={keycloak} isAuthenticated={isAuthenticated} setInfoMessage={setInfoMessage} infoMessage={infoMessage} />
            //     </Route>
            //     <Route path="/create-certeficate">
            //         <CreateCertPanel />
            //     </Route>
            // </Routes>
            <Routes>
                <Route exact path="/"  element={
                    <HomePanel
                        keycloak={keycloak}
                        isAuthenticated={isAuthenticated}
                        setInfoMessage={setInfoMessage}
                        infoMessage={infoMessage} />
                } />

                <Route path="/create-certificate" element={<CreateCertPanel />} />
            </Routes>
)}
        {/*<Switch>*/}
        {/*    <Route path="/" exact component={HomePanel} />*/}
        {/*    <Route path="/create-certeficate" component={About} />*/}
        {/*    <Route path="/manage-organizations" component={Contact} />*/}
        {/*</Switch>*/}
    </div>
      </BrowserRouter>
  );
}

export default App;
