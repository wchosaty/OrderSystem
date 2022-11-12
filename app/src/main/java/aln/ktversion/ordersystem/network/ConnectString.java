package aln.ktversion.ordersystem.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import aln.ktversion.ordersystem.tool.LogHistory;

public class ConnectString implements Callable<String> {
    private static final String TAG = "TAG ConnectString";
    private String url,outString;

    @Override
    public String call() throws Exception {
        return getData();
    }

    private String getData() {
        HttpURLConnection httpConnection = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            httpConnection = (HttpURLConnection) new URL(url).openConnection();
            httpConnection.setDoInput(true);
            httpConnection.setDoInput(true);
            httpConnection.setChunkedStreamingMode(0);
            httpConnection.setUseCaches(false);
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("charset","UTF-8");
            try (BufferedWriter bw =
                         new BufferedWriter(new OutputStreamWriter(httpConnection.getOutputStream()));) {
                LogHistory.d(TAG, "outGsonString :" + outString);
                bw.write(outString);
            }
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader
                        (new InputStreamReader(httpConnection.getInputStream()));) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }
            } else {
                LogHistory.d(TAG, "responseCode :" + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
        }
        LogHistory.d(TAG,"backString :"+stringBuilder.toString());
        return stringBuilder.toString();
    }

    public ConnectString(String url, String outString) {
        this.url = url;
        this.outString = outString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOutString() {
        return outString;
    }

    public void setOutString(String outString) {
        this.outString = outString;
    }
}
