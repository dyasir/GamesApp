package com.unity3d.player.http.custom;

import android.os.Build;

import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509ExtendedTrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 忽略证书校验
 */
public class SSLSocketClient {

    public static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                sslContext.init(null, getX509ExtendedTrustManager(), new SecureRandom());
            } else {
                sslContext.init(null, getX509TrustManager(), new SecureRandom());
            }
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static X509TrustManager[] getX509TrustManager() {
        X509TrustManager[] x509TrustManagers = new X509TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        return x509TrustManagers;
    }

    private static X509ExtendedTrustManager[] getX509ExtendedTrustManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            X509ExtendedTrustManager[] x509ExtendedTrustManagers = new X509ExtendedTrustManager[]{
                    new X509ExtendedTrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {

                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }};
            return x509ExtendedTrustManagers;
        }
        return null;
    }

    public static X509TrustManager getX509Trust() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    public static X509ExtendedTrustManager getX509Extended() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return new X509ExtendedTrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        } else {
            return null;
        }
    }

    public static HostnameVerifier getHostnameVerifier() {
        return (hostname, session) -> true;
    }
}
