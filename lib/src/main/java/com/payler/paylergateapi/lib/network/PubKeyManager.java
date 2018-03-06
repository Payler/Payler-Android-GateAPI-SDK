package com.payler.paylergateapi.lib.network;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class PubKeyManager implements X509TrustManager {

    private static String toHexString(byte abyte0[], int start) {
        StringBuffer stringbuffer = new StringBuffer();
        for (int i = start; i < abyte0.length; i++) {
            byte2hex(abyte0[i], stringbuffer);
            if (i < abyte0.length - 1)
                stringbuffer.append(" ");
        }

        return stringbuffer.toString();
    }

    private static void byte2hex(byte byte0, StringBuffer stringbuffer) {
        char ac[] = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'
        };
        int i = (byte0 & 0xf0) >> 4;
        int j = byte0 & 0xf;
        stringbuffer.append(ac[i]);
        stringbuffer.append(ac[j]);
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }

        if (!(chain.length > 0)) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
        }

        if (authType != null) {
            boolean validAuthType = authType.equalsIgnoreCase("RSA") || authType.equalsIgnoreCase("ECDHE_RSA");
            if (!validAuthType) {
                throw new CertificateException("checkServerTrusted: AuthType is not RSA or ECDHE_RSA");
            }
        }

        // Perform customary SSL/TLS checks
        try {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init((KeyStore) null);

            for (TrustManager trustManager : tmf.getTrustManagers()) {
                ((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
            }
        } catch (Exception e) {
            throw new CertificateException(e);
        }

        boolean foundCert = false;
        String thawteSerial = "02 5a 8a ef 19 6f 7e 0d 6c 21 04 b2 1a e6 70 2b";
        for (X509Certificate x509Certificate : chain) {
            String currentSerial = toHexString(x509Certificate.getSerialNumber().toByteArray(), 0);
            if (currentSerial.equalsIgnoreCase(thawteSerial)) {
                foundCert = true;
                break;
            }
        }
        if (!foundCert) {
            throw new CertificateException("checkServerTrusted: got unexpected CA Certificate Serial");
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}
