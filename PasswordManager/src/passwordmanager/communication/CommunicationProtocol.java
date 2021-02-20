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
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import passwordmanager.Credential;
import passwordmanager.UserAccount;

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
		IsKeyValid,
		GetKey,
		InitiateConnection,
		VerifyApplication
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
	private int validTransactionsRemaining = 0;
	
	private final ProtocolMode PROTO_MODE;
	
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	public CommunicationProtocol(Socket socket, ProtocolMode mode) {
		this.socket = socket;
		this.rsa = new RSA();
		
		this.PROTO_MODE = mode;
	}
	
	public <T> void send(T object, CommunicationOperation operation) {
		send(new Object[] { object }, operation);
	}
	
	public <T> void send(T[] objects, CommunicationOperation operation) {
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
		
		switch (operation) {
		case IsKeyValid:
		case GetKey:
			serializeMethod = CryptographyMethod.RSA;
			break;
		case InitiateConnection:
			serializeMethod = CryptographyMethod.None;
			break;
		default:
			serializeMethod = CryptographyMethod.AES;
		}
		
		try {
			int amountOfObjects = 0;
			
			if (objects != null) {
				amountOfObjects = objects.length;
			}
			
			if (operation == CommunicationOperation.InitiateConnection) {
				outputStream.writeInt(INITIATE_NEW_CONNECTION);
			}
			else if (operation == CommunicationOperation.GetKey) {
				outputStream.writeInt(EXCHANGE_KEYS);
			}
			else {
				outputStream.writeInt(USE_ESTABLISHED_CONNECTION);
			}
			
			outputStream.writeInt(amountOfObjects);
			
			System.out.println("Serializing operation... " + PROTO_MODE.toString());
			byte[] operationBytes = serializeObject(operation, serializeMethod);
			System.out.println("Serialized operation! " + PROTO_MODE.toString());
			outputStream.writeInt(operationBytes.length);
			outputStream.write(operationBytes);
			
			if (objects != null) {
				for (Object obj : objects) {
					byte[] objectBytes = serializeObject(obj, serializeMethod);
					
					System.out.println("Object successfully serialized!! " + PROTO_MODE.toString());
					outputStream.writeInt(objectBytes.length);
					outputStream.write(objectBytes);
				}
			}
			
			// If we serialized objects using the AES algorithm, decrease the amount of remaining valid transactions
			if (objects != null && serializeMethod == CryptographyMethod.AES) {
				validTransactionsRemaining -= 1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T1,T2> T1 sendAndReceive(T2 object, CommunicationOperation operation) {
		return (T1) sendAndReceiveMultiple(object, operation).values().iterator().next();
	}
	
	public <T1,T2> Multimap<CommunicationOperation, T1> sendAndReceiveMultiple(T2 object, CommunicationOperation operation) {
		send(object, operation);
		return receiveMultiple(operation);
	}
	
	private <T> Multimap<CommunicationOperation,T> receiveMultiple() {
		return receiveMultiple(null);
	}
	
	private <T> Multimap<CommunicationOperation,T> receiveMultiple(CommunicationOperation operation) {
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
				
				int amountOfObjects = inputStream.readInt();
				
				if (amountOfObjects <= 0) {
					continue;
				}
				
				Multimap<CommunicationOperation, T> returnObjects = HashMultimap.create();
				
				int operationLength = inputStream.readInt();
				CommunicationOperation returnOperation = null;
				
				CryptographyMethod deserializeMethod = null;
				
				if (operationLength > 0) {
					if (connectionType == INITIATE_NEW_CONNECTION) {
						deserializeMethod = CryptographyMethod.None;
					}
					else if (connectionType == EXCHANGE_KEYS) {
						deserializeMethod = CryptographyMethod.RSA;
					}
					else {
						deserializeMethod = CryptographyMethod.AES;
					}
					
					returnOperation = (CommunicationOperation)deserializeObject(inputStream.readNBytes(operationLength), deserializeMethod);
					
					if (operation != null && returnOperation != operation) {
						throw new Exception("Returned operation was wrong!");
					}
					
					for (int i = 0; i < amountOfObjects; i++) {
						int objectLength = inputStream.readInt();
						T obj = null;
						
						if (objectLength > 0) {
							obj = deserializeObject(inputStream.readNBytes(objectLength), deserializeMethod);
						
							returnObjects.put(returnOperation, obj);
						}
						else {
							returnObjects.put(returnOperation, null);
						}
					}
					
					if (amountOfObjects == 0) {
						returnObjects.put(returnOperation, null);
					}
					
					return returnObjects;
				}
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
					Multimap<CommunicationOperation, Object> retrievedData = receiveMultiple();
					
					for (Entry<CommunicationOperation, Object> entry : retrievedData.entries()) {
						CommunicationOperation operation = entry.getKey();
						Object object = entry.getValue();
						
						switch(operation) {
						case AddUser:
						case DeleteUser:
						case UpdateUser:
						case GetCredential:
						case GetAllCredentials:
						case GetUser:
						case VerifyUser:
							eventListener.onUserAccountEvent((UserAccount)object, operation);
							break;
						case AddCredential:
						case DeleteCredential:
						case UpdateCredential:
							eventListener.onCredentialEvent((Credential)object, operation);
							break;
						case InitiateConnection:
							System.out.println("Initiate connection event!");
							PublicKey publicKey = rsa.getPublicKey();
							PublicKey remotePublicKey = (PublicKey)object;
							System.out.println("Server recieved public key");
							
							rsa.setRecipientPublicKey(remotePublicKey);
							
							send(publicKey, operation);
							break;
						default:
							break;
						}
						
						switch(operation) {
						case GetKey:
						case InitiateConnection:
							break;
						default:
							if (object != null) {
								validTransactionsRemaining -= 1;
							}
						}
					}
				} catch (Exception e) {
				}
			}
		});
		
		subscribeThread.start();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T deserializeObject(byte[] bytes, CryptographyMethod cryptographyMethod) throws ClassNotFoundException, IOException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException {
		
		byte[] decryptedData = null; 
		
		if (cryptographyMethod == CryptographyMethod.AES) {
			decryptedData = AES.decrypt(bytes, passwordKey, salt);
		}
		else if (cryptographyMethod == CryptographyMethod.RSA) {
			decryptedData = rsa.decrypt(bytes);
		}
		else {
			decryptedData = bytes;
		}
		
		if (decryptedData == null) {
			System.out.println("Decrypted data is null!! + " + PROTO_MODE.toString());
		}
		
		System.out.println("Cryptographic method: " + cryptographyMethod.toString() + ". " + PROTO_MODE.toString());
		
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
		else if (cryptographyMethod == CryptographyMethod.RSA) {
			encryptedBytes = rsa.encrypt(baos.toByteArray());
		}
		else {
			encryptedBytes = baos.toByteArray();
		}
		
		return encryptedBytes;
	}
}
