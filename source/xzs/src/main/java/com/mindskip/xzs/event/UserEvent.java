package com.mindskip.xzs.event;

import com.mindskip.xzs.domain.UserEventLog;
import org.springframework.context.ApplicationEvent;

public class UserEvent extends ApplicationEvent {

    private final UserEventLog userEventLog;

    public UserEvent(final UserEventLog userEventLog) {
        super(userEventLog);
        this.userEventLog = userEventLog;
    }

    public UserEventLog getUserEventLog() {
        return userEventLog;
    }
}
