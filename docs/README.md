# Android application security

## How to test Android application security?

### Decompile APK file

* Get APK file
* Use dex to Java decompiler
* Navigate through sources

#### What to search?

* Embedded SQLite databases
* URL thanks to the following regex:

```shell
grep -REo '(http|https)://[^/"]+' . | grep -v 'Binary\|retrofit2\|android.com'
```

### Use a rooted device or a simulator

#### What to search?

* Access to SQLite database
* Access to SharedPreferences
* Access to saved files

### Capture HTTP traffic

* [Wireshark](https://www.wireshark.org/)
* [Fiddler](http://www.telerik.com/fiddler)

#### What to search?

* URIs
* HTTP Message Body

## A relevant tool: [QARK](https://github.com/linkedin/qark)

- decompile APK file
- spots several Android application vulnerabilities
- generates a final report

## Android specific things

### Code obfuscation: [Proguard](https://developer.android.com/studio/build/shrink-code.html), [DexGuard](https://www.guardsquare.com/en/dexguard)

### Preferences: [Hawk](https://github.com/orhanobut/hawk)

### SQLite databases: [SQLCipher](https://github.com/sqlcipher/android-database-sqlcipher)

- [requery](https://github.com/requery/requery/wiki/Android)

```java
new SqlCipherDatabaseSource(context, Models.DEFAULT, "db_name.sqlite", "db_password", DB_VERSION);
```

#### See also

- [DbFlow](https://github.com/Raizlabs/DBFlow)

### URL storage

* Encrypt URL related things
    - see [AESCrypt-Android](https://github.com/scottyab/AESCrypt-Android)

For example, run the following code:

```java
String password = "android_starter_alt";
String message = "https://api.github.com";
String encryptedMsg = AESCrypt.encrypt(password, message);
```
And the `encryptedMsg` variable contains the value to store in source file and to decrypt at runtime.

## Server-side

- More secure exchange format? [protobuf](https://github.com/google/protobuf)?
- Useful tool to avoid spam? [fail2Ban](http://www.fail2ban.org/wiki/index.php/Main_Page)?
- Cipher URL segment path (no "/users/12" but a hash)? Need a retrofit interceptor to cipher
- Encrypt bodies with public-key cryptography?

## Resources

- [android-security-awesome](https://github.com/ashishb/android-security-awesome)
- <https://rammic.github.io/2015/07/28/hiding-secrets-in-android-apps/>

## Other

- [ ] [Androwarn](https://github.com/maaaaz/androwarn/)?
- [ ] [devknox](https://devknox.io/)?
- [ ] [Inspeckage](https://github.com/ac-pm/Inspeckage)?
