package com.genenakagaki.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by gene on 11/14/16.
 */

public abstract class FetchJsonStringTask extends AsyncTask<Void, Void, String> {

    private static final String LOG_TAG = FetchJsonStringTask.class.getSimpleName();
    private static final boolean D = BuildConfig.APP_DEBUG;

    @Override
    protected String doInBackground(Void... params) {
        if (D) Log.d(LOG_TAG, "doInBackground");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        URL requestUrl = createRequestUrl();
        if (requestUrl == null) {
            if (D) Log.d(LOG_TAG, "requestUrl is null");
            return null;
        }

        try {
            // open connection
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream == null) {
                if (D) Log.d(LOG_TAG, "inputStream is null");
                return null;
            }

            StringBuilder sBuilder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }

            if (sBuilder.length() == 0) {
                if (D) Log.d(LOG_TAG, "inputStream is empty");
                return null;
            }

            jsonStr = sBuilder.toString();

        } catch (IOException e) {
            if (D) Log.d(LOG_TAG, "Error reading input stream", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    if (D) Log.d(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return jsonStr;
    }

    public abstract URL createRequestUrl();
}