import {Avatar, ListItem, ListItemButton, ListItemIcon, ListItemText} from "@mui/material";
import StarIcon from "@mui/icons-material/Star";
import React from "react";

// public class ParticipantResponse {
//     private Long participantId;
//     private String name;
//     private String surname;
//     private String email;
//     private LocalDateTime assignedAt;
// }

export default function ParticipantsList({data}) {
    return (
        <div>
            {data.map((item) => (
                <ListItem key={item.participantId} disablePadding>
                    <ListItemButton>
                        <ListItemIcon>
                            <Avatar>
                                <StarIcon />
                            </Avatar>
                        </ListItemIcon>
                        <ListItemText primary={item.name + item.surname} />
                    </ListItemButton>
                </ListItem>
            ))}
        </div>
    );
}