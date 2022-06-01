# Androids Download-Manager

This Cordova-Plugin uses the Download-Manager of Android System. 
The Download ends up as a file in the default download folder and as a notification in the notification-area of the android system, while downloading and after download with the possibility to open the downloaded file.

This Plugin includes enqueueing downloads (including header support for e.g. adding authentication headers), requesting status of downloads and removing downloads.

## Installation

for npm hit following command:
```
npm install cordova-plugin-download-manager-android
```

## Functions
### Enqueue - queues a download to the download-manager of android system

Call following function to queue a download to the download-manager which will download the file as soon as possible:
`AndroidDownloadManager.enqueue(data: EnqueueDataInterface, success: SuccessCallback, error: ErrorCallback);`

The `EnqueueDataInterface` contains following data:
```ts
interface EnqueueDataInterface {
    url: string;
    fileName?: string;
    title?: string;
    description?: string;
    mimeType?: string;
    header?: [
        {
            header: string;
            value: string;
        }
    ]
}
```

The `SuccessCallback` is a function defined like so `(id: string) => void`

The `ErrorCallback` is a function defined like so `(error: string) => void`
#### Example

```
AndroidDownloadManager.enqueue(
    {
        url: "https://google.de/big-picture.png",
        fileName: "big-picture.png",
        mimeType: "image/png",
        header: [
            {
                header: "cookie",
                value: "token=1234jkl34589kldsf83i34mejrewei4"
            }
        ]
    },
    (data) => console.log(data),
    (error) => console.log(error)
);
```

### Query - request the status of a download

Call following function to query the status of a specific download enquede by the download-manager:
`AndroidDownloadManager.query(data: QueryDataInterface, success: SuccessCallback, error: ErrorCallback);`

The `QueryDataInterface` contains following data:
```ts
interface QueryDataInterface {
    id: string;
}
```

The `SuccessCallback` is a function defined like so `(data: DownloadStatusInterface) => void` with following `data` response:

```ts
interface DownloadStatusInterface {
    id: string;                     // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_ID
    title: string;                  // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_TITLE
    description: string;            // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_DESCRIPTION
    mimeType: string;               // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_MEDIA_TYPE
    localUri: string;               // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_MEDIAPROVIDER_URI
    mediaProviderUri: string;       // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_MEDIAPROVIDER_URI
    uri: string;                    // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_URI
    lastModifiedTimestamp: string;  // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_LAST_MODIFIED_TIMESTAMP
    status: string;                 // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_STATUS
    reason: string;                 // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_REASON
    bytesDownloadedSoFar: string;   // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_BYTES_DOWNLOADED_SO_FAR
    totalSizeBytes: string;         // https://developer.android.com/reference/android/app/DownloadManager#COLUMN_TOTAL_SIZE_BYTES
}
```

The `ErrorCallback` is a function defined like so `(error: string) => void`
#### Example

```
AndroidDownloadManager.query(
    {id: 51},
    (data) => console.log(data),
    (error) => console.log(error)
);
```

### Remove - removes a download

Call following function to remove a download from download folder and notification area:
`AndroidDownloadManager.remove(data: RemoveDataInterface, success: SuccessCallback, error: ErrorCallback);`

The `RemoveDataInterface` contains following data:
```ts
interface RemoveDataInterface {
    id: string;
}
```

The `SuccessCallback` is a function defined like so `(info: 'done' | 'failed') => void`

The `ErrorCallback` is a function defined like so `(error: string) => void`
#### Example

```
AndroidDownloadManager.remove(
    {id: 12},
    (data) => console.log(data),
    (error) => console.log(error)
);
```




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
