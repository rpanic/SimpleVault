package simplepm;

import java.util.Map;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class MainApplication {
		
	Map<String, String> tokens;
	
	PasswordStore store;
	
	@RequestMapping("/")
	String home(){
		return "Hello, World! This is the main Page";
	}
	
	@RequestMapping(path={"/requestToken"}, method={RequestMethod.POST})
	String requestToken(@RequestParam(required=true, name="pw") String pw){
		
		Random r = new Random();
		String random = "";
		for(int i = 0 ; i < 10 ; i++){
			
			random += r.nextInt(9);
			
		}
		tokens.put(random, pw);
		return random;
		
	}
	
	@RequestMapping(path={"/insert"}, method={RequestMethod.POST})
	boolean insert(@RequestParam(required=true, name="cat") String cat, @RequestParam(required=true, name="pw") String pw, @RequestParam(required=true, name="token") String token){
		
		if(tokens.containsKey(token)){
			
			String key = tokens.get(token);
			
			String encoded;
			try {
				encoded = new String(new AES(key.getBytes()).encrypt(pw.getBytes()));
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			
			store.putEncoded(cat, encoded);
			return true;
		}
		
		return false;
	}
	
	@RequestMapping(path={"/get"}, method={RequestMethod.POST})
	String get(@RequestParam(required=true, name="cat") String cat, @RequestParam(required=true, name="key") String key){
		
		
		return null;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
}
