package passwordmanager.communication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

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
		VerifyUser
	}
	
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	
	public CommunicationProtocol(Socket socket) {
		this.socket = socket;
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
		
		try {
			int amountOfObjects = objects.length;
			outputStream.writeInt(amountOfObjects);
			
			byte[] operationBytes = serializeObject(operation);
			outputStream.writeInt(operationBytes.length);
			outputStream.write(operationBytes);
			
			for (Object obj : objects) {
				byte[] objectBytes = serializeObject(obj);
			
				outputStream.writeInt(objectBytes.length);
				outputStream.write(objectBytes);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T1,T2> T1 sendAndReceive(T2 object, CommunicationOperation operation) {
		return (T1) sendAndReceiveMultiple(object, operation).get(0);
	}
	
	public <T1,T2> ArrayList<T1> sendAndReceiveMultiple(T2 object, CommunicationOperation operation) {
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
		
		try {
			int amountOfObjects = 1;
			outputStream.writeInt(amountOfObjects);
			
			byte[] operationBytes = serializeObject(operation);
			byte[] objectBytes = serializeObject(object);
			
			outputStream.writeInt(operationBytes.length);
			outputStream.write(operationBytes);
			outputStream.writeInt(objectBytes.length);
			outputStream.write(objectBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
				int amountOfObjects = inputStream.readInt();
				
				if (amountOfObjects <= 0) {
					continue;
				}
				
				ArrayList<T1> returnObjects = new ArrayList<T1>();
				
				int operationLength = inputStream.readInt();
				CommunicationOperation returnOperation = null;
				
				if (operationLength > 0) {
					returnOperation = (CommunicationOperation)deserializeObject(inputStream.readNBytes(operationLength));
					
					if (returnOperation != operation) {
						throw new Exception("Returned operation was wrong!");
					}
					
					for (int i = 0; i < amountOfObjects; i++) {
						int objectLength = inputStream.readInt();
						T1 obj = null;
						
						if (objectLength > 0) {
							obj = deserializeObject(inputStream.readNBytes(objectLength));
						
							returnObjects.add(obj);
						}
					}
					
					return returnObjects;
				}
			} catch (IOException|ClassNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
					int amountOfObjects = inputStream.readInt();
					
					int operationLength = inputStream.readInt();
					CommunicationOperation operation = null;
					
					if (operationLength > 0) {
						operation = (CommunicationOperation)deserializeObject(inputStream.readNBytes(operationLength));
						
						for (int i = 0; i < amountOfObjects; i++) {
							int objectLength = inputStream.readInt();
							Object object = null;
							
							if (objectLength > 0) {
								object = deserializeObject(inputStream.readNBytes(objectLength));
							
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
								default:
								}
							}
						}
					}
				} catch (IOException|ClassNotFoundException e) {
				}
			}
		});
		
		subscribeThread.start();
	}
	
	@SuppressWarnings("unchecked")
	private <T> T deserializeObject(byte[] bytes) throws ClassNotFoundException, IOException {
		byte[] decryptedData = AES.decrypt(bytes);
		ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		T obj = (T)ois.readObject();
		
		return obj;
	}
	
	private <T> byte[] serializeObject(T object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		
		if (object != null) {
			oos.writeObject(object);
			oos.close();
		}
		
		byte[] encryptedBytes = AES.encrypt(baos.toByteArray());
		
		return encryptedBytes;
	}
}
