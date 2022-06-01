/*
 * Copyright (c) 2022 W. Schraml Softwarehaus GmbH
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.schramlsoft.cordova.android;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;


import android.database.Cursor;
import android.os.Environment;
import android.content.Context;
import android.net.Uri;
import android.app.DownloadManager;
import android.provider.Settings;

public class DownloadManagerCordova extends CordovaPlugin {
    private DownloadManager downloadManager;
    private long downloadID = -1L;

    /**
     * Constructor.
     */
    public DownloadManagerCordova() {
    }

    /**
     * Sets the context of the Command. This can then be used to do things like
     * get file paths associated with the Activity.
     *
     * @param cordova The context of the main Activity.
     * @param webView The CordovaWebView Cordova is running in.
     */
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);

        downloadManager = (DownloadManager) cordova.getActivity()
                .getApplication()
                .getApplicationContext()
                .getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * Executes the request and returns PluginResult.
     *
     * @param action          The action to execute.
     * @param args            JSONArry of arguments for the plugin.
     * @param callbackContext The callback id used when calling back into
     *                        JavaScript.
     * @return True if the action was valid, false if not.
     */
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if ("enqueue".equals(action)) {
                callbackContext.success(this.enqueue(args.getJSONObject(0)));
            } else if ("query".equals(action)) {
                callbackContext.success(this.query(args.getJSONObject(0)));
            } else if ("remove".equals(action)) {
                callbackContext.success(this.remove(args.getJSONObject(0)));
            } else {
                return false;
            }
            return true;
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            return false;
        }
        
    }

    /**
     * enqueue for starting a download
     *
     * @return
     */
    public String enqueue(JSONObject data) throws Exception {
        String url;
        String fileName;
        String title;
        String description;
        String mimeType;
        JSONArray headerArray;
        // get params
        try {
            url = data.getString("url");
            fileName = data.optString("fileName", FilenameUtils.getName(url));
            title = data.optString("title", fileName);
            description = data.optString("description", "Download");
            mimeType = data.optString("mimeType", "application/octet-stream");
            headerArray = data.optJSONArray("header") != null ? data.getJSONArray("header") : new JSONArray();
        } catch (Exception e) {
            throw new Exception("error: could not find params");
        }

        Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .mkdirs();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setTitle(title)
                .setDescription(description)
                .setMimeType(mimeType);

        try {
            int headerCount = headerArray.length();
            for(int i=0; i<headerCount; i++) {
                String header = headerArray.getJSONObject(i).getString("header");
                String value = headerArray.getJSONObject(i).getString("value");
                request.addRequestHeader(header, value);
            }
        } catch (Exception e) {
            throw new Exception("error: could not read header");
        }

        downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.

        return Long.toString(downloadID);
    }

    /**
     * query a download by id
     *
     * @return
     */
    public JSONArray query(JSONObject data) throws Exception {
        JSONArray result = new JSONArray();
        long[] ids = new long[1];
        try {
            ids[0] = Long.valueOf(data.getString("id"));
        } catch (Exception e) {
            throw new Exception("error: could not find id");
        }

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(ids);

        Cursor cursor = downloadManager.query(query);

        cursor.moveToFirst();
        try {
            do {
                JSONObject rowObject = new JSONObject();
                rowObject.put("id", cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_ID)));
                rowObject.put("title", cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)));
                rowObject.put("description", cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_DESCRIPTION)));
                rowObject.put("mimeType", cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE)));
                rowObject.put("localUri", cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)));
                rowObject.put("mediaProviderUri", cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIAPROVIDER_URI)));
                rowObject.put("uri", cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI)));
                rowObject.put("lastModifiedTimestamp", cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP)));
                rowObject.put("status", cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)));
                rowObject.put("reason", cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON)));
                rowObject.put("bytesDownloadedSoFar", cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
                rowObject.put("totalSizeBytes", cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)));
                result.put(rowObject);
            } while (cursor.moveToNext());

            return result;
        } catch (Exception e) {
            throw new Exception("error: could not find requested download");
        }
    }
    
    /**
     * remove a download by id
     *
     * @return
     */
    public String remove(JSONObject data) throws Exception {
        long[] ids = new long[1];
        try {
            ids[0] = Long.valueOf(data.getString("id"));
        } catch (Exception e) {
            throw new Exception("error: could not find id");
        }

        int removeCount = downloadManager.remove(ids);

        return removeCount == 1 ? "done" : "failed";
    }
}