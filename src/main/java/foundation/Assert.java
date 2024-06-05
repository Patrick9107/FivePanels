package foundation;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Supplier;


/**
 * Assertion utility class that assists in validating arguments.
 * <p>
 * We wil use it for ints, longs, Strings, Objects, Collections, etc.
 */

public abstract class Assert {


    // Null Assertions -------------------------------------------------------------


    /**
     * Checks if the given value is not null.
     *
     * @param <T>       the type of the value to be checked
     * @param value     the value to be checked for nullity
     * @param paramName the name of the parameter to be included in the exception message if the value is null
     * @return the provided value if it is not null
     * @throws AssertException if the provided value is null
     */
    public static <T> T isNotNull(T value, String paramName) {
        if (value == null)
            throw new AssertException(STR."\{paramName} is null");
        return value;
    }

    /**
     * Checks if the given value is null.
     *
     * @param <T>       the type of the value to be checked
     * @param value     the value to be checked for nullity
     * @param paramName the name of the parameter to be included in the exception message if the value is not null
     * @return the provided value if it is null
     * @throws AssertException if the provided value is not null
     */
    public static <T> T isNull(T value, String paramName) {
        if (value != null)
            throw new AssertException(STR."\{paramName} is not null");
        return null;
    }


    // String Assertions -----------------------------------------------------------

    /**
     * Checks if a given string is not blank (not empty and not only whitespace).
     * Throws an exception if the string is blank.
     *
     * @param value     the string to be checked
     * @param paramName the name of the parameter, used for the exception message
     * @return the original string if it is not blank
     * @throws AssertException if the string is blank or null
     */
    public static String isNotBlank(String value, String paramName) {
        isNotNull(value, paramName);

        if (value.isBlank())
            throw new AssertException(STR."\{paramName} is blank");

        return value;
    }

    /**
     * Checks if a given string does not exceed the specified maximum length.
     * Throws an exception if the string length is greater than the specified maximum length.
     *
     * @param value     the string to be checked
     * @param maxLength the maximum allowable length for the string
     * @param paramName the name of the parameter, used for the exception message
     * @return the original string if its length is within the maximum length
     * @throws AssertException if the string length is greater than the specified maximum length or if the string is blank or if the string is null
     */
    public static String hasMaxLength(String value, int maxLength, String paramName) {

        isNotBlank(value, paramName);

        if (value.length() > maxLength)
            throw new AssertException(STR."\{paramName} is greater than \{maxLength}");

        return value;
    }

    private static <T extends Number, V extends Number> int compare(T value, V valueToProof) {

        BigDecimal newValueToProof = new BigDecimal(valueToProof.toString());
        BigDecimal newValue = new BigDecimal(value.toString());
        return newValue.compareTo(newValueToProof);
    }

    /**
     * Checks if a given value is greater than another specified value.
     * Throws an exception if the value is not greater.
     *
     * @param value        the value to be checked
     * @param valueName    the name of the value, used for the exception message
     * @param valueToProof the value to compare against
     * @param proofName    the name of the value to compare against, used for the exception message
     * @return the original value if it is greater than the value to compare against
     * @throws AssertException if the value is equal to or less than the value to compare against or if any of the values are null
     */
    public static <T extends Number, V extends Number> T isGreaterThan(T value, String valueName, V valueToProof, String proofName) {

        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        try {
            isEqualTo(value, valueName, valueToProof, proofName);
            throw new AssertException(STR."\{valueName} is equal to \{proofName}");
        } catch (AssertException e) {

            if (!(compare(value, valueToProof) > 0))
                throw new AssertException(STR."\{valueName} is lower than or equal to \{proofName}");
            return value;
        }
    }

    /**
     * Checks if a given value is lower than another specified value.
     * Throws an exception if the value is not lower.
     *
     * @param value        the value to be checked
     * @param valueName    the name of the value, used for the exception message
     * @param valueToProof the value to compare against
     * @param proofName    the name of the value to compare against, used for the exception message
     * @return the original value if it is lower than the value to compare against
     * @throws AssertException if the value is equal to or greater than the value to compare against or if any of the values are null
     */
    public static <T extends Number, V extends Number> T isLowerThan(T value, String valueName, V valueToProof, String proofName) {

        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        try {
            isEqualTo(value, valueName, valueToProof, proofName);
            throw new AssertException(STR."\{valueName} is equal to \{proofName}");
        } catch (AssertException e) {

            if (!(compare(value, valueToProof) < 0))
                throw new AssertException(STR."\{valueName} is greater than \{proofName}");
            return value;
        }
    }

    /**
     * Checks if a given value is equal to another specified value.
     * Throws an exception if the values are not equal.
     *
     * @param value        the value to be checked
     * @param valueName    the name of the value, used for the exception message
     * @param valueToProof the value to compare against
     * @param proofName    the name of the value to compare against, used for the exception message
     * @return the original value if it is equal to the value to compare against
     * @throws AssertException if the value is not equal to the value to compare against or if any of the values are null
     */
    public static <T extends Number, V extends Number> T isEqualTo(T value, String valueName, V valueToProof, String proofName) {

        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        if (!(compare(value, valueToProof) == 0))
            throw new AssertException(STR."\{valueName} is not equal to \{proofName}");
        return value;
    }

    /**
     * Checks if a given value is greater than or equal to another specified value.
     * Throws an exception if the value is not greater or equal.
     *
     * @param value        the value to be checked
     * @param valueName    the name of the value, used for the exception message
     * @param valueToProof the value to compare against
     * @param proofName    the name of the value to compare against, used for the exception message
     * @return the original value if it is greater than or equal to the value to compare against
     * @throws AssertException if the value is less than the value to compare against or if any of the values are null
     */
    public static <T extends Number, V extends Number> T isGreaterThanOrEqual(T value, String valueName, V valueToProof, String proofName) {

        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        if ((compare(value, valueToProof) < 0))
            throw new AssertException(STR."\{valueName} is lower than \{proofName}");
        return value;
    }

    /**
     * Checks if a given value is lower than or equal to another specified value.
     * Throws an exception if the value is not lower or equal.
     *
     * @param value        the value to be checked
     * @param valueName    the name of the value, used for the exception message
     * @param valueToProof the value to compare against
     * @param proofName    the name of the value to compare against, used for the exception message
     * @return the original value if it is lower than or equal to the value to compare against
     * @throws AssertException if the value is greater than the value to compare against or if any of the values are null
     */
    public static <T extends Number, V extends Number> T isLowerThanOrEqual(T value, String valueName, V valueToProof, String proofName) {
        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        if ((compare(value, valueToProof) > 0))
            throw new AssertException(STR."\{valueName} is greater than \{proofName}");
        return value;
    }

    // Collection Assertions -------------------------------------------------------

    /**
     * Checks if a given collection does not exceed the specified maximum size.
     * Throws an exception if the collection size is greater than the specified maximum size.
     *
     * @param values    the collection to be checked
     * @param maxSize   the maximum allowable size for the collection
     * @param valueName the name of the collection, used for the exception message
     * @return the original collection if its size is within the maximum size
     * @throws AssertException if the collection size is greater than the specified maximum size or if the value is null
     */
    public static <T extends Collection<?>> T hasMaxSize(T values, int maxSize, String valueName) {
        isNotNull(values, valueName);
        if (values.size() > maxSize) {
            throw new AssertException(STR."\{valueName} has more number of elements than \{maxSize}");
        }
        return values;
    }

    /**
     * Checks if a given collection is not empty.
     * Throws an exception if the collection is empty.
     *
     * @param values    the collection to be checked
     * @param valueName the name of the collection, used for the exception message
     * @return the original collection if it is not empty
     * @throws AssertException if the collection is empty or if the value is null
     */
    public static <T extends Collection<?>> T isNotEmpty(T values, String valueName) {
        isNotNull(values, valueName);
        if (values.isEmpty())
            throw new AssertException(STR."\{valueName} is empty");
        return values;
    }

    // Expression Assertions -------------------------------------------------------

    // EAGER message evaluation:
    // Eager means that the error message is evaluated even if the expression is true
    // public static void isTrue(boolean expression, String errorMsg) {
    //     if (!expression)
    //         throw new AssertException(errorMsg);
    // }

    // LAZY message evaluation:
    // Lazy means that the error message is only evaluated if the expression is false

    /**
     * Checks if a given boolean expression is true.
     * Throws an exception if the expression is false.
     *
     * @param expression the boolean expression to be checked
     * @param errorMsg   the error message used for the exception if the expression is false
     * @throws AssertException if the expression is false
     */
    public static void isTrue(boolean expression, String errorMsg) {
        if (!expression)
            throw new AssertException(STR."\{errorMsg}");
    }

    /**
     * Checks if a given boolean expression is false.
     * Throws an exception if the expression is true.
     *
     * @param expression the boolean expression to be checked
     * @param errorMsg   the error message used for the exception if the expression is true
     * @throws AssertException if the expression is true
     */
    public static void isFalse(boolean expression, String errorMsg) {
        if (expression)
            throw new AssertException(STR."\{errorMsg}");
    }
}
