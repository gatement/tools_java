package johnson.tools;

import java.nio.charset.Charset;
import java.util.Base64;

public class Base64Demo {
	public static void base64Demo() {
	    System.out.println(Base64.getEncoder().encodeToString("abc".getBytes(Charset.forName("UTF-8"))));	
	    System.out.println(Base64.getUrlEncoder().encodeToString("a.b.c".getBytes(Charset.forName("UTF-8"))));	
	    System.out.println(Base64.getMimeEncoder().encodeToString("abc000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000".getBytes(Charset.forName("UTF-8"))));	
	}
}
