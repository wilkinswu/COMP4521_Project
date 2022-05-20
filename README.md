# COMP4521 Warzone APP

Group 6: WU Yijian, WU Lijia, LI Pingjiang

## Dependency

Android 12

## Get started

1. Obtain [Google Map API Key](https://developers.google.com/maps) and store it in local.properties as following:

    ```
    MAPS_API_KEY=<You key in google platform>
    ```

2. Build backend server by using https://github.com/lwuar/warzone_backend

3. Copy credential.xml from [template.credential.xml](./app/src/main/res/values) and paste it in same directory, set ip address and port according to the backend.
4. Open project in Android Studio and sync for Gradle build.
5. Launch the app in simulator with Android 12 which has Google Play, or install it accordingly. This is because Google Map Service requires Google Play in simulator
