package domain.User;

import org.apache.commons.validator.routines.EmailValidator;

public class Email {

    // not null, max 64 characters before "@", max 255 characters after "@", min 3 characters including the "@", has to include "@"
    private String address;

    public Email(String address) {
        setAddress(address);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        // not null assert in EmailValidator
        if (!(EmailValidator.getInstance().isValid(address)))
            throw new UserException("setAddress(): address is not valid");
        this.address = address;
    }
}
