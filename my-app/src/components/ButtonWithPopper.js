import * as React from 'react';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Popper from '@mui/material/Popper';
import PopupState, { bindToggle, bindPopper } from 'material-ui-popup-state';
import Fade from '@mui/material/Fade';
import Paper from '@mui/material/Paper';
import {ClickAwayListener} from "@mui/material";
import Box from "@mui/material/Box";

export default function ButtonWithPopper({buttonLabel,popperContent, onClick}) {
    const [content, setContent] = React.useState("Click to send request");
    const handleClick = async () => {
        setContent("Sending request...");
        if (onClick) {
            const result = await onClick();
            setContent(result);
        }
    };

    return (
        <PopupState variant="popper" popupId={buttonLabel}>
            {(popupState) => (
                <Box width="100%">
                    <Button
                        variant="outlined"
                        {...bindToggle(popupState)}
                        fullWidth
                        onClick={(e) => {
                            onClick(e);
                            popupState.toggle(e);
                        }}
                    >
                        {buttonLabel}
                    </Button>
                    <Popper {...bindPopper(popupState)} transition>
                        {({ TransitionProps }) => (
                            <ClickAwayListener onClickAway={popupState.close}>
                                <Fade {...TransitionProps} timeout={350}>
                                    <Paper>
                                        <Typography sx={{ p: 2 }}>{popperContent}</Typography>
                                    </Paper>
                                </Fade>
                            </ClickAwayListener>
                        )}
                    </Popper>
                </Box>
            )}
        </PopupState>
    );
}
