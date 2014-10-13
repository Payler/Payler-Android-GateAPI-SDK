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

    private static String PUB_KEY = "BC FF 1C 4E 45 4F 44 B6 B7 27 77 96 17 15 11 57 " +
            "54 0B 9C CD 2A 72 90 DB 76 26 AC 63 97 F9 70 17 " +
            "05 D3 1E 52 6C 6B 60 83 02 44 BE 3E BD 09 28 AA " +
            "98 D5 4C 93 36 18 0D 1C C2 AC E0 8A C0 DE 00 4A " +
            "3E 66 6D EC DA D9 D6 9D FB F1 22 00 93 E6 31 D7 " +
            "30 73 81 40 BF 5A 12 64 9E 0F 66 E7 12 3A 31 04 " +
            "FB 5D 18 05 4C F3 12 1F 35 71 10 04 F8 34 C3 99 " +
            "69 9B 44 7C D2 18 C9 84 8C 43 D0 34 AC AB 33 FD " +
            "2B 08 E2 98 98 EC 78 50 2B 08 20 54 65 E1 68 DF " +
            "E7 67 E3 E2 EB CC A3 FE C8 50 A6 1A 4E 67 C8 27 " +
            "F4 EB 3E B5 8F 35 DD EE A9 C6 F9 57 0F B1 AA 7B " +
            "ED 4B CE 54 E8 97 6D 15 A9 3D 3B 04 88 4B 19 7A " +
            "1E 68 BA B7 95 9F 2A CD 40 55 C6 41 3E 8F FE AA " +
            "29 0E 1B F7 C4 8B 2D 8C 34 96 24 ED 57 62 E4 04 " +
            "D2 5D D0 78 E8 99 18 47 44 98 5A 41 EA 27 64 E3 " +
            "67 D0 7F D0 0F 2C D4 1E 16 3F 28 F6 F6 AD 6F E1";

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
