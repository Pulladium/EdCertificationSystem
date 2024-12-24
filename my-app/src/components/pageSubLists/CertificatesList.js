import {Avatar, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import StarIcon from "@mui/icons-material/Star";
import React from "react";

export default function CertificatesList({data}) {
    return (
        <div>
            {data.map((item) => (
                <ListItem key={item.certificateId} disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <Avatar>
                                <StarIcon />
                            </Avatar>
                        </ListItemIcon>
                        <ListItemText primary={item.description} />
                    </ListItemButton>
                </ListItem>
            ))}
        </div>
    );
}