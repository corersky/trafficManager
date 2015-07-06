package com.yuandu.erp.webservice.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import com.yuandu.erp.common.utils.StringUtils;

public class EncrypRSA {
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * BASE64解密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static byte[] decryptBASE64(String key) throws Exception {
		return Base64.decodeBase64(key);
	}

	private static PublicKey codeToPublicKey(String publicKeyStr) throws Exception {
		byte[] publicKeyBytes = Base64.decodeBase64(publicKeyStr);
		// x.509,是x500那套网络协议（好像是目录协议吧）的一个子集，专门定义了在目录访问中需要身份认证的证书的格式。
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		return keyFactory.generatePublic(keySpec);
	}

	private static PrivateKey codeToPrivateKey(String privateKeyStr) throws Exception {
		byte[] privateKeyBytes = Base64.decodeBase64(privateKeyStr);
		// PKCS#8：描述私有密钥信息格式，该信息包括公开密钥算法的私有密钥以及可选的属性集等。
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey keyPrivate = keyFactory.generatePrivate(keySpec);
		return keyPrivate;
	}

	/**
	 * BASE64加密
	 * 
	 * @param key
	 * @return
	 * @throws Exception
	 */
	private static String encryptBASE64(byte[] key) throws Exception {
		return new String(Base64.encodeBase64(key));
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 * @param srcBytes
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String pulicKeyStr, String msg) throws Exception {
		if (StringUtils.isNotEmpty(pulicKeyStr)) {
			RSAPublicKey publicKey = (RSAPublicKey) codeToPublicKey(pulicKeyStr);
			// Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// 根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] resultBytes = cipher.doFinal(msg.getBytes());
			return encryptBASE64(resultBytes);
		}
		return null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 * @param srcBytes
	 * @return
	 * @throws Exception
	 */
	protected String decrypt(String privateKeyStr, String base4Msg)
			throws Exception {
		if (StringUtils.isNotEmpty(privateKeyStr)) {
			RSAPrivateKey privateKey = (RSAPrivateKey) codeToPrivateKey(privateKeyStr);
			// Cipher负责完成加密或解密工作，基于RSA
			Cipher cipher = Cipher.getInstance("RSA");
			// 根据公钥，对Cipher对象进行初始化
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] resultBytes = cipher.doFinal(decryptBASE64(base4Msg));
			return new String(resultBytes);
		}
		return null;
	}
	
	private EncrypRSA() {}// 单例
	
	private static EncrypRSA me;
	

	public static EncrypRSA create() {
		if (me == null) {
			me = new EncrypRSA();
		}
		return me;
	}
	

	/**
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws BadPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, Exception {
		EncrypRSA rsa = new EncrypRSA();
		String msg = "<span style='font-family: Arial, Helvetica, sans-serif;'>公钥和私钥</span>";

		String puKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDj32Ezgfp69lWU80n4BRq1h82tmJH16bmfXXdv4p+Fj2J00smZdijFTpbttudb02NY7c61HAAnBWX02NLyApnzAFQbYSPtymCnt99DCNEGfIg4tssOaqmcFUydaBOfb4KQldCrCkAz435fp3qQj57tzMF3ZfGIkdpqz38bzRIrQIDAQAB";
		String privKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIOPfYTOB+nr2VZTzSfgFGrWHza2YkfXpuZ9dd2/in4WPYnTSyZl2KMVOlu2251vTY1jtzrUcACcFZfTY0vICmfMAVBthI+3KYKe330MI0QZ8iDi2yw5qqZwVTJ1oE59vgpCV0KsKQDPjfl+nepCPnu3MwXdl8YiR2mrPfxvNEitAgMBAAECgYBW0KaEr6zbOU6XP/+es6jlg1zfruUWAYHakano5c/POn/rZmot8YFOxOBYy0cLEfq8NbQg9zTdwiIhqVXaGSx7tQDcobVz2tRNnXeGDjHspx/AmoTlPZXBsGYoNZssur+hfLYt9TSOhyzJFAwrgdmU0eApfrecLbg6dtgrFUdjIQJBAMnBhNRckSKZPWTEP68HKoLfRTs6nNqOJ1k8afV7Pe8uHNmll6Q25Hhj2hS7xA1xWs83jXSWQ4t9HAmzucKM6pkCQQCm7o1yOqxKuROkC+9LFEHuXzEAFcRszxwAxnymUTjYaV3P4mxFNx2gaRAndx2g+TN5ECadjXQK+z6VUAZkxs81AkBJDYs9Ia9jqbzzKaQtihi4foOg6J42/NX+l8N+IXvRiQ7lN/JqgX3EodMrQrnAbFUHRwQvPOGENm7ajHssmL2xAkBcA/Dp/1eLVNtzuLRtwTvahpQ/BMCibcN01fRNxUW9XM8+UQP3XzswedlSt8EYQ2VRHUr728YUa4uCyHrNwBzlAkEAyBDDSur8r3GnqbKL7LlXKuOLsCNgvgW2Gd4pJPO3CEYG5Ix68GUVy45F/+QCY3/3/aFjqzN65fbA3sRbmmmCpQ==";

		// 用公钥加密
		String base64Msg = rsa.encrypt(puKey, msg);

//		// 用私钥解密
		String decBytes = rsa.decrypt(privKey, base64Msg);

		System.out.println("明文是:" + msg);
		System.out.println("双重加密后是:" + base64Msg);
		System.out.println("解密后是:" + decBytes);
	}
}
