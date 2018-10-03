package simplepm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MainApplication {
		
	Map<String, String> tokens = new HashMap<>();
	
	PasswordStore store;
	
	public static final String delimiter = ";";
	
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
	boolean insert(@RequestParam(required=true, name="cat") String cat, @RequestParam(required=true, name="pw") String pw, @RequestParam(required=true, name="token") String token, @RequestParam(required=true, name="description") String description, @RequestParam(required=true, name="username") String username){
		
		if(tokens.containsKey(token)){
			
			String key = tokens.get(token);
			
			String encoded;
			try {
				encoded = new AES(key).encrypt(pw);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			List<String> usernames = new ArrayList<>();
			Collections.addAll(usernames, username.split(";"));
			
			Password p = new Password(encoded, usernames, description);
			
			store.putEncoded(cat, p);
			
			save();
			return true;
		}
		
		return false;
	}
	
	@RequestMapping(path={"/get"}, method={RequestMethod.POST})
	Password get(@RequestParam(required=true, name="cat") String cat, @RequestParam(required=true, name="token") String token){
		
		if(tokens.containsKey(token)) {

			String key = tokens.get(token);
			
			try {
				AES aes = new AES(key);
				return store.getDecoded(aes, cat);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	@RequestMapping(path={"/get2"}, method={RequestMethod.POST})
	Password get(@RequestBody String body) {
		String[] arr = body.split(";");
		return get(arr[0], arr[1]);
	}
	
	@RequestMapping(path= {"/categories"}, method= {RequestMethod.POST, RequestMethod.GET})
	List<String> categories(@RequestParam(required=true, name="token") String token){
		
		return store.encoded.keySet().stream().collect(Collectors.toList());
		
	}
	
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	
	public MainApplication() {
		store = new PasswordStore();
		load();
	}
	
	File f = new File(System.getProperty("user.dir") + "/enc.csv");
	
	public void save() {
		
		if(!f.exists()) {
			f.getParentFile().mkdirs();
		}
		
		List<String> str = store.save(delimiter);
		
		try {
			Files.write(f.toPath(), str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void load() {
		
		if(f.exists()) {
			try{
				
				List<String> lines = Files.readAllLines(f.toPath());
				
				for(String line : lines) {
					
					int index = line.indexOf(delimiter.charAt(0));
					String cat = line.substring(0, index);
					line = line.substring(index + 1);
					
					Password p = Password.fromSave(line, delimiter);
					store.putEncoded(cat, p);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
}
