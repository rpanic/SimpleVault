package simplepm;

import java.util.HashMap;
import java.util.Map;

public class PasswordStore {

	Map<String, String> encoded;
	
	public PasswordStore(Map<String, String> enc){
		encoded = enc;
	}
	
	public PasswordStore(){
		encoded = new HashMap<String, String>();
	}
	
	public void putEncoded(String key, String s){
		encoded.put(key, s);
	}
	
	public String getDecoded(AES aes, String str){
		try {
			return new String(aes.decrypt(encoded.get(str).getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
