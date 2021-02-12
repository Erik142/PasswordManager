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
	
	public void sendRequest(Object object, CommunicationOperation operation) {
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
	}
	
	public Object sendAndReceive(Object object, CommunicationOperation operation) {
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
				int operationLength = inputStream.readInt();
				CommunicationOperation returnOperation = null;
				
				if (operationLength > 0) {
					returnOperation = (CommunicationOperation)deserializeObject(inputStream.readNBytes(operationLength));
					
					if (returnOperation != operation) {
						throw new Exception("Returned operation was wrong!");
					}
					
					int objectLength = inputStream.readInt();
					Object returnObject = null;
					
					if (objectLength > 0) {
						returnObject = deserializeObject(inputStream.readNBytes(objectLength));
					
						return returnObject;
					}
				}
			} catch (IOException|ClassNotFoundException e) {
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
					int operationLength = inputStream.readInt();
					CommunicationOperation operation = null;
					
					if (operationLength > 0) {
						operation = (CommunicationOperation)deserializeObject(inputStream.readNBytes(operationLength));
						
						int objectLength = inputStream.readInt();
						Object object = null;
						
						if (objectLength > 0) {
							object = deserializeObject(inputStream.readNBytes(objectLength));
						
							switch(operation) {
							case AddUser:
							case DeleteUser:
							case UpdateUser:
							case GetUser:
							case VerifyUser:
								eventListener.onUserAccountEvent((UserAccount)object, operation);
								break;
							case AddCredential:
							case DeleteCredential:
							case UpdateCredential:
							case GetCredential:
								eventListener.onCredentialEvent((Credential)object, operation);
								break;
							default:
							}
						}
					}
				} catch (IOException|ClassNotFoundException e) {
				}
			}
		});
		
		subscribeThread.start();
	}
	
	private Object deserializeObject(byte[] bytes) throws ClassNotFoundException, IOException {
		byte[] decryptedData = AES.decrypt(bytes);
		ByteArrayInputStream bis = new ByteArrayInputStream(decryptedData);
		ObjectInputStream ois = new ObjectInputStream(bis);
		
		Object obj = ois.readObject();
		
		return obj;
	}
	
	private byte[] serializeObject(Object object) throws IOException {
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
