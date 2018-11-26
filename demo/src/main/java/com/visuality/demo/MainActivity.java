package com.visuality.demo;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;

import com.visuality.convenience.Convenience;
import com.visuality.convenience.PermissionManager;
import com.visuality.convenience.RequestResult;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] permissions = new String[] {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        Convenience.getPermissions().requestPermissionsIfNeeded(
                permissions,
                this,
                new PermissionManager.OnRequestListener() {
                    @Override
                    public void onResult(RequestResult requestResult) {
                    }
                }
        );
    }
}
