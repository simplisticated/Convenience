package com.visuality.convenience;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;

public class PermissionManager {

    private static final PermissionManager sharedInstance = new PermissionManager();

    public static PermissionManager getInstance() {
        return sharedInstance;
    }

    public static final int REQUEST_CODE = Integer.MAX_VALUE;

    private OnRequestListener onRequestListener;

    private boolean waitingForResponse;

    public boolean isWaitingForResponse() {
        return this.waitingForResponse;
    }

    private boolean enabled;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private PermissionManager() {
        this.waitingForResponse = false;
        this.enabled = true;
    }

    public void checkPermissions(
            String[] permissions,
            Activity activity,
            final OnCheckListener listener
    ) {
        if (!this.enabled) {
            return;
        }

        ArrayList<String> allowedPermissionsList = new ArrayList<>();
        ArrayList<String> blockedPermissionsList = new ArrayList<>();

        for (String permission : permissions) {
            int checkResult = activity.checkSelfPermission(permission);
            boolean permissionAllowed = checkResult == PackageManager.PERMISSION_GRANTED;

            if (permissionAllowed) {
                allowedPermissionsList.add(permission);
            } else {
                blockedPermissionsList.add(permission);
            }
        }

        String[] allowedPermissionsArray = allowedPermissionsList.toArray(
                new String[] {}
        );
        String[] blockedPermissionsArray = blockedPermissionsList.toArray(
                new String[] {}
        );

        if (listener != null) {
            listener.onResult(
                    allowedPermissionsArray,
                    blockedPermissionsArray
            );
        }
    }

    public void requestPermissions(
            String[] permissions,
            Activity activity,
            final OnRequestListener listener
    ) {
        if (!this.enabled) {
            return;
        }

        this.onRequestListener = listener;
        activity.requestPermissions(
                permissions,
                REQUEST_CODE
        );
        this.waitingForResponse = true;
    }

    public void requestPermissionsIfNeeded(
            String[] permissions,
            final Activity activity,
            final OnRequestListener listener
    ) {
        if (listener == null) {
            return;
        }

        if (!this.enabled) {
            return;
        }

        this.checkPermissions(
                permissions,
                activity,
                new OnCheckListener() {
                    @Override
                    public void onResult(String[] allowedPermissions, String[] blockedPermissions) {
                        if (blockedPermissions.length == 0) {
                            listener.onResult(
                                    allowedPermissions,
                                    blockedPermissions
                            );
                        } else {
                            final ArrayList<String> allowedPermissionsList = new ArrayList<>();
                            allowedPermissionsList.addAll(
                                    Arrays.asList(
                                            allowedPermissions
                                    )
                            );

                            PermissionManager.this.requestPermissions(
                                    blockedPermissions,
                                    activity,
                                    new OnRequestListener() {
                                        @Override
                                        public void onResult(String[] allowedPermissions, String[] blockedPermissions) {
                                            allowedPermissionsList.addAll(
                                                    Arrays.asList(
                                                            allowedPermissions
                                                    )
                                            );

                                            String[] allowedPermissionsArray = allowedPermissionsList.toArray(
                                                    new String[] {}
                                            );
                                            String[] blockedPermissionsArray = blockedPermissions;

                                            listener.onResult(
                                                    allowedPermissionsArray,
                                                    blockedPermissionsArray
                                            );
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }

    public boolean onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] results
    ) {
        if (requestCode != REQUEST_CODE) {
            return false;
        }

        this.waitingForResponse = false;

        if (this.onRequestListener == null) {
            return false;
        }

        ArrayList<String> allowedPermissionsList = new ArrayList<>();
        ArrayList<String> blockedPermissionsList = new ArrayList<>();

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int result = results[i];
            boolean permissionAllowed = result == PackageManager.PERMISSION_GRANTED;

            if (permissionAllowed) {
                allowedPermissionsList.add(permission);
            } else {
                blockedPermissionsList.add(permission);
            }
        }

        String[] allowedPermissionsArray = allowedPermissionsList.toArray(
                new String[] {}
        );
        String[] blockedPermissionsArray = blockedPermissionsList.toArray(
                new String[] {}
        );

        this.onRequestListener.onResult(
                allowedPermissionsArray,
                blockedPermissionsArray
        );
        this.onRequestListener = null;

        return true;
    }

    public static abstract class OnCheckListener {
        public abstract void onResult(
                String[] allowedPermissions,
                String[] blockedPermissions
        );
    }

    public static abstract class OnRequestListener {
        public abstract void onResult(
                String[] allowedPermissions,
                String[] blockedPermissions
        );
    }
}
