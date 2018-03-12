package com.oxy.easilyhttpenginetest.net;


import com.oxy.easilyhttpengine.IParamsTransformer;
import com.oxy.easilyhttpengine.IResponseTransformer;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesCipher implements IResponseTransformer,IParamsTransformer {
	//填写您的加密密钥
	private byte[] raw;

	public AesCipher(String key){
		try {
			raw = key.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2),
					16);
		}
		return b;
	}

	public static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	@Override
	public String transformResponse(String response) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encrypted1 = hex2byte(response);
		byte[] original = cipher.doFinal(encrypted1);
		return new String(original,"utf-8");
	}

	@Override
	public Map<String, String> transformParams(Map<String, String> params) throws Exception{
		String text = params.get("data");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(text.getBytes());
		text = byte2hex(encrypted).toUpperCase();
		params.put("data",text);
		return params;
	}
}
