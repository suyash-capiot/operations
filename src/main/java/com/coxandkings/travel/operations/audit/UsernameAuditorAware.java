package com.coxandkings.travel.operations.audit;

import com.coxandkings.travel.operations.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

public class UsernameAuditorAware implements AuditorAware<String> {
    @Autowired
    private UserService userService;

    /*
        As of now, I have provided a hard coded user, but if you are
        using Spring Security, then use it to find the currently logged-in user.
     */
    @Override
    public String getCurrentAuditor() {
        return userService.getLoggedInUserId();
    }
}