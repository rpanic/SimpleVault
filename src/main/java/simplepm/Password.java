package simplepm;

import java.util.Arrays;
import java.util.List;


public class Password {

	String cat;
	String pw;
	List<String> username;
	String description;
	
	public Password(String pw, List<String> username, String description) {
		super();
		this.pw = pw;
		this.username = username;
		this.description = description;
	}

	public String getCat() {
		return cat;
	}

	public void setCat(String cat) {
		this.cat = cat;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}

	public List<String> getUsernames() {
		return username;
	}

	public void setUsernames(List<String> username) {
		this.username = username;
	}
	
	public void addUsername(String username) {
		this.username.add(username);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toSaveable(String delimiter) {
		return pw + delimiter + description + delimiter + String.join(delimiter, username);
	}
	
	public static Password fromSave(String str, String delimiter) {
		String[] arr = str.split(delimiter);
		Password p = new Password(arr[0], Arrays.asList(Arrays.copyOfRange(arr, 2, arr.length)), arr[1]);
		return p;
	}
	
}
