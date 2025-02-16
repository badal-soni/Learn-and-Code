package com.itt.assignment3.constant;

public enum HttpMethod {

    GET,
    POST,
    PUT,
    DELETE;

    public static String valueOf(HttpMethod method) {
        return method.name();
    }

}
