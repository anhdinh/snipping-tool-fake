package com.andy.screen.shoot.event;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class ScreenOverlayCloseEvent {

    public ScreenOverlayCloseEvent(String data) {
        this.data = data;
    }

    public String data;

}
