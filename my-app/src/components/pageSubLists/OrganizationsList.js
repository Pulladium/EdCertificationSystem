import {Avatar, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import StarIcon from "@mui/icons-material/Star";
import React from "react";
// private String name;
// private String address;
// private String contactInfo;
// private Organization.OrganizationStatus status;
// private String maintainerKeycloakUUID;
export default function OrganizationsList({data}) {
    return (
        <div>
            {data.map((item) => (
                <ListItem key={item.organizationId} disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <Avatar>
                                <StarIcon />
                            </Avatar>
                        </ListItemIcon>
                        <ListItemText primary={item.name} />
                    </ListItemButton>
                </ListItem>
            ))}
        </div>
    );
}