package org.apache.cordova;

/**
* A phonegap plugin that converts a Base64 String to a PNG file.
*
* @author mcaesar
* @license MIT.
*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.cordova.api.Plugin;
import org.apache.cordova.api.PluginResult;
import org.json.JSONArray;

import android.os.Environment;
import java.io.*;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Base64;

public class Base64ToPNG extends Plugin {

    @Override
    public PluginResult execute(String action, JSONArray args, String callbackId) {

        if (!action.equals("saveImage")) {
            return new PluginResult(PluginResult.Status.INVALID_ACTION);
        }

        try {
            JSONObject obj = new JSONObject();

            String b64String = "";
            if (b64String.startsWith("data:image")) {
                b64String = args.getString(0).substring(21);
            } else {
                b64String = args.getString(0);
            }
            JSONObject params = args.getJSONObject(1);

            //Optional parameter
            String filename = params.has("filename")
                    ? params.getString("filename")
                    : "b64Image_" + System.currentTimeMillis() + ".png";

            String folder = params.has("folder")
                    ? params.getString("folder")
                    : Environment.getExternalStorageDirectory() + "/Pictures";

            Boolean overwrite = params.has("overwrite") 
                    ? params.getBoolean("overwrite") 
                    : false;

            return this.saveImage(b64String, filename, folder, overwrite, callbackId);

        } catch (JSONException e) {
            e.printStackTrace();
            return new PluginResult(PluginResult.Status.JSON_EXCEPTION, e.getMessage());
        } catch (InterruptedException e) {            
            e.printStackTrace();
            return new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }

    }

    private PluginResult saveImage(String b64String, String fileName, String dirName, Boolean overwrite, String callbackId) throws InterruptedException, JSONException {

        try {
            JSONObject obj = new JSONObject();
            obj.put("filename", dirName + "/" + fileName);

            //Directory and File
            File dir = new File(dirName);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dirName, fileName);

            //Avoid overwriting a file
            if (!overwrite && file.exists()) {
                obj.put("message", "File already exists!");
                return new PluginResult(PluginResult.Status.OK, obj);
            }

            //Decode Base64 back to Binary format
            byte[] decodedBytes = Base64.decode(b64String.getBytes(), Base64.DEFAULT);

            //Save Binary file to phone
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            fOut.write(decodedBytes);
            fOut.close();


            obj.put("message", "Saved successfully!");
            return new PluginResult(PluginResult.Status.OK, obj);
            //return new PluginResult(PluginResult.Status.OK, "Saved successfully!");

        } catch (FileNotFoundException e) {
            return new PluginResult(PluginResult.Status.ERROR, "File not Found!");
        } catch (IOException e) {
            return new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
    }
}