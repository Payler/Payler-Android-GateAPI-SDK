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

    private static final String PUB_KEY_2018 = "" +
            "ab e2 e1 2d e0 c9 1a 65 6b 9f 35 19 eb ce b4 2f " +
            "46 6f b4 e8 e7 88 b8 0d a6 6d bf 9f c8 5b 60 a5 " +
            "12 3c fd 0f 97 e0 b4 28 42 c4 f7 d7 c7 00 c0 ae " +
            "73 36 c0 28 4e 3c 54 ca 23 58 35 c3 59 b3 a2 a8 " +
            "34 c3 0a d1 ca 3b b9 f8 bf 47 99 2d fd 5b ba a5 " +
            "00 a7 70 57 6f 8c a5 3d de 3d 9c df d1 f4 27 ed " +
            "99 c8 9e 7a 73 7b e8 ed e6 1a cc 96 c9 6b f9 57 " +
            "0e d9 53 89 39 54 eb 4d b4 0c 4d 8c b5 05 28 b3 " +
            "a3 75 8a 35 9e 54 1e bf 4a 8f fe 2f a3 2b 29 e6 " +
            "fa 58 07 8b fe 43 9d f9 bf a6 10 8a 4b 66 f8 c0 " +
            "57 a0 b3 17 f1 12 e4 71 79 4c f5 e1 02 72 90 a3 " +
            "79 50 af 3e 41 6f 3a 94 f5 d7 61 b7 11 93 12 6f " +
            "1c 61 0a 5f 16 79 46 a2 0d 7d 00 d0 12 8a e2 a7 " +
            "0b 3b 50 2b 0d 3c 45 a0 4a ab 4d 62 07 a0 ce 36 " +
            "ab a4 22 a3 a6 ee 9d 7d 7f 3b 1e 33 85 44 27 2d " +
            "74 06 cd d5 d1 b8 56 9f ab e6 f6 c6 c2 89 18 b3 " +
            "70 3d b5 46 bb 5b 75 55 fe 71 7d 57 8e 44 2d b1 " +
            "1b bc cb cf 14 9d 4c cb be 7e fa 3d bc 56 d9 b1 " +
            "e1 11 23 7a ce 18 fb cf cf ea 79 8d 88 2a 56 26 " +
            "36 1c 46 5e 88 ba ad 0a 17 3c e7 fe e4 cc 20 3f " +
            "d4 ec 8f 24 39 87 57 c3 4d 36 3c 2d 67 f2 9f 74 " +
            "82 e0 c9 35 fa 3e 88 67 ff 27 14 fb b3 d4 b8 d2 " +
            "1b a0 d5 21 8e 6f 71 2a 40 7d e5 d3 58 14 24 39 " +
            "cb ee fb dc 1e 7b e8 b1 f6 ec 7d 9b ed 15 bc 75 " +
            "b7 37 e7 ec 74 b0 90 ca bb 7e b1 1b cb f9 03 28 " +
            "c4 cb 6a 84 ae c8 01 e4 0e f4 8d 9b ea e9 1a 31 " +
            "6e 51 3a df 52 de f0 f4 da ff be 7f 17 bf 00 19 " +
            "05 6f 26 29 4b 4c ef 9f c5 b2 b5 7e f4 d9 16 91 " +
            "cf 45 6b cf 32 2a 7f d8 78 84 cd 10 bc b5 b3 25 " +
            "96 45 f7 c3 21 84 22 b0 4a 1b 4f 26 a1 9e 7f e7 " +
            "bd fa f2 32 56 0d a4 56 ff 52 8c f1 9a 2a 28 a0 " +
            "af 16 a5 ab 91 9b 7b 46 d4 4a 80 07 71 0a b2 89";

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
        final boolean expected = PUB_KEY_2018.equalsIgnoreCase(hexPublicKey);
        if (!expected) {
            throw new CertificateException("checkServerTrusted: got unexpected public key: " + hexPublicKey);
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
