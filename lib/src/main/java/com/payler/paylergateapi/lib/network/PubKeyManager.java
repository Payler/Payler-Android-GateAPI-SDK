package com.payler.paylergateapi.lib.network;

import java.math.BigInteger;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class PubKeyManager implements X509TrustManager {

    private static String PUB_KEY = "c5 28 81 e5 c7 06 67 9f 17 33 ae 8f 73 e9 7b 09 " +
            "26 1d 20 96 06 44 10 77 79 60 32 ff 8c d7 f9 2f " +
            "c8 6f 94 84 fb 1b eb 43 ad 8e 0e 61 58 8b 2b cd " +
            "24 6e 69 24 f7 f2 1a 4f e6 db c2 a2 8c 6a a3 ad " +
            "19 e0 df 08 06 e0 2d aa 3c 69 10 03 ad 00 96 34 " +
            "44 0d f1 ab 0f 96 59 79 8a 33 e3 29 e7 45 c0 bb " +
            "94 01 c3 f9 23 9e 1a b3 00 4d 74 91 b9 84 10 42 " +
            "66 ee cc 5f bd 37 a0 d3 bf eb 1d f8 53 61 3c 0b " +
            "d8 4d 71 05 53 f4 b7 29 ff eb e7 9a 13 30 2c b6 " +
            "8a 70 87 f0 76 ee 6b 05 0a c7 df 16 f3 96 54 83 " +
            "e2 08 dd c9 5a be ba dd 30 db fc d0 e0 83 ba 51 " +
            "e2 72 5f ea 40 03 51 4b 45 02 c0 cb cf 0b 97 89 " +
            "89 d7 d3 32 ff b5 6e 76 0b ea 63 ae 4b 82 79 0d " +
            "dd 64 ac 4b ea 7c 87 1d df 17 80 c0 a5 66 e6 3f " +
            "b6 19 75 98 ba 10 22 f2 89 ee 2c 92 6c 68 88 e8 " +
            "91 70 3a 9c 20 63 35 b2 1d ef 6b 95 96 f7 95 60 " +
            "d6 06 91 c3 60 ae 70 cd 72 2b d9 4e ec 61 0b f4 " +
            "f6 f9 51 77 f7 ae dc 0d 23 3d f7 d5 d7 d2 51 8d " +
            "67 ec 40 40 a6 a7 4a 1a 22 69 6e 52 a5 d6 c5 29 " +
            "03 0e 47 85 ee 91 98 ab 42 49 30 15 7c c9 22 94 " +
            "03 f9 cb 19 13 52 4d 49 cd 64 7e c3 53 c6 ae 9a " +
            "55 ff 98 b8 f7 ff 3c df f1 1e 46 24 7e e7 0d f0 " +
            "2c cb 66 fd e9 0d 51 a2 bd 99 7e 8d 61 dd 37 99 " +
            "e1 5c 33 5b c9 1c c8 e1 3e 64 36 99 a5 6a 0a 1f " +
            "c6 bc 24 a6 e9 ea e4 7d 12 49 70 e9 94 e0 99 c3 " +
            "17 5c 46 c7 76 b8 54 a1 3c 83 ce d5 44 21 d1 76 " +
            "5b 40 2a c4 de a7 67 21 6e 95 fe 76 e1 e6 44 41 " +
            "fe 86 9f 55 fa 8a 10 e1 03 1e aa 03 5d ec c8 e7 " +
            "98 20 14 40 78 b7 bb 2e bf c0 f0 2f ad 07 9c cf " +
            "e2 f7 de 7f 19 c5 b8 ad 20 d1 48 12 d3 45 3e 16 " +
            "ff 97 96 ae a9 b9 2c 21 9f 57 5c 88 9a 6a 1f d3 " +
            "a1 ac 2c 7c ac 5c 42 bb 63 18 c0 c6 bf bf 61 0b ";

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

        if(authType != null) {
            boolean validAuthType = authType.equalsIgnoreCase("RSA") || authType.equalsIgnoreCase("ECDHE_RSA");
            if(!validAuthType) {
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


        RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
        BigInteger modulus = pubkey.getModulus();
        String hexPublicKey = toHexString(modulus.toByteArray(), 1); // ignore first byte 0x00

        // Pin it!
        final boolean expected = PUB_KEY.equalsIgnoreCase(hexPublicKey);
        if (!expected) {
            throw new CertificateException("checkServerTrusted: Expected public key: "
                    + PUB_KEY + ", got public key:" + hexPublicKey);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    private static String getCertFingerPrint(String s, X509Certificate certificate)
            throws Exception {
        byte abyte0[] = certificate.getEncoded();
        MessageDigest messagedigest = MessageDigest.getInstance(s);
        byte abyte1[] = messagedigest.digest(abyte0);
        return toHexString(abyte1, 0);
    }

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
}
