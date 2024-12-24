import * as React from 'react';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import Popper from '@mui/material/Popper';
import PopupState, { bindToggle, bindPopper } from 'material-ui-popup-state';
import Fade from '@mui/material/Fade';
import Paper from '@mui/material/Paper';
import {ClickAwayListener} from "@mui/material";

export default function PopperPopupState() {

    return (
        <PopupState variant="popper" popupId="demo-popup-popper">
            {(popupState) => (
                <div>
                    <Button variant="contained" {...bindToggle(popupState)}>
                        Toggle Popper
                    </Button>
                    <Popper {...bindPopper(popupState)} transition>
                        {({ TransitionProps }) => (
                            <ClickAwayListener onClickAway={popupState.close}>
                                <Fade {...TransitionProps} timeout={350}>
                                    <Paper>
                                        <Typography sx={{ p: 2 }}>The content of the Popper.</Typography>
                                    </Paper>
                                </Fade>
                            </ClickAwayListener>
                        )}
                    </Popper>
                </div>
            )}
        </PopupState>
    );
}
