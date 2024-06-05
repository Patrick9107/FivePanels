package domain.User;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;


public class Password {

    private char[] hashedPassword;

    public Password(char[] rawPassword) {
        try {
            setHashedPassword(rawPassword);
        } finally {
            for (int i = 0; i < rawPassword.length; i++) {
                rawPassword[i] = 0;
            }
        }
    }

    public void setHashedPassword(char[] rawPassword) {
        try {
            if (isPasswordStrong(rawPassword)) {
                this.hashedPassword = hashPassword(rawPassword);
            } else {
                throw new UserException("setHashedPassword(): Password is not strong enough");
            }
        } finally {
            for (int i = 0; i < rawPassword.length; i++) {
                rawPassword[i] = 0;
            }
        }
    }

    public char[] getHashedPassword() {
        return hashedPassword;
    }

    public boolean isPasswordStrong(char[] password) {
        try {
            Zxcvbn zxcvbn = new Zxcvbn();
            Strength strength = zxcvbn.measure(java.nio.CharBuffer.wrap(password));
            return strength.getScore() >= 3;
        } finally {
            for (int i = 0; i < password.length; i++) {
                password[i] = 0;
            }
        }
    }

    public char[] hashPassword(char[] password) {
        try {
            hashedPassword = BCrypt.withDefaults().hashToChar(12, password);
            return hashedPassword;
        } finally {
            for (int i = 0; i < password.length; i++) {
                password[i] = 0;
            }
        }
    }

    public boolean checkPasswords(char[] password, char[] hashedPassword) {
        try {
            return BCrypt.verifyer().verify(password, hashedPassword).verified;
        } finally {
            for (int i = 0; i < password.length; i++) {
                password[i] = 0;
            }
        }
    }
}
