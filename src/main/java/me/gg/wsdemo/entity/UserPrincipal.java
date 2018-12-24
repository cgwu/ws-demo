package me.gg.wsdemo.entity;

import lombok.ToString;

import java.security.Principal;

/**
 * Created by sam on 18-12-13.
 */
@ToString
public class UserPrincipal implements Principal {
    private final String name;
    private final String token;

    public UserPrincipal(String name, String token){
        this.name = name;
        this.token = token;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}
