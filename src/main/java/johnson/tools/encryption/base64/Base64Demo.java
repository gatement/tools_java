package johnson.tools.encryption.base64;

import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Demo {
	public static void base64Demo() {
		// normal
	    System.out.println(Base64.getEncoder().encodeToString("abc".getBytes(Charset.forName("UTF-8"))));	
	    // url safe
	    System.out.println(Base64.getUrlEncoder().encodeToString("a.b.c".getBytes(Charset.forName("UTF-8"))));	
	    // cut to lines by length
	    System.out.println(Base64.getMimeEncoder().encodeToString("abc000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".getBytes(Charset.forName("UTF-8"))));	
	}
}
