package simplepm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasswordStore {

	Map<String, Password> encoded;
	
	public PasswordStore(Map<String, Password> enc){
		encoded = enc;
	}
	
	public PasswordStore(){
		encoded = new HashMap<String, Password>();
	}
	
	public void putEncoded(String key, Password s){
		encoded.put(key, s);
	}
	
	public Password getDecoded(AES aes, String str){
		try {
			Password p = encoded.get(str);
			if(p != null) {
				String dec = aes.decrypt(p.getPw());
				if(dec == null) {
					return null;
				}
				Password n = new Password(dec, p.getUsernames(), p.getDescription());
				return n;
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> save(String delimiter){
		
		List<String> list = new ArrayList<>();
		
		for(String key : encoded.keySet()) {
			
			Password p = encoded.get(key);
			
			String s = key + delimiter + p.toSaveable(delimiter);
			list.add(s);
			
		}
		
		return list;
		
	}
	
}
