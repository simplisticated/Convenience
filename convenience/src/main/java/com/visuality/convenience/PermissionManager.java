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
        CheckResult checkResult = new CheckResult(
                allowedPermissionsArray,
                blockedPermissionsArray
        );

        if (listener != null) {
            listener.onResult(
                    checkResult
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
        if (!this.enabled) {
            return;
        }

        this.checkPermissions(
                permissions,
                activity,
                new OnCheckListener() {
                    @Override
                    public void onResult(CheckResult checkResult) {
                        if (checkResult.getBlockedPermissions().length == 0) {
                            RequestResult requestResult = new RequestResult(
                                    checkResult.getAllowedPermissions(),
                                    checkResult.getBlockedPermissions()
                            );

                            if (listener != null) {
                                listener.onResult(
                                        requestResult
                                );
                            }
                        } else {
                            final ArrayList<String> allowedPermissionsList = new ArrayList<>();
                            allowedPermissionsList.addAll(
                                    Arrays.asList(
                                            checkResult.getAllowedPermissions()
                                    )
                            );

                            PermissionManager.this.requestPermissions(
                                    checkResult.getBlockedPermissions(),
                                    activity,
                                    new OnRequestListener() {
                                        @Override
                                        public void onResult(RequestResult result) {
                                            allowedPermissionsList.addAll(
                                                    Arrays.asList(
                                                            result.getAllowedPermissions()
                                                    )
                                            );

                                            String[] allowedPermissionsArray = allowedPermissionsList.toArray(
                                                    new String[]{}
                                            );
                                            String[] blockedPermissionsArray = result.getBlockedPermissions();

                                            RequestResult requestResult = new RequestResult(
                                                    result.getAllowedPermissions(),
                                                    result.getBlockedPermissions()
                                            );

                                            if (listener != null) {
                                                listener.onResult(
                                                        requestResult
                                                );
                                            }
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
        RequestResult requestResult = new RequestResult(
                allowedPermissionsArray,
                blockedPermissionsArray
        );

        this.onRequestListener.onResult(
                requestResult
        );
        this.onRequestListener = null;

        return true;
    }

    public interface OnCheckListener {
        void onResult(
                CheckResult checkResult
        );
    }

    public interface OnRequestListener {
        void onResult(
                RequestResult requestResult
        );
    }
}
