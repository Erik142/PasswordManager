package passwordmanager.communication;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * The AES class is used for encryption/decryption using the AES algorithm. The
 * class includes methods for encrypting/decrypting using AES as well as methods
 * for generating passwords and salts
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class AES {
	/**
	 * The length of the password and salt
	 */
	private static final int KEY_LENGTH = 128;

	/**
	 * Generate a password with a fixed length for AES
	 * 
	 * @return The password as a String
	 */
	public static String generateKeyPassword() {
		return RandomStringUtils.randomAlphanumeric(KEY_LENGTH);
	}

	/**
	 * Generate a salt with a fixed length
	 * 
	 * @return The salt as a String
	 */
	public static String generateSalt() {
		return RandomStringUtils.randomAlphanumeric(KEY_LENGTH);
	}

	/**
	 * Encrypts a byte array with the specified password and salt
	 * 
	 * @param data        The byte array to encrypt
	 * @param keyPassword The password String
	 * @param salt        The salt String
	 * @return The encrypted data as a byte array
	 */
	public static byte[] encrypt(byte[] data, String keyPassword, String salt) {
		try {
			byte[] iv = new byte[KEY_LENGTH / 8];
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(keyPassword.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}

		return null;
	}

	/**
	 * Decrypts an encrypted byte array with a specified password and salt
	 * 
	 * @param encryptedData The encrypted data as a byte array
	 * @param keyPassword   The password String
	 * @param salt          The salt String
	 * @return The decrypted data as a byte array
	 */
	public static byte[] decrypt(byte[] encryptedData, String keyPassword, String salt) {
		try {
			byte[] iv = new byte[KEY_LENGTH / 8];
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(keyPassword.toCharArray(), salt.getBytes(), 65536, 256);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}

		return null;
	}
}
