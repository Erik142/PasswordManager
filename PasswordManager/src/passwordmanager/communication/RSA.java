package passwordmanager.communication;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.security.Key;

public class RSA {
	private final String KEYPAIRALGO = "RSA";
	private final String CIPHERTRANSFORMATION = "RSA/ECB/OAEPWITHSHA-512ANDMGF1PADDING";
	private final int KEYSIZE = 4096;
	
	private PublicKey publicKey = null;
	private PublicKey recipientPublicKey = null;
	
	private PrivateKey privateKey = null;
	
	public RSA() {
		generateKeyPair();
	}
	
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEYPAIRALGO);
			keyPairGenerator.initialize(KEYSIZE);
			
			KeyPair keyPair = keyPairGenerator.generateKeyPair();
			
			this.publicKey = keyPair.getPublic();
			this.privateKey = keyPair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PublicKey getPublicKey() {
		return this.publicKey;
	}
	
	public PublicKey getRecipientPublicKey() {
		return recipientPublicKey;
	}
	
	public void setRecipientPublicKey(PublicKey publicKey) {
		recipientPublicKey = publicKey;
	}
	
	public byte[] encrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		return performCipherOperation(Cipher.ENCRYPT_MODE, recipientPublicKey, bytes);
	}
	
	public byte[] decrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		return performCipherOperation(Cipher.DECRYPT_MODE, privateKey, bytes);
	}
	
	private byte[] performCipherOperation(int cipherOpMode, Key key, byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (key == null) {
			throw new NullPointerException("The key was null.");
		}
		
		Cipher cipher = Cipher.getInstance(CIPHERTRANSFORMATION);
		cipher.init(cipherOpMode, key);
		
		byte[] cipherBytes = cipher.doFinal(bytes);
		
		return cipherBytes;
	}
}
