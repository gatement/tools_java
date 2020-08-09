package johnson.tools.encryption.hash;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

public class HashUtilTest {
	@Test
	public void testCRC32() {
		String expected = "352441c2";
		String actual = HashUtil.crc32("abc".getBytes(Charset.forName("UTF-8")));
		assertEquals(expected, actual);
	}

	@Test
	public void testAdler32() {
		String expected = "24d0127";
		String actual = HashUtil.adler32("abc".getBytes(Charset.forName("UTF-8")));
		assertEquals(expected, actual);
	}

	@Test
	public void testMd5() throws NoSuchAlgorithmException {
		byte[] expected = new byte[] { (byte) 144, 1, 80, (byte) 152, 60, (byte) 210, 79, (byte) 176, (byte) 214,
				(byte) 150, 63, 125, 40, (byte) 225, 127, 114 };
		byte[] actual = HashUtil.md5("abc".getBytes(Charset.forName("UTF-8")));
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testSha1() throws NoSuchAlgorithmException {
		byte[] expected = new byte[] { (byte) 169, (byte) 153, 62, 54, 71, 6, (byte) 129, 106, (byte) 186, 62, 37, 113, 120, 80, (byte) 194, 108, (byte) 156, (byte) 208, (byte) 216, (byte) 157 };
		byte[] actual = HashUtil.sha1("abc".getBytes(Charset.forName("UTF-8")));
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testSha256() throws NoSuchAlgorithmException {
		byte[] expected = new byte[] { (byte)186, 120, 22, (byte)191, (byte)143, 1, (byte)207, (byte)234, 65, 65, 64, (byte)222, 93, (byte)174, 34, 35, (byte)176, 3, 97, (byte)163, (byte)150, 23, 122, (byte)156, (byte)180, 16, (byte)255, 97, (byte)242, 0, 21, (byte)173 };
		byte[] actual = HashUtil.sha256("abc".getBytes(Charset.forName("UTF-8")));
		assertArrayEquals(expected, actual);
	}
}
