package com.kali_corporation.freeonlinetranslation.nt.tool.hekp;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class NetHelper {
    public static String key = "98h831nd89ksz812";

    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {

            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {

            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return Base64.encodeToString(encrypted, Base64.NO_WRAP);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {

                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {

                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decode(sSrc.getBytes(), Base64.NO_WRAP);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }


    public static String enByAESDES(String sSrc, String sKey){
        // 判断Key是否正确
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        try {
            String enAES = Encrypt(sSrc,sKey);
            String enDES = NtHelper.eEcb(sKey,enAES);
            return enDES;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String deByAESDES(String sSrc, String sKey){
        // 判断Key是否正确
        if (sKey == null) {
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            return null;
        }
        try {

            String deDES = NtHelper.dEcb(sKey,sSrc);
            if (deDES.equals("")){
                String deAES = Decrypt(sSrc,sKey);
                return deAES;
            }
            String deAES = Decrypt(deDES,sKey);
            return deAES;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
