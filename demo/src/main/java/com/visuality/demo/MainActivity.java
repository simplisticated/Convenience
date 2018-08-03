package com.visuality.demo;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;

import com.visuality.convenience.PermissionManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        
        PermissionManager.getInstance().checkPermissions(
                permissions,
                this,
                new PermissionManager.OnCheckListener() {
                    @Override
                    public void onResult(String[] allowedPermissions, String[] blockedPermissions) {
                    }
                }
        );
    }
}
