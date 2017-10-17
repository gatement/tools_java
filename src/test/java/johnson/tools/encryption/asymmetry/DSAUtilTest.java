package johnson.tools.encryption.asymmetry;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

public class DSAUtilTest {

	@Test
	public void testSignature() throws Exception {
		String inputStr = "Johnson";
		byte[] inputData = inputStr.getBytes("UTF-8");
		
		Map<String, Object> keyMap = DSAUtil.initKey();
		byte[] privateKey = DSAUtil.getPrivatekey(keyMap);
		byte[] publicKey = DSAUtil.getPublickey(keyMap);

		byte[] sign = DSAUtil.sign(inputData, privateKey);
		//System.out.println("DSA signature = " + Base64.encodeBase64String(sign));
		
		boolean verification = DSAUtil.verify(inputData, publicKey, sign);
		assertTrue(verification);
	}
}
