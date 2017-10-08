package johnson.tools;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;


public class RSAUtil {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	private static final int KEY_SIZE = 2048;
	
	public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {
	    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
	    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
	    
	    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
	    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
	    return cipher.doFinal(data);
	}

	public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
	    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
	    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
	    
	    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
	    cipher.init(Cipher.DECRYPT_MODE, privateKey);
	    return cipher.doFinal(data);
	}
	
	public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {
	    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
	    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	    PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
	    
	    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
	    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	    return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {
	    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
	    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	    PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
	    
	    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
	    cipher.init(Cipher.DECRYPT_MODE, publicKey);
	    return cipher.doFinal(data);
	}
	
	public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
	    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
	    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	    PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
	    
	    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
	    signature.initSign(priKey);
	    signature.update(data);
	    return signature.sign();
	}
	
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
	    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
	    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
	    PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

	    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
	    signature.initVerify(pubKey);
	    signature.update(data);
	    return signature.verify(sign);
	}
	
	public static byte[] getPrivatekey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	public static byte[] getPublickey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}
	
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PRIVATE_KEY, privateKey);
		keyMap.put(PUBLIC_KEY, publicKey);
		return keyMap;
	}
}
