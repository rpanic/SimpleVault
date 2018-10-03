package simplepm;

public class Test {
	public static void main(String[] args) throws Exception{
		
		String pw = "masterpassword";
		
		AES aes = new AES(pw);
		
		String text = "Haloodsagodsoag";
		
		String enc = aes.encrypt(text);
		
		System.out.println(enc);
		
		System.out.println(aes.decrypt(enc));
		
	}
}
