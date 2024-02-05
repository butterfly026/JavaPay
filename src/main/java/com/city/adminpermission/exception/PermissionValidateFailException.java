package com.city.adminpermission.exception;

public class PermissionValidateFailException extends Exception {
    private static final long serialVersionUID = 2540766252483785192L;

    public PermissionValidateFailException(String errmsg) {
        super(errmsg);
    }
}
