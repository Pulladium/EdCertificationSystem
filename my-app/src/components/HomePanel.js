import Grid from "@mui/material/Grid2";
import Button from "@mui/material/Button";
import PaginatedList from "./PaginatedList";
import keycloak from "../config/keycloak";
import AdminPanel from "./AdminPanel";

export default function HomePanel({isAuthenticated, setInfoMessage, infoMessage}) {
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
                    onClick={() => { setInfoMessage(keycloak.authenticated ? 'Authenticated: TRUE' : 'Authenticated: FALSE') }}
                    variant="outlined" color="primary" >Authed?</Button>

                <p>{infoMessage}</p>
            </Grid>
            <Grid>
                {isAuthenticated && keycloak.hasRealmRole('admin')} {
                <AdminPanel>
                    <Button variant="outlined" color="primary">Add Admin</Button>
                </AdminPanel>

            }

            </Grid>
        </Grid>
    )
}