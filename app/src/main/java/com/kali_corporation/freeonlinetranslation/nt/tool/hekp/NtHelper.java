package com.kali_corporation.freeonlinetranslation.nt.tool.hekp;

import android.util.Base64;

import com.kali_corporation.freeonlinetranslation.nt.tool.Constant;
import com.kali_corporation.freeonlinetranslation.nt.tool.NtManager;
import com.kali_corporation.freeonlinetranslation.nt.tool.sp.SPUtil;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class NtHelper {
    public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
    private static final String DES_IV = "98365612";
    public static final String DEF_KEY= "98h831nd89ksz8129m973hjz73gzi763";
    private static String encode(String key, byte[] data) throws Exception {
        try {
//            byte[] ivbyte = {1, 2, 3, 4, 5, 6, 7, 8};
            byte[] ivbyte = DES_IV.getBytes();
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
// key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            IvParameterSpec iv = new IvParameterSpec(ivbyte);
            AlgorithmParameterSpec paramSpec = iv;
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
            byte[] bytes = cipher.doFinal(data);
            String res = Base64.encodeToString(bytes, Base64.NO_WRAP);
            return res;


        } catch (Exception e) {
            throw new Exception(e);
        }
    }
//
//    private static byte[] decode(String key, byte[] data) throws Exception {
//        try {
//            DESKeySpec dks = new DESKeySpec(key.getBytes());
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//// key的长度不能够小于8位字节
//            Key secretKey = keyFactory.generateSecret(dks);
//            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
//            byte[] ivbyte = DES_IV.getBytes();
//            IvParameterSpec iv = new IvParameterSpec(ivbyte);
//            AlgorithmParameterSpec paramSpec = iv;
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
//            return cipher.doFinal(data);
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//    }

    public static String e(String data){
        return e("3h8dj38sty7j3aq4",data);
    }

//    public static String d(String data){
//        return d("3h8dj38sty7j3aq4",data);
//    }

    public static String e(String key, String data){
        if (key==null){
//            key = DEF_KEY;
            key = (String) SPUtil.getInstance(NtManager.app.getApplicationContext()).readData(Constant.PK,"");
            if (key.equals("")){
                return null;
            }
        }
        try {
            return encode(key, data.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String d(String key, String data) {
//        if (key==null){
//            key = DEF_KEY;
//        }
//        byte[] datas;
//        String value = null;
//        try {
//
//            datas = decode(key, Base64.decode(data.getBytes(), Base64.NO_WRAP));
//
//            value = new String(datas);
//        } catch (Exception e) {
//            value = "";
//        }
//        return value;
//    }

    public static String eEcb(String key, String data) {
        try {
            key = key.substring(0,8)+"1c4b6a1s";
            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] bytes = cipher.doFinal(data.getBytes());

            return Base64.encodeToString(bytes, Base64.NO_WRAP);

        } catch (Exception e) {
            return "";
        }
    }

    public static String dEcb(String key, String data) {
        try {
            key = key.substring(0,8)+"1c4b6a1s";

            byte[] datas = Base64.decode(data.getBytes(), Base64.NO_WRAP);

            DESKeySpec dks = new DESKeySpec(key.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // key的长度不能够小于8位字节
            Key secretKey = keyFactory.generateSecret(dks);
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(datas));
        } catch (Exception e) {
            return "";
        }
    }
}
