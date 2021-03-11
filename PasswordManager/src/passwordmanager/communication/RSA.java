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

/**
 * The RSA class is used to encrypt and decrypt data using the RSA algorithm, as
 * well as generating private/public RSA keypairs
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class RSA {
	private final String KEYPAIRALGO = "RSA";
	private final String CIPHERTRANSFORMATION = "RSA/ECB/PKCS1Padding";
	private final int KEYSIZE = 1024;

	private PublicKey publicKey = null;
	private PublicKey recipientPublicKey = null;

	private PrivateKey privateKey = null;

	/**
	 * Creates a new instance of a RSA object and generates a new keypair
	 */
	public RSA() {
		generateKeyPair();
	}

	/**
	 * Generates a new private/public keypair
	 */
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEYPAIRALGO);
			keyPairGenerator.initialize(KEYSIZE);

			KeyPair keyPair = keyPairGenerator.generateKeyPair();

			this.publicKey = keyPair.getPublic();
			this.privateKey = keyPair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieves the public RSA key
	 * 
	 * @return The public RSA key
	 */
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	/**
	 * Get the public RSA key for the recipient
	 * 
	 * @return The public RSA key
	 */
	public PublicKey getRecipientPublicKey() {
		return recipientPublicKey;
	}

	/**
	 * Sets the public RSA key for the recipient
	 * 
	 * @param publicKey The new RSA public key
	 */
	public void setRecipientPublicKey(PublicKey publicKey) {
		recipientPublicKey = publicKey;
	}

	/**
	 * Encrypts the specified byte array with the public RSA key of the recipient
	 * 
	 * @param bytes The unencrypted byte array
	 * @return The encrypted byte array
	 * @throws NoSuchPaddingException Thrown by the underlying Cipher class
	 * @throws NoSuchAlgorithmException Thrown by the underlying Cipher class
	 * @throws InvalidKeyException Thrown by the underlying Cipher class
	 * @throws IllegalBlockSizeException Thrown by the underlying Cipher class
	 * @throws BadPaddingException Thrown by the underlying Cipher class
	 */
	public byte[] encrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		return performCipherOperation(Cipher.ENCRYPT_MODE, recipientPublicKey, bytes);
	}

	/**
	 * Decrypts the specified byte array with the private RSA key generated in this
	 * instance
	 * 
	 * @param bytes The RSA-encrypted byte array
	 * @return The decrypted byte array
	 * @throws NoSuchPaddingException Thrown by the underlying Cipher class
	 * @throws NoSuchAlgorithmException Thrown by the underlying Cipher class
	 * @throws InvalidKeyException Thrown by the underlying Cipher class
	 * @throws IllegalBlockSizeException Thrown by the underlying Cipher class
	 * @throws BadPaddingException Thrown by the underlying Cipher class
	 */
	public byte[] decrypt(byte[] bytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException {
		return performCipherOperation(Cipher.DECRYPT_MODE, privateKey, bytes);
	}

	/**
	 * Perform a cipher operation, e.g. an encryption or decryption of data
	 * 
	 * @param cipherOpMode The cipher operation mode. Cipher.DECRYPT_MODE for
	 *                     decryption, Cipher.ENCRYPT_MODE for encryption
	 * @param key          The key used to perform the cipher operation
	 * @param bytes        The byte array on which the cipher operation should be
	 *                     applied to
	 * @return The ciphered byte array
	 * @throws NoSuchPaddingException Thrown by the underlying Cipher class
	 * @throws NoSuchAlgorithmException Thrown by the underlying Cipher class
	 * @throws InvalidKeyException Thrown by the underlying Cipher class
	 * @throws IllegalBlockSizeException Thrown by the underlying Cipher class
	 * @throws BadPaddingException Thrown by the underlying Cipher class
	 */
	private byte[] performCipherOperation(int cipherOpMode, Key key, byte[] bytes) throws NoSuchPaddingException,
			NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		if (key == null) {
			throw new NullPointerException("The key was null.");
		}

		Cipher cipher = Cipher.getInstance(CIPHERTRANSFORMATION);
		cipher.init(cipherOpMode, key);

		byte[] cipherBytes = cipher.doFinal(bytes);

		return cipherBytes;
	}
}
