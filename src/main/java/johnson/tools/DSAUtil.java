package johnson.tools;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class DSAUtil {
	public static final String ALGORITHM = "DSA";
	public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";
	private static final String PUBLIC_KEY = "DSAPublicKey";
	private static final String PRIVATE_KEY = "DSAPrivateKey";
	private static final int KEY_SIZE = 1024;
	
	public static byte[] sign(byte[] data, byte[] privateKey) throws Exception {
	    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
	    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
	    PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
	    
	    Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
	    signature.initSign(priKey);
	    signature.update(data);
	    return signature.sign();
	}
	
	public static boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception {
	    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
	    KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
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
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGen.initialize(KEY_SIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		DSAPrivateKey privateKey = (DSAPrivateKey) keyPair.getPrivate();
		DSAPublicKey publicKey = (DSAPublicKey) keyPair.getPublic();

		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PRIVATE_KEY, privateKey);
		keyMap.put(PUBLIC_KEY, publicKey);
		return keyMap;
	}
}
