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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import passwordmanager.Credential;
import passwordmanager.UserAccount;
import passwordmanager.communication.Response.ResponseCode;

/**
 * 
 * @author Erik Wahlberger
 *
 */
public class CommunicationProtocol implements Serializable {
	public enum CommunicationOperation {
		AddCredential,
		DeleteCredential,
		UpdateCredential,
		GetCredential,
		GetAllCredentials,
		AddUser,
		DeleteUser,
		UpdateUser,
		GetUser,
		VerifyUser,
		ExchangeKeys,
		InitiateConnection,
		VerifyApplication,
		ForgotPassword
	}
	
	public enum ProtocolMode {
		Client,
		Server
	}
	
	private enum CryptographyMethod {
		RSA,
		AES,
		None
	}
	
	private final int INITIATE_NEW_CONNECTION = 1;
	private final int EXCHANGE_KEYS = 2;
	private final int USE_ESTABLISHED_CONNECTION = 0;
	
	private final int MAX_VALID_TRANSACTIONS = 10;
	
	private RSA rsa = null;
	
	private String passwordKey = "";
	private String salt = "";
	private String recipientPasswordKey = "";
	private String recipientSalt = "";
	private int validTransactionsRemaining = 0;
	
	private final ProtocolMode PROTO_MODE;
	
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	public CommunicationProtocol(Socket socket, ProtocolMode mode) {
		this.socket = socket;
		this.rsa = new RSA();
		
		this.PROTO_MODE = mode;
		
		if (PROTO_MODE == ProtocolMode.Client) {
			initiateConnection();
			negotiateKeys();
		}
	}
	
	private void initiateConnection() {
		// TODO: Validate connection
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
		
		Query<PublicKey> rsaQuery = new Query<PublicKey>("", CommunicationOperation.InitiateConnection, publicKey);
		
		Response<PublicKey> serverResponse = sendAndReceive(rsaQuery);
		
		PublicKey remotePublicKey = serverResponse.getData();
		
		rsa.setRecipientPublicKey(remotePublicKey);
		
		System.out.println("Successfully exchanged RSA keys");
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
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
		passwordKey = AES.generateKeyPassword();
		salt = AES.generateSalt();
		
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
			
			Response<String> aesResponse = new Response<String>(ResponseCode.OK, CommunicationOperation.ExchangeKeys, builder.toString());
			
			send(aesResponse);
		}
		else {
			// Receive AES keys and decipher them
			System.out.println("Client receiving AES keys...");
			
			Query<Object> keysQuery = new Query<Object>("", CommunicationOperation.ExchangeKeys, null);
			
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
			}
			else {
				// Something bad happened!
				System.out.println("Something happened when AES keys were being received!! " + keysResponse.getResponseCode());
			}
		}
		
		System.out.println("Finished negotiating AES keys! " + PROTO_MODE);
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
	}
	
	private boolean isKeyValid() {
		return validTransactionsRemaining > 0;
	}

	public <T> void send(Message<T> message) {
		do {
			try {
				if (outputStream == null) {
					outputStream = new DataOutputStream(socket.getOutputStream());
				}
				break;
			} catch(IOException ex) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} while(true);
		
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
		
		// Re-negotiate keys if key is not valid
		if (serializeMethod == CryptographyMethod.AES && message.getOperation() != CommunicationOperation.ExchangeKeys) {
			if (PROTO_MODE == ProtocolMode.Client) {
				while (!isKeyValid()) {
					System.out.println("AES keys need to be exchanged!");
					negotiateKeys();
				}
			}
			else {
				if (!isKeyValid()) {
					Response<Boolean> response = new Response<Boolean>(ResponseCode.InvalidKey, message.operation,false);
					send(response);
				}
			}
			
		}
		
		try {
			switch (message.getOperation()) {
			case InitiateConnection:
				outputStream.writeInt(INITIATE_NEW_CONNECTION);
				break;
			case ExchangeKeys:
				outputStream.writeInt(EXCHANGE_KEYS);
				break;
			default:
				outputStream.writeInt(USE_ESTABLISHED_CONNECTION);
			}
			
			byte[] objectBytes = null;
			
			if (message.getOperation() == CommunicationOperation.ExchangeKeys) {
				// Data length limitation in RSA encryption, cannot encrypt the entire Message object at once
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
			}
			else {
				objectBytes = serializeObject(message, serializeMethod);
			}
			
			outputStream.writeInt(objectBytes.length);
			outputStream.write(objectBytes);
			
			// If we serialized objects using the AES algorithm, decrease the amount of remaining valid transactions
			if (serializeMethod == CryptographyMethod.AES) {
				validTransactionsRemaining -= 1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T1,T2> Response<T1> sendAndReceive(Query<T2> query) {
		send(query);
		return (Response<T1>)receive();
	}
	
	private <T> Message<T> receive() {
		do {
			try {
				if (inputStream == null) {
					inputStream = new DataInputStream(socket.getInputStream());
				}
				break;
			} catch(IOException ex) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} while(true);
		
		while (!socket.isClosed()) {
			try {
				int connectionType = inputStream.readInt();
				
				if (connectionType < 0) {
					continue;
				}
				
				CryptographyMethod deserializeMethod = null;
				
				if (connectionType == INITIATE_NEW_CONNECTION) {
					deserializeMethod = CryptographyMethod.None;
				}
				else if (connectionType == EXCHANGE_KEYS) {
					deserializeMethod = CryptographyMethod.RSA;
				}
				else {
					deserializeMethod = CryptographyMethod.AES;
				}
				
				Message<T> response = null;
				
				if (deserializeMethod == CryptographyMethod.RSA) {
					// Exchanging AES keys
					int aesKeyLength = inputStream.readInt();
					
					byte[] aesKeyBytes = inputStream.readNBytes(aesKeyLength);
					int aesSaltKeyLength = inputStream.readInt();
					
					byte[] aesSaltBytes = inputStream.readNBytes(aesSaltKeyLength);
					int responseLength = inputStream.readInt();
					
					recipientPasswordKey = deserializeObject(aesKeyBytes, CryptographyMethod.RSA);
					recipientSalt = deserializeObject(aesSaltBytes, CryptographyMethod.RSA);
					
					response = deserializeObject(inputStream.readNBytes(responseLength), CryptographyMethod.AES);
				}
				else {
					int responseLength = inputStream.readInt();
					
					if (responseLength <= 0) {
						continue;
					}
					
					response = deserializeObject(inputStream.readNBytes(responseLength), deserializeMethod);
				}
				
				return response;
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}
		
		return null;
	}
	
	public void subscribeOnSocket(CommunicationEventListener eventListener) {
		Thread subscribeThread = new Thread(() -> {
			do {
				try {
					if (inputStream == null) {
						inputStream = new DataInputStream(socket.getInputStream());
					}
					break;
				} catch(IOException ex) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} while(true);
			
			while (!socket.isClosed()) {
				try {
					Message<Object> retrievedQuery = receive();
					
					CommunicationOperation operation = retrievedQuery.operation;
					Object object = retrievedQuery.data;
					
					switch(operation) {
					case AddUser:
					case DeleteUser:
					case UpdateUser:
					case GetCredential:
					case GetAllCredentials:
					case GetUser:
					case VerifyUser:
					case ForgotPassword:
						eventListener.onUserAccountEvent((UserAccount)object, operation);
						break;
					case AddCredential:
					case DeleteCredential:
					case UpdateCredential:
						eventListener.onCredentialEvent((Credential)object, operation);
						break;
					case ExchangeKeys:
						negotiateKeys();
						break;
					case InitiateConnection:
						PublicKey publicKey = rsa.getPublicKey();
						PublicKey remotePublicKey = (PublicKey)object;
						
						rsa.setRecipientPublicKey(remotePublicKey);
						
						Response<PublicKey> initiateConnectionResponse = new Response<PublicKey>(ResponseCode.OK, operation, publicKey);
						
						send(initiateConnectionResponse);
						break;
					default:
						break;
					}
					
					switch(operation) {
					case ExchangeKeys:
						break;
					default:
						if (object != null && operation != null) {
							validTransactionsRemaining -= 1;
						}
					}
				} catch (Exception e) {
					System.out.println("Exception while retrieving query on server");
				}
			}
		});
		
		subscribeThread.start();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T deserializeObject(byte[] bytes, CryptographyMethod cryptographyMethod) throws ClassNotFoundException, IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] decryptedData = null; 
		
		if (cryptographyMethod == CryptographyMethod.AES) {
			decryptedData = AES.decrypt(bytes, recipientPasswordKey, recipientSalt);
		}
		else if (cryptographyMethod == CryptographyMethod.RSA) {
			decryptedData = rsa.decrypt(bytes);
		}
		else {
			decryptedData = bytes;
		}
		
		ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		T obj = null;
		
		try {
			obj = (T)ois.readObject();
		} catch (EOFException e) {
			
		}
		
		return obj;
	}
	
	private <T> byte[] serializeObject(T object, CryptographyMethod cryptographyMethod) throws IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		
		if (object != null) {
			oos.writeObject(object);
			oos.close();
		}
		
		byte[] encryptedBytes = null;
		
		if (cryptographyMethod == CryptographyMethod.AES ) {
			encryptedBytes = AES.encrypt(baos.toByteArray(), passwordKey, salt);
		}
		// It is an AES key request/response if the encryption method is RSA
		else if (cryptographyMethod == CryptographyMethod.RSA) {
			encryptedBytes = rsa.encrypt(baos.toByteArray());
		}
		else {
			encryptedBytes = baos.toByteArray();
		}
		
		return encryptedBytes;
	}
}
