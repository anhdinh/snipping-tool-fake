package com.andy.screen.shoot.event;

import com.google.common.eventbus.EventBus;

public class AppEventBus {
    private static final EventBus INSTANCE = new EventBus();

    private AppEventBus() {}

    public static EventBus getInstance() {
        return INSTANCE;
    }
}
