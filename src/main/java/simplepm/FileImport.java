package simplepm;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileImport {

	public static List<Password> importFrom(File file) throws IOException{
		
		List<Password> passwords = new ArrayList<>();
		List<String> lines = Files.readAllLines(file.toPath());
		
		for(int i = 0 ; i < lines.size() ; i++) {
			
			List<String> passwordLines = new ArrayList<>();
			for(int j = i ; j < i + 4 && j < lines.size() ; j++) { //4 ist maximum mit description
				
				String str = lines.get(j);
				if(!(str.equals("") || str.equals("\n"))) {
					
					passwordLines.add(str);
					
				}else {
					i = j;
					break;
				}
			}
			
			if(passwordLines.size() >= 3) {
				ArrayList<String> usernames = new ArrayList<>();
				Arrays.asList(passwordLines.get(1).split(";")).stream().map(x -> x.toString()).forEach(x -> usernames.add(x));
				
				Password p = new Password(passwordLines.get(2), usernames, (passwordLines.size() >= 4 ? passwordLines.get(3) : ""));
				p.setCat(passwordLines.get(0).replaceAll(":", ""));
				
				passwords.add(p);
			}
		}
		return passwords;
		
	}
	
}
