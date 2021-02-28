package passwordmanager;

public class Login {
	 
    public static boolean authenticate(String username, String password) {
        // hardcoded username and password for testing
        if (username.equals("Arian") && password.equals("123")) {
            return true;
        }
        return false;
    }
}