# Android application security

* Use QARK

Go in the decompiled path and execute:

```shell
grep -REo '(http|https)://[^/"]+' . | grep -v 'Binary\|retrofit2\|android.com'
```

To find URLs in the APK file.

## Tools

- [QARK](https://github.com/linkedin/qark)
- [Androwarn](https://github.com/maaaaz/androwarn/)?
- [devknox](https://devknox.io/)?
- [Inspeckage](https://github.com/ac-pm/Inspeckage)?

## Android specific things

### Proguard, [DexGuard](https://www.guardsquare.com/en/dexguard)

### Preferences: [Hawk](https://github.com/orhanobut/hawk)

### SQLite: [SQLCipher](https://github.com/sqlcipher/android-database-sqlcipher)

- DbFlow
- requery

### URL storage

## Server-side

- More secure exchange format? protobuf
- Useful tool to avoid spam? fail2Ban
- What about cipher URL segment path (no "/users/12" but a hash)? Need a retrofit interceptor to cipher

## Libraries

- [AESCrypt-Android](https://github.com/scottyab/AESCrypt-Android)

## Resources

- [android-security-awesome](https://github.com/ashishb/android-security-awesome)
- https://rammic.github.io/2015/07/28/hiding-secrets-in-android-apps/