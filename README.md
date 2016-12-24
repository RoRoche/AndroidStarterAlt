# AndroidStarterAlt
A sample Android app using the MVP architecture, using :
- [Dagger2](https://google.github.io/dagger/)
- [mosby](http://hannesdorfmann.com/mosby/)
- [retrofit2](https://square.github.io/retrofit/)
- [RxJava](https://github.com/ReactiveX/RxJava)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidStarterAlt-brightgreen.svg?style=flat)](https://android-arsenal.com/details/3/4933)

[![Build Status](https://travis-ci.org/RoRoche/AndroidStarterAlt.svg?branch=master)](https://travis-ci.org/RoRoche/AndroidStarterAlt)
[![Build Status](https://circleci.com/gh/RoRoche/AndroidStarterAlt.svg?style=shield&circle-token=e1392aa8f9f0e28e84fcbe56e7799aa0dad35142)](https://circleci.com/gh/RoRoche/AndroidStarterAlt)
[![Build Status](https://www.bitrise.io/app/30fed1131586f570.svg?token=m4Zm_PsnWHanpcQyojBX3A&branch=master)](https://www.bitrise.io/app/30fed1131586f570)
[![Code coverage](https://codecov.io/github/RoRoche/AndroidStarterAlt/coverage.svg?branch=master)](https://codecov.io/gh/RoRoche/AndroidStarterAlt)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3fd4615e71704f6cbbd01b8f82e7f0fc)](https://www.codacy.com/app/romain-rochegude_2/AndroidStarterAlt?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=RoRoche/AndroidStarterAlt&amp;utm_campaign=Badge_Grade)
[![codebeat badge](https://codebeat.co/badges/e20f9049-330c-4eb1-99dc-e3c4c7a711f7)](https://codebeat.co/projects/github-com-roroche-androidstarteralt)
[![Dependency Status](https://www.versioneye.com/user/projects/5818f5144304530b002f88b1/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/5818f5144304530b002f88b1)

Compared to the <https://github.com/RoRoche/AndroidStarter> repository, this one uses:

* [requery](https://github.com/requery/requery/) as persistence layer
* [EventBus](https://github.com/greenrobot/EventBus) as event bus layer
* [LoganSquare](https://github.com/bluelinelabs/LoganSquare) as JSON parsing layer
* [Conductor](https://github.com/bluelinelabs/Conductor) to build View-based Android application

It allowed me to write less code and delete some classes written in the <https://github.com/RoRoche/AndroidStarter> repository.

## TODO

- [ ] move from MVP to VIPER
