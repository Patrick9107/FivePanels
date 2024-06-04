package domain.User;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;

import java.util.Arrays;

public class Password {

    // max 100 characters
    private char[] hashedPassword;

    public Password(char[] rawPassword) {
        try {
            setHashedPassword(rawPassword);
        } finally {
            Arrays.fill(rawPassword, '\0');
        }
    }

    public void setHashedPassword(char[] rawPassword) {
        try {
            this.hashedPassword = hashPassword(rawPassword);
        } finally {
            Arrays.fill(rawPassword, '\0');
        }
    }

    public char[] getHashedPassword() {
        return hashedPassword;
    }

    public boolean isPasswordStrong(String password) { // we have to look into CharSequence
        // found that CharSequence is not necessarily immutable (which means its internal state, unlike Strings, can change) which makes it a good candidate in this case for the password to not have anyone catch traces of it in the heap space
        Zxcvbn zxcvbn = new Zxcvbn();
        Strength strength = zxcvbn.measure(password);
        return strength.getScore() >= 3;
    }

    public char[] hashPassword(char[] password){
        try {
            hashedPassword = BCrypt.withDefaults().hashToChar(12, password);
            return hashedPassword;
        } finally {
            Arrays.fill(password, '\0');
        }
    }

    public boolean checkPasswords(char[] password, char[] hashedPassword){
        try {
            return BCrypt.verifyer().verify(password, hashedPassword).verified;
        } finally {
            Arrays.fill(password, '\0');
        }
    }
}
