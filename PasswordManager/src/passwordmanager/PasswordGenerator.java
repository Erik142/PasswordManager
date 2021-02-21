import java.security.SecureRandom;
import java.util.Random;

public class PasswordGenerator {        
    public static String generatePassword (int length, boolean lower,boolean upper, boolean num, boolean symb) {
        boolean pinCode=false;
    if(lower==false &&upper==false&&num==false&&symb==false) //if nothing is picked, then everything will be included. 
    {
        lower=true; upper=true; num=true; symb=true;
    }
    if((length<4&&num==true) && (lower==false && upper == false && symb == false))
    {
        pinCode=true; length=4;
    } 
    //minimum length of 6, unless it is a PIN code, then only 4 is acceptable (library cards etc.)
    if (length < 6 && pinCode==false) {
        length = 6;
    }

    String lowercase = "abcdefghijklmnopqrstuvwxyz";
    if(lower==false){lowercase="";}
    String uppercase = "ABCDEFGJKLMNPRSTUVWXYZ";
    if(upper==false){uppercase="";}
    String numbers = "0123456789";
    if(num==false){numbers="";}
    String symbols = "^$?!@#%&_-.,;:*)(][}{<>|";
    if(symb==false) {symbols="";}
    String allStr = lowercase+uppercase+numbers+symbols; //printing every type of character
    System.out.println(allStr);

    Random r = new SecureRandom(); //making it (more) secure. 

    StringBuilder pw = new StringBuilder();

    for (int i= 0; i < length; i++) {
    int k = r.nextInt(allStr.length()-1);   // random number between 0 and set.length()-1 inklusive
    pw.append(allStr.charAt(k));
    }
    String password = pw.toString();
    return password;
    }
}
