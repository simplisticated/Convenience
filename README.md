<p align="center" >
	<img src="/images/logo_2048_600.png" alt="Convenience" title="Convenience">
</p>

<p align="center">
	<a href="https://http://www.android.com">
		<img src="https://img.shields.io/badge/android-23-green.svg?style=flat">
	</a>
	<a href="https://jitpack.io/#igormatyushkin014/Convenience">
		<img src="https://jitpack.io/v/igormatyushkin014/Convenience.svg">
	</a>
	<a href="https://tldrlegal.com/license/apache-license-2.0-(apache-2.0)">
		<img src="https://img.shields.io/badge/License-Apache 2.0-blue.svg?style=flat">
	</a>
</p>

## At a Glance

`Convenience` is a set of tools for making permissions management in Android significantly easier.

## How to Get Started

Add `jitpack.io` repository to your project:

```javascript
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

Then add `Convenience` to dependencies list:

```javascript
dependencies {
    implementation 'com.github.igormatyushkin014:Convenience:2.0.1'
}
```

## Requirements

* Android SDK 23 and later
* Android Studio 3.0 and later
* Java 7 and later

## Usage

### Permission Manager

All operations with permissions are done with `PermissionManager` instance which is available via:

```java
PermissionManager permissionManager = Convenience.getPermissions();
```

### Preparing Activity

Before using `Convenience` for permission request, you have to override activity's `onRequestPermissionsResult` method:

```java
@Override
public void onRequestPermissionsResult(
    int requestCode,
    @NonNull String[] permissions,
    @NonNull int[] grantResults
) {
    boolean handledByConvenience = Convenience.getPermissions().onRequestPermissionsResult(
        requestCode,
        permissions,
        grantResults
    );

    if (!handledByConvenience) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        );
    }
}
```

### Check Permissions

```java
String[] permissions = new String[] {
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
};

Convenience.getPermissions().check(
    permissions,
    activity,
    new PermissionManager.OnRequestListener() {
        @Override
        public void onResult(CheckResult checkResult) {
            if (requestResult.hasBlockedPermissions()) {
                // Handle blocked permissions
            } else {
                // All permissions available
            }
        }
    }
);
```

`CheckResult` instance includes both allowed and blocked permissions:

```java
String[] allowedPermissions = checkResult.getAllowedPermissions();
String[] blockedPermissions = checkResult.getBlockedPermissions();
```

### Request Permissions

Ask user to accept permissions:

```java
Convenience.getPermissions().request(
    permissions,
    activity,
    new PermissionManager.OnRequestListener() {
        @Override
        public void onResult(RequestResult requestResult) {
            if (requestResult.hasBlockedPermissions()) {
                // Handle blocked permissions
            } else {
                // All permissions available
            }
        }
    }
);
```

Also, you can request only those permissions from the list that are currently blocked. It means that, if the permissions list includes accepted permissions, they will not be requested again. User will be asked to accept other permissions that were not accepted before:

```java
Convenience.getPermissions().requestIfNeeded(
    permissions,
    activity,
    new PermissionManager.OnRequestListener() {
        @Override
        public void onResult(RequestResult requestResult) {
            if (requestResult.hasBlockedPermissions()) {
                // Handle blocked permissions
            } else {
                // All permissions available
            }
        }
    }
);
```

`RequestResult` instance includes both allowed and blocked permissions:

```java
String[] allowedPermissions = requestResult.getAllowedPermissions();
String[] blockedPermissions = requestResult.getBlockedPermissions();
```

## License

`Convenience` is available under the Apache 2.0 license. See the [LICENSE](./LICENSE) file for more info.
