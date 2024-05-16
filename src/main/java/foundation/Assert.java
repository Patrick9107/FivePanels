package foundation;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;
import java.util.function.Supplier;


/**
 * Assertion utility class that assists in validating arguments.
 *
 * We wil use it for ints, longs, Strings, Objects, Collections, etc.
 */

public abstract class Assert {


    // Null Assertions -------------------------------------------------------------

    // public static Object isNotNull(Object value, String paramName) {
    public static <T> T isNotNull(T value, String paramName) {
        if (value == null)
            throw new AssertException(STR."\{paramName} is null");
        return value;
    }

    public static <T> T isNull(T value, String paramName) {
        if (value != null)
            throw new AssertException(STR."\{paramName} is not null");
        return null;
    }


    // String Assertions -----------------------------------------------------------

    public static String isNotBlank(String value, String paramName) {
        isNotNull(value, paramName);

        if (value.isBlank())
            throw new AssertException(STR."\{paramName} is blank");

        return value;
    }

    public static String hasMaxLength(String value, int maxLength, String paramName) {

        isNotBlank(value, paramName);

        if (value.length() > maxLength)
            throw new AssertException(STR."\{paramName} is greater than \{maxLength}");

        return value;
    }

    // T extends Number & Comparable<T>

    private static <T extends Number, V extends Number> int compare(T value, V valueToProof) {

        BigDecimal newValueToProof = new BigDecimal(valueToProof.toString());
        BigDecimal newValue = new BigDecimal(value.toString());
        return newValue.compareTo(newValueToProof);
    }

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
    public static <T extends Number, V extends Number> T isEqualTo(T value, String valueName, V valueToProof, String proofName) {

        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        if (!(compare(value, valueToProof) == 0))
            throw new AssertException(STR."\{valueName} is not equal to \{proofName}");
        return value;
    }

    public static <T extends Number, V extends Number> T isGreaterThanOrEqual(T value, String valueName, V valueToProof, String proofName) {

        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        if ((compare(value, valueToProof) < 0))
            throw new AssertException(STR."\{valueName} is lower than \{proofName}");
        return value;
    }

    public static <T extends Number, V extends Number> T isLowerThanOrEqual(T value, String valueName, V valueToProof, String proofName) {
        isNotNull(value, valueName);
        isNotNull(valueToProof, proofName);

        if ((compare(value, valueToProof) > 0))
            throw new AssertException(STR."\{valueName} is greater than \{proofName}");
        return value;
    }

    // Collection Assertions -------------------------------------------------------

    public static <T extends Collection<?>> T hasMaxSize(T values, int maxLength, String valueName) {
        isNotNull(values, valueName);
        if (values.size() > maxLength) {
            throw new AssertException(STR."\{values} has more number of elements than \{maxLength}");
        }
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
    public static void isTrue(boolean expression, Supplier<String> errorMsg) {
        if (!expression)
            throw new AssertException(errorMsg.get());
    }
}
