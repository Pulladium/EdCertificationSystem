import Grid from "@mui/material/Grid2";
import Button from "@mui/material/Button";
import BasePaginatedList from "./paginateList/BasePaginatedList";
import keycloak from "../config/keycloak";
import AdminPanel from "./AdminPanel";
import {useNavigate} from "react-router-dom";
import CertificatesList from "./pageSubLists/CertificatesList";
import OrganizationsList from "./pageSubLists/OrganizationsList";
import FetchWrapperPaginatedList from "./paginateList/FetchWrapperPaginatedList";

export default function HomePanel({isAuthenticated, setInfoMessage, infoMessage}) {

    const navigate = useNavigate();

    const handleCreateCertificate = () => {
        navigate('/create-certificate');
    };

    let handleCreateOrg = () => {};

    return (

        <Grid container spacing={2} >
            <Grid margin={2} size={{ xs: 10, md: 5 }}>
                {/*<Button variant="outlined" color="primary" onClick={handleCreateCertificate}>Add Certificate</Button>*/}
                {/*<FetchWrapperPaginatedList fetchApiEndpoint="/api/certificates">*/}
                {/*<BasePaginatedList AddButtonLabel={"Add new certificate"} onAddButtonClick={handleCreateCertificate} ListComponent={CertificatesList} apiEndpoint={"http://localhost:8080/api/data/certificates/pagingList"} />*/}
                {/*</FetchWrapperPaginatedList>*/}
                <FetchWrapperPaginatedList
                    fetchApiEndpoint={"http://localhost:8080/api/data/certificates/pagingList"}>
                    <BasePaginatedList
                        onAddButtonClick={handleCreateCertificate}
                        AddButtonLabel={"\"Add new certificate\""}
                        ListComponent={CertificatesList}
                    />
                </FetchWrapperPaginatedList>

            </Grid>
            <Grid margin={2} size={{ xs: 10, md: 5}}>
                {/*<Button variant="outlined" color="primary">Add Organization</Button>*/}
                {/*<BasePaginatedList AddButtonLabel={"Create new organization"} onAddButtonClick={handleCreateOrg} ListComponent={OrganizationsList} apiEndpoint={"http://localhost:8080/api/data/organization/pagingList"} />*/}
                <FetchWrapperPaginatedList
                    fetchApiEndpoint={"http://localhost:8080/api/data/organization/pagingList"}>
                    <BasePaginatedList
                        onAddButtonClick={handleCreateCertificate}
                        AddButtonLabel={"Manage Your Organizations"}
                        ListComponent={OrganizationsList}
                    />
                </FetchWrapperPaginatedList>
            </Grid>
            <Grid marginY={6} size={{ xs: 2, md: 1}}>
                <Button variant="outlined" color="primary">Verify Certificate</Button>
            </Grid>
            <Grid>
                {isAuthenticated && keycloak.hasRealmRole('admin') && (
                    <AdminPanel>
                        <Button variant="outlined" color="primary">Add Admin</Button>
                    </AdminPanel>
                )}
            </Grid>
        </Grid>
    )
}