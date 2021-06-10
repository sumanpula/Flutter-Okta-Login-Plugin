package com.dnb.okta.flutter.signin;

import android.os.Bundle;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.flutter.embedding.android.FlutterActivity;


public class MainActivity extends FlutterActivity {
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            trustConnections(new URL("https://"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HttpsURLConnection trustConnections(URL url) throws Exception {
        HttpsURLConnection connection = null;
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };

        SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        connection = (HttpsURLConnection) url.openConnection();
        connection.setHostnameVerifier(hostnameVerifier);
        connection.setSSLSocketFactory(sslSocketFactory);

        return connection;
    }
}
