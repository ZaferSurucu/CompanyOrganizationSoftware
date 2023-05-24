package com.internship.deltasmartsoftware.events.model;

import com.internship.deltasmartsoftware.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class ResetPasswordCompleteEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public ResetPasswordCompleteEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
