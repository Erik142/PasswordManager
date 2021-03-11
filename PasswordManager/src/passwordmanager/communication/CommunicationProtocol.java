package passwordmanager.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import passwordmanager.communication.Response.ResponseCode;
import passwordmanager.model.Credential;
import passwordmanager.model.UserAccount;

/**
 * Implements the protocol used to communicate between the client and server
 * 
 * @author Erik Wahlberger
 * @version 2021-03-07
 */
public class CommunicationProtocol implements Serializable {
	
	private static final long serialVersionUID = -222451491800802999L;

	/**
	 * Specifies the allowed operations to perform when communicating
	 */
	public enum CommunicationOperation {
		/**
		 * Add a new Credential
		 */
		AddCredential, 
		/**
		 * Delete an existing Credential
		 */
		DeleteCredential,
		/**
		 * Update an existing Credential
		 */
		UpdateCredential, 
		/**
		 * Get all Credentials for a UserAccount object
		 */
		GetAllCredentials, 
		/**
		 * Add a new UserAccount
		 */
		AddUser, 
		/**
		 * Delete an existing UserAccount
		 */
		DeleteUser, 
		/**
		 * Update an existing UserAccount
		 */
		UpdateUser, 
		/**
		 * Get an existing UserAccount from an email address
		 */
		GetUser,
		/**
		 * Exchange AES encryption keys
		 */
		ExchangeKeys, 
		/**
		 * Initiate a new connection, exchange RSA keys
		 */
		InitiateConnection,
		/**
		 * Request an email with a password reset link for the provided email address
		 */
		ForgotPassword
	}

	/**
	 * Specifies if the protocol should be run in Client or Server mode
	 */
	public enum ProtocolMode {
		/**
		 * Client mode
		 */
		Client, 
		/**
		 * Server mode
		 */
		Server
	}

	/**
	 * Specifies different cryptography methods
	 */
	private enum CryptographyMethod {
		/**
		 * RSA cryptography
		 */
		RSA, 
		/**
		 * AES cryptography
		 */
		AES, 
		/**
		 * Unencrypted
		 */
		None
	}

	/**
	 * Used in the protocol to initiate a new connection
	 */
	private final int INITIATE_NEW_CONNECTION = 1;
	/**
	 * Used in the protocol to trigger an AES key exchange
	 */
	private final int EXCHANGE_KEYS = 2;
	/**
	 * Used in the protocol to indicate that invalid AES keys are used
	 */
	private final int INVALID_KEY = 3;
	/**
	 * Used in the protocol to indicate that no special messages are being transferred
	 */
	private final int USE_ESTABLISHED_CONNECTION = 0;

	/**
	 * The maximum allowed transactions for the AES keys
	 */
	private final int MAX_VALID_TRANSACTIONS = 10;

	/**
	 * AES encryption instance
	 */
	private RSA rsa = null;
	/**
	 * RSA encryption instance
	 */
	private AES aes = null;

	/**
	 * The AES password
	 */
	private String passwordKey = "";
	/**
	 * The AES salt
	 */
	private String salt = "";
	/**
	 * The AES password for the recipient
	 */
	private String recipientPasswordKey = "";
	/**
	 * The AES salt for the recipient
	 */
	private String recipientSalt = "";
	/**
	 * Remaining transactions for the AES keys before renewal is required
	 */
	private int validTransactionsRemaining = 0;

	/**
	 * The used protocol mode
	 */
	private final ProtocolMode PROTO_MODE;

	/**
	 * The socket connection
	 */
	private Socket socket;
	/**
	 * The input stream from the socket
	 */
	private DataInputStream inputStream;
	/**
	 * The output stream from the socket
	 */
	private DataOutputStream outputStream;

	/**
	 * Initializes a new CommunicationProtocol instance for the specified socket and
	 * in the specified mode.
	 * 
	 * @param socket The socket used to communicate
	 * @param mode   The mode for this instance. If the mode is Client, the instance
	 *               will immediately start to exchange encryption keys. If the mode
	 *               is Server, the instance will not do anything by itself.
	 */
	public CommunicationProtocol(Socket socket, ProtocolMode mode) {
		this.socket = socket;
		this.rsa = new RSA();
		this.aes = new AES();

		this.PROTO_MODE = mode;

		if (PROTO_MODE == ProtocolMode.Client) {
			initiateConnection();
			negotiateKeys();
		}
	}

	/**
	 * Initiates the connection by generating and exchanging RSA encryption keys
	 */
	private void initiateConnection() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Initiating connection on client side!");
		System.out.println("Exchanging RSA keys...");

		PublicKey publicKey = rsa.getPublicKey();
		if (publicKey == null) {
			System.out.println("RSA Public key is null!");
		}

		Query<PublicKey> rsaQuery = new Query<PublicKey>(CommunicationOperation.InitiateConnection, publicKey);

		Response<PublicKey> serverResponse = sendAndReceive(rsaQuery);

		PublicKey remotePublicKey = serverResponse.getData();

		rsa.setRecipientPublicKey(remotePublicKey);

		System.out.println("Successfully exchanged RSA keys");

		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

	/**
	 * Generates and exchanges AES encryption keys
	 */
	private void negotiateKeys() {
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Negotiating AES keys! " + PROTO_MODE);

		if (rsa.getRecipientPublicKey() == null) {
			// Connection has not been initiated
			System.out.println("RSA keys have not been exchanged!");
			return;
		}

		System.out.println("RSA keys are valid!");

		// Generate AES keys and send them to recipient
		passwordKey = aes.generateKeyPassword();
		salt = aes.generateSalt();

		if (PROTO_MODE == ProtocolMode.Server) {
			StringBuilder builder = new StringBuilder();

			// Append the number of valid transactions
			Random random = new Random();
			int validTransactions = 1 + random.nextInt(MAX_VALID_TRANSACTIONS);
			validTransactionsRemaining = validTransactions;

			builder.append(String.format("%02d", validTransactions));

			// Assume that password and salt has the same length, according to AES class
			int length = passwordKey.length();

			for (int i = 0; i < length; i++) {
				builder.append(passwordKey.charAt(i));
				builder.append(salt.charAt(i));
			}

			Response<String> aesResponse = new Response<String>(ResponseCode.OK, CommunicationOperation.ExchangeKeys,
					builder.toString());

			send(aesResponse);
		} else {
			// Receive AES keys and decipher them
			System.out.println("Client receiving AES keys...");

			Query<Object> keysQuery = new Query<Object>(CommunicationOperation.ExchangeKeys, null);

			Response<String> keysResponse = sendAndReceive(keysQuery);

			if (keysResponse.getResponseCode() == ResponseCode.OK) {
				System.out.println("Client: AES keys have been received!");

				String keys = keysResponse.getData();

				String transactionsString = keys.substring(0, 2);
				int transactions = Integer.parseInt(transactionsString);

				validTransactionsRemaining = transactions;

				System.out.println("Valid transactions remaining: " + validTransactionsRemaining);

				keys = keys.substring(2);

				recipientPasswordKey = "";
				recipientSalt = "";

				for (int i = 0; i < keys.length() - 1; i += 2) {
					recipientPasswordKey += keys.charAt(i);
					recipientSalt += keys.charAt(i + 1);
				}
			} else {
				// Something bad happened!
				System.out.println(
						"Something happened when AES keys were being received!! " + keysResponse.getResponseCode());
			}
		}

		System.out.println("Finished negotiating AES keys! " + PROTO_MODE);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}

	/**
	 * Checks if the current AES keys are valid by using the value for remaining
	 * transactions as specified by the server
	 * 
	 * @return True if AES keys are valid
	 */
	private boolean isKeyValid() {
		return validTransactionsRemaining > 0;
	}

	/**
	 * Sends a message to the Socket used in this instance, re-negotiates AES keys
	 * if necessary
	 * 
	 * @param <T>     The type of data in the Message
	 * @param message The Message to send
	 */
	public <T> void send(Message<T> message) {
		if (outputStream == null) {
			System.out.println("Setting output stream...");
			do {
				try {
					outputStream = new DataOutputStream(socket.getOutputStream());
					break;
				} catch (IOException ex) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} while (true);
			System.out.println("Output stream is set!");
		}
		
		CryptographyMethod serializeMethod = null;

		switch (message.getOperation()) {
		case ExchangeKeys:
			serializeMethod = CryptographyMethod.RSA;
			break;
		case InitiateConnection:
			serializeMethod = CryptographyMethod.None;
			break;
		default:
			serializeMethod = CryptographyMethod.AES;
		}

		boolean isInvalidKey = false;

		if (message instanceof Response) {
			Response<T> response = (Response<T>) message;
			if (response.getResponseCode() == ResponseCode.InvalidKey) {
				serializeMethod = CryptographyMethod.RSA;
				isInvalidKey = true;
			}
		}
		// Re-negotiate keys if key is not valid
		else if (serializeMethod == CryptographyMethod.AES
				&& message.getOperation() != CommunicationOperation.ExchangeKeys) {
			if (PROTO_MODE == ProtocolMode.Client) {
				while (!isKeyValid()) {
					System.out.println("AES keys need to be exchanged!");
					negotiateKeys();
				}
			} else {
				if (!isKeyValid()) {
					Response<T> response = new Response<T>(ResponseCode.InvalidKey, message.operation, message.data);
					send(response);
				}
			}

		}

		try {
			switch (serializeMethod) {
			case None:
				outputStream.writeInt(INITIATE_NEW_CONNECTION);
				break;
			case RSA:
				if (isInvalidKey) {
					outputStream.writeInt(INVALID_KEY);
				} else {
					outputStream.writeInt(EXCHANGE_KEYS);
				}
				break;
			default:
				outputStream.writeInt(USE_ESTABLISHED_CONNECTION);
			}

			byte[] objectBytes = null;

			if (message.getOperation() == CommunicationOperation.ExchangeKeys) {
				// Data length limitation in RSA encryption, cannot encrypt the entire Message
				// object at once
				// Encrypt AES key with RSA, and the rest of the message with AES
				byte[] aesKeyBytes = serializeObject(passwordKey, CryptographyMethod.RSA);
				byte[] aesSaltBytes = serializeObject(salt, CryptographyMethod.RSA);

				int aesKeyLength = aesKeyBytes.length;
				int aesSaltLength = aesSaltBytes.length;

				outputStream.writeInt(aesKeyLength);
				outputStream.write(aesKeyBytes);
				outputStream.writeInt(aesSaltLength);
				outputStream.write(aesSaltBytes);

				objectBytes = serializeObject(message, CryptographyMethod.AES);
			} else {
				objectBytes = serializeObject(message, serializeMethod);
			}

			outputStream.writeInt(objectBytes.length);
			outputStream.write(objectBytes);

			// If we serialized objects using the AES algorithm, decrease the amount of
			// remaining valid transactions
			if (serializeMethod == CryptographyMethod.AES) {
				validTransactionsRemaining -= 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				socket.close();
				inputStream.close();
				outputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println("Writing complete!");
	}

	/**
	 * Sends a Query object to the Socket in this instance and receives a Response
	 * object from the Socket
	 * 
	 * @param <T1>  The expected data type in the Response
	 * @param <T2>  The data type used in the Query
	 * @param query The Query to send
	 * @return The Response object
	 */
	@SuppressWarnings("unchecked")
	public <T1, T2> Response<T1> sendAndReceive(Query<T2> query) {
		send(query);
		return (Response<T1>) receive();
	}

	/**
	 * Receives a Message from the Socket in this instance
	 * 
	 * @param <T> The data type for the Message
	 * @return The Message
	 */
	private <T> Message<T> receive() {
		if (inputStream == null) {
			System.out.println("Setting input stream...");
			do {
				try {
					inputStream = new DataInputStream(socket.getInputStream());
					break;
				} catch (IOException ex) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} while (true);
			System.out.println("Input stream set!");
		}
		
		while (!socket.isClosed()) {
			try {
				int connectionType = inputStream.readInt();

				if (connectionType < 0) {
					continue;
				}

				CryptographyMethod deserializeMethod = null;

				if (connectionType == INITIATE_NEW_CONNECTION) {
					deserializeMethod = CryptographyMethod.None;
				} else if (connectionType == EXCHANGE_KEYS || connectionType == INVALID_KEY) {
					deserializeMethod = CryptographyMethod.RSA;
				} else {
					deserializeMethod = CryptographyMethod.AES;
				}

				Message<T> response = null;

				if (connectionType == EXCHANGE_KEYS) {
					// Exchanging AES keys
					int aesKeyLength = inputStream.readInt();

					byte[] aesKeyBytes = inputStream.readNBytes(aesKeyLength);
					int aesSaltKeyLength = inputStream.readInt();

					byte[] aesSaltBytes = inputStream.readNBytes(aesSaltKeyLength);
					int responseLength = inputStream.readInt();

					recipientPasswordKey = deserializeObject(aesKeyBytes, CryptographyMethod.RSA);
					recipientSalt = deserializeObject(aesSaltBytes, CryptographyMethod.RSA);

					response = deserializeObject(inputStream.readNBytes(responseLength), CryptographyMethod.AES);
				} else {
					int responseLength = inputStream.readInt();

					if (responseLength <= 0) {
						continue;
					}

					response = deserializeObject(inputStream.readNBytes(responseLength), deserializeMethod);
				}

				return response;
			} catch (Exception e) {
				e.printStackTrace();
				try {
					socket.close();
					inputStream.close();
					outputStream.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			}
		}

		return null;
	}

	/**
	 * Subscribes on Events on the Socket in this instance. Used exclusively on the
	 * server to listen on received messages from clients
	 * 
	 * @param eventListener The event listener instance
	 */
	public void subscribeOnSocket(CommunicationEventListener eventListener) {
		Thread subscribeThread = new Thread(() -> {
			while (!socket.isClosed()) {
				Message<Object> retrievedQuery = receive();

				CommunicationOperation operation = retrievedQuery.operation;
				Object object = retrievedQuery.data;
				
				switch (operation) {
				case AddUser:
				case DeleteUser:
				case UpdateUser:
				case GetAllCredentials:
				case GetUser:
				case ForgotPassword:
					eventListener.onUserAccountEvent((UserAccount) object, operation);
					break;
				case AddCredential:
				case DeleteCredential:
				case UpdateCredential:
					eventListener.onCredentialEvent((Credential) object, operation);
					break;
				case ExchangeKeys:
					negotiateKeys();
					break;
				case InitiateConnection:
					PublicKey publicKey = rsa.getPublicKey();
					PublicKey remotePublicKey = (PublicKey) object;

					rsa.setRecipientPublicKey(remotePublicKey);

					Response<PublicKey> initiateConnectionResponse = new Response<PublicKey>(ResponseCode.OK,
							operation, publicKey);

					send(initiateConnectionResponse);
					break;
				default:
					break;
				}

				switch (operation) {
				case ExchangeKeys:
					break;
				default:
					if (object != null && operation != null) {
						validTransactionsRemaining -= 1;
					}
				}
			}
		});

		subscribeThread.start();
	}

	/**
	 * Deserializes an encrypted byte array to the corresponding Object
	 * 
	 * @param <T>                The data type for the Object
	 * @param bytes              The encrypted byte array containing the Object
	 * @param cryptographyMethod The method used to deserialize the Object
	 * @return The Object
	 * @throws ClassNotFoundException
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	@SuppressWarnings("unchecked")
	private <T> T deserializeObject(byte[] bytes, CryptographyMethod cryptographyMethod)
			throws ClassNotFoundException, IOException, InvalidKeyException, NoSuchPaddingException,
			NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {

		byte[] decryptedData = null;

		if (cryptographyMethod == CryptographyMethod.AES) {
			decryptedData = aes.decrypt(bytes, recipientPasswordKey, recipientSalt);
		} else if (cryptographyMethod == CryptographyMethod.RSA) {
			decryptedData = rsa.decrypt(bytes);
		} else {
			decryptedData = bytes;
		}

		ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
		ObjectInputStream ois = new ObjectInputStream(bis);

		T obj = null;

		try {
			obj = (T) ois.readObject();
		} catch (EOFException e) {
			e.printStackTrace();
		}

		return obj;
	}

	/**
	 * Serializes an object to an encrypted byte array using the specified
	 * encryption method
	 * 
	 * @param <T>                The data type for the Object
	 * @param object             The Object to be serialized
	 * @param cryptographyMethod The cryptography method used to encrypt the
	 *                           serialized object
	 * @return The encrypted byte array containing the serialized object
	 * @throws IOException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchAlgorithmException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private <T> byte[] serializeObject(T object, CryptographyMethod cryptographyMethod)
			throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
			IllegalBlockSizeException, BadPaddingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);

		if (object != null) {
			oos.writeObject(object);
			oos.close();
		}

		byte[] encryptedBytes = null;

		if (cryptographyMethod == CryptographyMethod.AES) {
			encryptedBytes = aes.encrypt(baos.toByteArray(), passwordKey, salt);
		} else if (cryptographyMethod == CryptographyMethod.RSA) {
			encryptedBytes = rsa.encrypt(baos.toByteArray());
		} else {
			encryptedBytes = baos.toByteArray();
		}

		return encryptedBytes;
	}
}
