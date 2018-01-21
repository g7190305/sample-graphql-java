package com.yahoo.graphql.sample;

import com.coxautodev.graphql.tools.GraphQLResolver;

public class SigninResolver implements GraphQLResolver<SigninPayload> {

    public User user(SigninPayload payload) {
        return payload.getUser();
    }
}