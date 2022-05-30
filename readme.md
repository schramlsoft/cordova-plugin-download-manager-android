# Androids Download-Manager

This Cordova-Plugin uses the Download-Manager of Android System. 
This Plugin includes header-support for urls which will be downloaded, progress- and ready-notifications, requesting status of downloads and removing downloads.

## Usage



## General Development
### Testing a Plugin during development
The simplest way to manually test a plugin during development is to create a Cordova app as usual and add the plugin with the `--link` option:

```sh
$ npm run cordova -- plugin add ../path/to/my/plugin/relative/to/project --link
```

This creates a symbolic link instead of copying the plugin files, which enables you to work on your plugin and then simply rebuild the app to use your changes.

#### For Windows
After adding the plugin to your project, you probably will need to install npm again `npm i` and afterwards you will remove platforms and plugins folder and add the platform again and build the app again.

to use your changes while develeping you will need to do following command:
```sh
$ rm -rf platforms plugins && npm run cordova -- platform add android && npm run cordova -- build android --packageType=apk
```
#### For Linux / Mac

Just rebuild the app

```sh
$ npm run cordova -- build android --packageType=apk
```

### Install your App on a device using adb

```sh
$ adb install -r platforms/android/app/build/outputs/apk/debug/app-debug.apk
```

### Debugging - Logging

For logging what is going on the device start logging with following command

```adb logcat > ./log.txt```
https://ourcodeworld.com/articles/read/295/how-to-debug-java-code-in-a-cordova-android-application-from-your-device-using-adb-in-windows

## Examples

For enqueue:
```
AndroidDownloadManager.enqueue(
    {
        url: "https://testza3.schramlsoft.de/api/v1.0/shop/download/order/cf5a1e18d9168576e98ca60521605bdb68e3d22a58058e233ae8b31026d5ef8fa323989b0dda55d639cbf55cf14d371e5b98023109ab2d41da8384f7ae1d210bEY1gNJ3x-wCS73wbZkTflDC0SLktGi77D2iGVHRDqnS1FSVeBQqP0zzof7w3-nNjz1tGZ__toq2hjIbfDiR5AKgJUnhPBZk63Zm-4h7WLAle2UNz3KqqyJW69VZAultd5FWukeuNTSu3c4Bnq41FbKIW1-bvOZ2IO3g",
        fileName: "Bestellauftrag.pdf",
        header: [
            {
                header: "cookie",
                value: "SessionID=0phtf6a8v3fpq50a5i6tu3alnd"
            },
            {
                header: "User-Agent",
                value: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.67 Safari/537.36"
            }
        ]
    },
    (data) => console.log(data),
    (error) => console.log(error)
);
```

For query a download process:
```
AndroidDownloadManager.query(
    {id: 51},
    (data) => console.log(data),
    (error) => console.log(error)
);
```

For removal:
```
AndroidDownloadManager.remove(
    {id: 12},
    (data) => console.log(data),
    (error) => console.log(error)
);
```
