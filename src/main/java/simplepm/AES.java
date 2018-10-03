package simplepm;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;

public class AES
{
    private byte[] key;
    private SecretKeySpec secretKeySpec;

    private static final String ALGORITHM = "AES";
    
    public AES(String key) {
		try {
			
	    	MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] keyb = sha.digest(key.getBytes());
			// Only first 128 bits
			keyb = Arrays.copyOf(keyb, 16); 
			this.key = keyb;
			secretKeySpec = new SecretKeySpec(keyb, "AES");
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
    }
    
    public String encrypt(String text) {
    	
    	try {
    		
	    	// Encrypt
	    	Cipher cipher = Cipher.getInstance("AES");
	    	cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
	    	byte[] encrypted = cipher.doFinal(text.getBytes());
	    	 
	    	// convert to Base64
	    	BASE64Encoder myEncoder = new BASE64Encoder();
	    	String enc = myEncoder.encode(encrypted);
	    	
	    	return enc;
    	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public String decrypt(String encrypted) {
    	
        try {
	    	BASE64Decoder myDecoder2 = new BASE64Decoder();
			byte[] crypted2 = myDecoder2.decodeBuffer(encrypted);
	    	
	    	//decrypt
	    	Cipher cipher2 = Cipher.getInstance("AES");
	        cipher2.init(Cipher.DECRYPT_MODE, secretKeySpec);
	        byte[] cipherData2 = cipher2.doFinal(crypted2);
	        String erg = new String(cipherData2);
	        
	        return erg;
	        
		} catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
        return null;
        
    }

    /**
     * Encrypts the given plain text
     *
     * @param plainText The plain text to encrypt
     */
    @Deprecated
    public byte[] encrypt(byte[] plainText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(plainText);
    }

    /**
     * Decrypts the given byte array
     *
     * @param cipherText The data to decrypt
     */
    @Deprecated
    public byte[] decrypt(byte[] cipherText) throws Exception
    {
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(cipherText);
    }
}