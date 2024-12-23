import Keycloak from "keycloak-js";
const keycloak = new Keycloak({
    url: "http://localhost:8484/auth",
    realm: "CertEdu",
    clientId: "cert-edu-api",
});

export default keycloak;