// keycloak.js
import Keycloak from "keycloak-js";

const initOptions = {
    url: 'http://localhost:8484/',
    realm: 'CertEdu',
    clientId: 'cert-edu-api',
};

const keycloak = new Keycloak(initOptions);
let initCalled = false;

export function initKeycloak(...args) {
    if (!initCalled) {
        console.log("[Keycloak] init() called for the first time.");
        initCalled = true;

        keycloak.onAuthSuccess = () => {
            console.log("[Keycloak] Authentication successful");
            window.dispatchEvent(new Event('keycloakAuthSuccess'));
        };

        keycloak.onAuthLogout = () => {
            console.log("[Keycloak] Logout");
            window.dispatchEvent(new Event('keycloakAuthLogout'));
        };

        return keycloak.init(...args);
    } else {
        console.log("[Keycloak] init() was already called. Skipping...");
        // Возвращаем то, что нужно вашему коду:
        // например Promise.resolve(keycloak.authenticated)
        return Promise.resolve(keycloak.authenticated);
    }
}

export default keycloak;