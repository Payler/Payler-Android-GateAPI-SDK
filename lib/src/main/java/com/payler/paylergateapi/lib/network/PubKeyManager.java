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

    private static String PUB_KEY_2015 = "c5 28 81 e5 c7 06 67 9f 17 33 ae 8f 73 e9 7b 09 " +
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
            "a1 ac 2c 7c ac 5c 42 bb 63 18 c0 c6 bf bf 61 0b";

    private static String PUB_KEY_2016 = "" +
            "a4 82 93 1d 83 12 bd cf 38 e7 f5 14 65 74 " +
            "d7 5d 76 33 ff d7 a4 74 a1 f4 c0 e4 14 7b 99 " +
            "74 7a 32 f7 11 14 7f b0 fa 65 89 fb 23 9c 05 " +
            "7c e0 d5 71 74 0a d7 74 7a 14 c4 53 aa d1 f2 " +
            "28 22 00 22 92 d5 07 39 be 4c 19 b1 d5 cc 8d " +
            "e9 3d 03 5c 5c 1f 4c eb f6 73 7b 56 e2 01 7c " +
            "88 ba ff 0c d5 13 13 7e 66 e7 91 fb 17 3a c7 " +
            "88 20 b3 6e 05 04 72 de e4 d3 d7 4e f5 2a 0a " +
            "02 18 e6 48 3c 37 7c 33 64 74 33 40 5a 34 8f " +
            "71 e1 c5 e0 d9 b2 8b 7e 25 90 77 21 6c 0b 16 " +
            "4b c4 cb d9 3c 7b ce a0 18 0d 18 6f 9c e8 a7 " +
            "a7 6d 5c a7 88 a4 84 a4 2e 0d ac 9a 98 09 a6 " +
            "b4 a4 97 87 e7 40 53 60 94 c6 25 fd b4 8c 78 " +
            "38 d4 69 5f 37 f1 34 8a 16 6f b9 9c 62 30 b3 " +
            "8c 25 01 ca 74 95 3d d2 df df 3b ab 68 b9 54 " +
            "95 7f db 39 42 71 6b 17 0e 6d b1 57 a3 d4 3c " +
            "78 94 b5 61 bf c6 e0 a7 42 58 02 a7 eb 11 b9 " +
            "b0 e6 dc c2 51 16 3d ac 76 43 e3 e5 8f ee b9 " +
            "e1 8b a8 c8 a4 fd 0a 2d a8 c0 e8 cf ae 9a e8 " +
            "f1 6e 5d 0f 07 67 ec 85 49 bb 2b 45 5f f0 f1 " +
            "17 de 2a cd 87 74 eb 98 c3 57 72 28 a5 76 9f " +
            "b8 7d 4f 28 f1 fe 6d 81 15 cd d7 27 fe 43 bc " +
            "9d 83 de 36 75 42 b9 90 e4 3f 51 68 4d ce da " +
            "76 c4 ee 0e ef b0 e5 c6 53 54 a6 09 ef 52 d2 " +
            "d7 ae 4d 5c 9d 17 bd 50 4c c8 9e 20 04 31 4f " +
            "89 ff 76 a9 c9 f8 bb f4 14 69 06 4c bb 9b e1 " +
            "71 55 c0 cc 0b 46 c7 81 56 8a 8c dd cb b6 d9 " +
            "2b f4 fd 55 a0 88 5c 68 b1 70 35 23 e5 2a 64 " +
            "27 d5 cb 30 9d ea 7d f8 a1 3d f9 e9 67 63 b7 " +
            "71 6a 70 eb de 4d 8c f6 f4 1e b2 e1 80 d0 e4 " +
            "12 bc 0d 25 86 2a 9b 7a c1 0f 6e 10 e4 51 6b " +
            "cc c5 0d 87 be 89 54 f4 fd 34 4c a6 38 4c eb " +
            "d5 23 e3 42 e8 68 cb 33 70 43 fe cf 4d 0c 8c " +
            "f6 09 38 41 fb 81 50 09 df 9f 01 e2 84 6d de " +
            "f5 44 9b";

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
        final boolean expected = PUB_KEY_2015.equalsIgnoreCase(hexPublicKey) || PUB_KEY_2016.equalsIgnoreCase(hexPublicKey);
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
