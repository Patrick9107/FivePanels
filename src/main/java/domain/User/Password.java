package domain.User;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

public class Password {

    // max 100 characters
    private char[] hashedPassword;

    public boolean isPasswordStrong(String password){
        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);
        return strength.getScore() >= 3;
    }

    public char[] hashPassword(String password){

        hashedPassword = BCrypt.withDefaults().hashToChar(12, password.toCharArray());
        return hashedPassword;
    }

    public boolean checkPasswords(String password, char[] hashedPassword){
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
    }
}
