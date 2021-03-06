package passwordmanager.util;

/**
 * 
 * @author Erik Wahlberger
 *
 * StringExtensions class contains a helper method for Strings to check if Strings are null or empty
 */
public class StringExtensions {
	/**
	 * 
	 * @param string The string used to decide the return value
	 * @return true if the string is null or empty, false otherwise
	 */
	public static boolean isNullOrEmpty(String string) {
		return string == null || string.isEmpty() || string.isBlank();
	}
}
