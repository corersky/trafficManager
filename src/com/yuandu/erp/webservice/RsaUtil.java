package com.yuandu.erp.webservice;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RsaUtil {

	public static void main(String... args) {
		RsaUtil rsa = RsaUtil.create();
		String pubKey = rsa.getPublicKey();
		// 原文
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < 40; i++) {
			res.append("测试");
		}
		System.out.println("原文对比:" + res.toString());
		System.out.println("------------");
		String enStr = rsa.encodeByPublicKey(res.toString(), pubKey);
		System.out.println("公钥加密:" + enStr);
	}

	public static final String KEY_ALGORITHM = "RSA";
	public static final String split = " ";// 分隔符
	public static final int max = 117;// 加密分段长度//不可超过117

	private static RsaUtil me;

	private RsaUtil() {}// 单例

	public static RsaUtil create() {
		if (me == null) {
			me = new RsaUtil();
		}
		// 生成公钥、私钥
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			kpg.initialize(1024);
			KeyPair kp = kpg.generateKeyPair();
			me.publicKey = (RSAPublicKey) kp.getPublic();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return me;
	}

	private RSAPublicKey publicKey;

	/** 获取公钥 */
	public String getPublicKey() {
		return parseByte2HexStr(publicKey.getEncoded());
	}

	/** 加密-公钥 */
	public String encodeByPublicKey(String res, String key) {
		byte[] resBytes = res.getBytes();
		byte[] keyBytes = parseHexStr2Byte(key);// 先把公钥转为2进制
		StringBuffer result = new StringBuffer();// 结果
		// 如果超过了100个字节就分段
		if (keyBytes.length <= max) {// 不超过直接返回即可
			return encodePub(resBytes, keyBytes);
		} else {
			int size = resBytes.length / max
					+ (resBytes.length % max > 0 ? 1 : 0);
			for (int i = 0; i < size; i++) {
				int len = i == size - 1 ? resBytes.length % max : max;
				byte[] bs = new byte[len];// 临时数组
				System.arraycopy(resBytes, i * max, bs, 0, len);
				result.append(encodePub(bs, keyBytes));
				if (i != size - 1)
					result.append(split);
			}
			return result.toString();
		}
	}


	/** 将二进制转换成16进制 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/** 将16进制转换为二进制 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	/** 加密-公钥-无分段 */
	private String encodePub(byte[] res, byte[] keyBytes) {
		X509EncodedKeySpec x5 = new X509EncodedKeySpec(keyBytes);// 用2进制的公钥生成x509
		try {
			KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
			Key pubKey = kf.generatePublic(x5);// 用KeyFactory把x509生成公钥pubKey
			Cipher cp = Cipher.getInstance(kf.getAlgorithm());// 生成相应的Cipher
			cp.init(Cipher.ENCRYPT_MODE, pubKey);// 给cipher初始化为加密模式，以及传入公钥pubKey
			return parseByte2HexStr(cp.doFinal(res));// 以16进制的字符串返回
		} catch (Exception e) {
			System.out.println("公钥加密失败");
			e.printStackTrace();
		}
		return null;
	}

}