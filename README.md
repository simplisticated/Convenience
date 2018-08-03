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

Check permissions:

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
            if (checkResult.getBlockedPermissions().length == 0) {
                // All permissions available
            } else {
                // Some permissions are blocked
            }
        }
    }
);
```

Request permissions:

```java
PermissionManager.getInstance().requestPermissions(
    permissions,
    activity,
    new PermissionManager.OnRequestListener() {
        @Override
        public void onResult(RequestResult requestResult) {
            if (requestResult.getBlockedPermissions().length == 0) {
                // All permissions available
            } else {
                // Some permissions are blocked
            }
        }
    }
);
```

Request only those permissions from the list that are currently blocked:

```java
PermissionManager.getInstance().requestPermissionsIfNeeded(
    permissions,
    activity,
    new PermissionManager.OnRequestListener() {
        @Override
        public void onResult(RequestResult requestResult) {
            if (requestResult.getBlockedPermissions().length == 0) {
                // All permissions available
            } else {
                // Some permissions are blocked
            }
        }
    }
);
```

## License

`Convenience` is available under the Apache 2.0 license. See the [LICENSE](./LICENSE) file for more info.
