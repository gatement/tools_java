package johnson.tools;

import static org.junit.Assert.assertArrayEquals;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class HashTest {
	@Test
	public void testMd5() throws NoSuchAlgorithmException {
		byte[] expected = new byte[] { (byte) 144, 1, 80, (byte) 152, 60, (byte) 210, 79, (byte) 176, (byte) 214,
				(byte) 150, 63, 125, 40, (byte) 225, 127, 114 };
		byte[] actual = Hash.md5("abc".getBytes(Charset.forName("UTF-8")));
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testSha() throws NoSuchAlgorithmException {
		byte[] expected = new byte[] { (byte) 169, (byte) 153, 62, 54, 71, 6, (byte) 129, 106, (byte) 186, 62, 37, 113,
				120, 80, (byte) 194, 108, (byte) 156, (byte) 208, (byte) 216, (byte) 157 };
		byte[] actual = Hash.sha("abc".getBytes(Charset.forName("UTF-8")));
		assertArrayEquals(expected, actual);
	}
}
