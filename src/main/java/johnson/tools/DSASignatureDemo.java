package johnson.tools;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Signature;
import java.security.SignedObject;

public class DSASignatureDemo {
	public static void demoDSASignature1() throws Exception {
		byte[] data = "Data Signature".getBytes();

		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
		keyPairGen.initialize(1024);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		Signature signature = Signature.getInstance(keyPairGen.getAlgorithm());
		signature.initSign(keyPair.getPrivate());
		signature.update(data);
		byte[] sign = signature.sign();

		System.out.println();
		System.out.println("DSA signature: ");
		Util.printBytes(sign);

		signature.initVerify(keyPair.getPublic());
		signature.update(data);
		boolean status = signature.verify(sign);

		System.out.print("verify result: ");
		System.out.println(status);
	}

	public static void demoDSASignature2() throws Exception {
		byte[] data = "Data Signature".getBytes();

		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
		keyPairGen.initialize(1024);

		KeyPair keyPair = keyPairGen.generateKeyPair();

		Signature signature = Signature.getInstance(keyPairGen.getAlgorithm());

		SignedObject s = new SignedObject(data, keyPair.getPrivate(), signature);
		byte[] sign = s.getSignature();

		System.out.println();
		System.out.println("DSA signature: ");
		Util.printBytes(sign);

		boolean status = s.verify(keyPair.getPublic(), signature);

		System.out.print("verify result: ");
		System.out.println(status);
	}
}
