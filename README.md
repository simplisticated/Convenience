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
    implementation 'com.github.igormatyushkin014:Convenience:1.1'
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
PermissionManager permissionManager = PermissionManager.getInstance();
```

### Check

```java
String[] permissions = new String[] {
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
};

PermissionManager.getInstance().checkPermissions(
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

### Request

Ask user to accept permissions:

```java
PermissionManager.getInstance().requestPermissions(
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
PermissionManager.getInstance().requestPermissionsIfNeeded(
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
