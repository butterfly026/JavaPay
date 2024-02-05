package com.city.adminpermission.exception;

public class UserNotFoundException extends Exception {
    private static final long serialVersionUID = 2540766252483785192L;

    public UserNotFoundException(String errmsg) {
        super(errmsg);
    }
}
