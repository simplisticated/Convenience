package com.visuality.convenience;

public class Convenience {

    private static PermissionManager permissionManager;

    public static PermissionManager getPermissions() {
        if (permissionManager == null) {
            permissionManager = new PermissionManager();
        }

        return permissionManager;
    }
}
