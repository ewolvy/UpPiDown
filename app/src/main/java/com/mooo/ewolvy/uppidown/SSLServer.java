package com.mooo.ewolvy.uppidown;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import com.mooo.ewolvy.uppidown.AARemotes.*;

class SSLServer {
    // Constants
    static private final String LOG_TAG = "SSLServer";

    // Variables
    private String address;
    private int port;
    private String username;
    private String password;
    private String codeToSend;
    private AASuper currentAAState;
    private ImageView onOffSign;

    // Constructor
    SSLServer(String add, int po, String user, String pass) {
        address = add;
        port = po;
        username = user;
        password = pass;
    }

    private static HttpsURLConnection setUpHttpsConnection(String urlString, Context context) {
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            // My CRT file that I put in the assets folder
            // I got this file by following these steps:
            // * Go to https://littlesvr.ca using Firefox
            // * Click the padlock/More/Security/View Certificate/Details/Export
            // * Saved the file as littlesvr.crt (type X.509 Certificate (PEM))
            // The MainActivity.context is declared as:
            // public static Context context;
            // And initialized in MainActivity.onCreate() as:
            // MainActivity.context = getApplicationContext();
            InputStream caInput = context.getResources().openRawResource(R.raw.ewolvy);
            Certificate ca = cf.generateCertificate(caInput);
            // System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            // Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            // Create an SSLContext that uses our TrustManager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);

            // Tell the URLConnection to use a SocketFactory from our SSLContext
            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

            return urlConnection;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Failed to establish SSL connection to server: " + ex.toString());
            return null;
        }
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    void sendCode(String code, Context context, AASuper aaState, ImageView iv){
        codeToSend = code;
        currentAAState = aaState;
        doConnection connection = new doConnection();
        connection.execute(context);
        onOffSign = iv;
    }

    private class doConnection extends AsyncTask<Context, Void, String>{
        String fullAddress;
        Context currentContext;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            fullAddress = address;
            fullAddress = fullAddress + ":";
            fullAddress = fullAddress + port;
            fullAddress = fullAddress + "/";
            fullAddress = fullAddress + currentAAState.getServerPath();
            fullAddress = fullAddress + "/";
            fullAddress = fullAddress + codeToSend;
        }

        @Override
        protected String doInBackground(Context... contexts) {
            currentContext = contexts[0];
            HttpsURLConnection urlConnection = setUpHttpsConnection(fullAddress, currentContext);
            String jsonResponse = "";
            try {
                if (urlConnection != null){
                    urlConnection.setReadTimeout(10000 /* milliseconds */);
                    urlConnection.setConnectTimeout(15000 /* milliseconds */);
                    urlConnection.setRequestMethod("GET");
                    String userCredentials = username;
                    userCredentials = userCredentials + ":";
                    userCredentials = userCredentials + password;
                    String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), 0);
                    urlConnection.setRequestProperty ("Authorization", basicAuth);
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        try{
                            jsonResponse = readFromStream(inputStream);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "IO Exception: " + e.toString());
            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (codeToSend.equals(currentAAState.getPowerOff())) {
                if (result != null) {
                    if (onOffSign != null) {
                        onOffSign.setVisibility(View.INVISIBLE);
                    }
                    currentAAState.setOn(false);
                    Toast toast = Toast.makeText(currentContext, result, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(currentContext, currentContext.getString(R.string.connection_error), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else{
                if (result != null){
                    if (onOffSign != null) {
                        onOffSign.setVisibility(View.VISIBLE);
                    }
                    currentAAState.setOn(true);
                    Toast toast = Toast.makeText(currentContext, result, Toast.LENGTH_SHORT);
                    toast.show();
                }else{
                    Toast toast = Toast.makeText(currentContext, currentContext.getString(R.string.connection_error), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }
}