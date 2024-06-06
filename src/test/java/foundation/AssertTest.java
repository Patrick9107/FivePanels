package foundation;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static foundation.Assert.*;
import static org.junit.jupiter.api.Assertions.*;

class AssertTest {

    @Test
    void isNotNull_shouldReturnValue_WhenNotNullPassed() {
        try {
            String value = "test";
            assertEquals("test", isNotNull(value, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isNotNull_shouldThrow_WhenNullIsPassed() {
        try {
            String value = null;
            assertThrowsExactly(AssertException.class, () -> isNotNull(value, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }

    }

    @Test
    void isNotBlank_shouldThrow_WhenValueIsOnlyWhitespaces() {

        try {
            String value = "        ";
            assertThrowsExactly(AssertException.class, () -> isNotBlank(value, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isNotBlank_shouldThrow_WhenValueIsEmpty() {
        try {
            String value = "";
            assertThrowsExactly(AssertException.class, () -> isNotBlank(value, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isNotBlank_shouldThrow_WhenValueIsNull() {
        try {
            String value = null;
            assertThrowsExactly(AssertException.class, () -> isNotBlank(value, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isNotBlank_shouldReturnValue_WhenValueIsNotBlank() {
        try {
            String value = "test";
            assertEquals(value, isNotBlank(value, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxLength_shouldReturnValue_WhenValueLengthIsEqualToMaxLength() {
        try {
            String value = "123456789";
            assertEquals(value, hasMaxLength(value, 9, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxLength_shouldThrow_WhenValueLengthIsGreaterThanMaxLength() {
        try {
            String value = "123456789";
            assertThrowsExactly(AssertException.class, () -> hasMaxLength(value, 8, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxLength_shouldReturnValue_WhenValueLengthIsLowerThanMaxLength() {
        try {
            String value = "123456789";
            assertEquals(value, hasMaxLength(value, 10, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxLength_shouldThrow_WhenNullIsPassed() {
        try {
            String value = null;
            assertThrowsExactly(AssertException.class, () -> hasMaxLength(value, 8, "testString"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThanOrEqual_shouldReturnValue_WhenFirstValueIsGreaterThanOtherValue() {
        try {
            int value = 5;
            int value2 = 3;

            assertEquals(value, isGreaterThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThanOrEqual_shouldReturnValue_WhenFirstValueIsEqualToOtherValue() {
        try {
            int value = 5;
            int value2 = 5;

            assertEquals(value, isGreaterThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThanOrEqual_shouldThrow_WhenFirstValueIsLowerThanOtherValue() {
        try {
            int value = 3;
            int value2 = 5;

            assertThrowsExactly(AssertException.class, () -> isGreaterThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThanOrEqual_shouldReturnValue_WhenComparingDoubleWithIntAndFirstValueIsGreaterThanOtherValue() {
        try {
            double value = 3.8;
            int value2 = 3;

            assertEquals(value, isGreaterThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThanOrEqual_shouldReturnValue_WhenFirstValueIsLowerThanOtherValue() {
        try {
            int value = 3;
            int value2 = 5;

            assertEquals(value, isLowerThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThanOrEqual_shouldReturnValue_WhenFirstValueIsEqualToOtherValue() {
        try {
            int value = 5;
            int value2 = 5;

            assertEquals(value, isLowerThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThanOrEqual_shouldThrow_WhenFirstValueIsGreaterThanOtherValue() {
        try {
            int value = 5;
            int value2 = 3;

            assertThrowsExactly(AssertException.class, () -> isLowerThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThanOrEqual_shouldReturnValue_WhenComparingDoubleWithIntAndFirstValueIsLowerThanOtherValue() {
        try {
            double value = 3.897;
            int value2 = 5;

            assertEquals(value, isLowerThanOrEqual(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isEqualTo_shouldReturnValue_WhenFirstValueIsEqualToOtherValue() {
        try {
            int value = 5;
            int value2 = 5;

            assertEquals(value, isEqualTo(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isEqualTo_shouldThrow_WhenFirstValueIsNotEqualToOtherValue() {
        try {
            double value = 5.122149;
            double value2 = 5.122148;

            assertThrowsExactly(AssertException.class, () -> isEqualTo(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldThrow_WhenValueIsNull() {
        try {
            String valueName = "value";
            double lowerValue = 0;
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";

            assertThrowsExactly(AssertException.class, () -> isBetween(null, valueName, lowerValue, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldThrow_WhenLowerValueIsNull() {
        try {
            double value = 15;
            String valueName = "value";
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";

            assertThrowsExactly(AssertException.class, () -> isBetween(value, valueName, null, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldThrow_WhenHigherValueIsNull() {
        try {
            double value = 15;
            String valueName = "value";
            double lowerValue = 0;
            String lowerName = "lowerValue";
            String higherName = "higherValue";

            assertThrowsExactly(AssertException.class, () -> isBetween(value, valueName, null, lowerName, null, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldThrow_WhenValueIsLowerThanLowerValue() {
        try {
            double value = -0.0000001;
            String valueName = "value";
            double lowerValue = 0;
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";

            assertThrowsExactly(AssertException.class, () -> isBetween(value, valueName, lowerValue, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldThrow_WhenValueIsHigherThanHigherValue() {
        try {
            double value = 100.000000001;
            String valueName = "value";
            double lowerValue = 0;
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";

            assertThrowsExactly(AssertException.class, () -> isBetween(value, valueName, lowerValue, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldReturnValue_WhenValueIsEqualToHigherValue() {
        try {
            double value = 100.00000000;
            String valueName = "value";
            double lowerValue = 0;
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";
            assertEquals(value, isBetween(value, valueName, lowerValue, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldReturnValue_WhenValueIsEqualToLowerValue() {
        try {
            double value = 0.00000000000;
            String valueName = "value";
            double lowerValue = 0;
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";
            assertEquals(value, isBetween(value, valueName, lowerValue, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldReturnValue_WhenValueIsAnyNumberBetweenLowerValueAndHigherValue() {
        try {
            float value = 37.1284f;
            String valueName = "value";
            double lowerValue = 0;
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";
            assertEquals(value, isBetween(value, valueName, lowerValue, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isBetween_shouldThrow_WhenLowerValueIsGreaterThanHigherValue() {
        try {
            double value = 100;
            String valueName = "value";
            double lowerValue = 100.00000001;
            String lowerName = "lowerValue";
            double higherValue = 100;
            String higherName = "higherValue";
            assertThrowsExactly(AssertException.class, () -> isBetween(value, valueName, lowerValue, lowerName, higherValue, higherName));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isTrue_shouldNotThrow_WhenExpressionPassedIsTrue() {
        try {
            boolean bool = true;
            isTrue(bool, bool + "is not true");
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isTrue_shouldThrow_WhenExpressionPassedIsFalse() {
        try {
            boolean bool = false;
            assertThrowsExactly(AssertException.class, () -> isTrue(bool, STR."\{bool} is not true"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isFalse_shouldNotThrow_WhenExpressionPassedIsFalse() {
        try {
            boolean bool = false;
            isFalse(false, bool + "is true");
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isFalse_shouldThrow_WhenExpressionPassedIsTrue() {
        try {
            boolean bool = true;
            assertThrowsExactly(AssertException.class, () -> isFalse(bool, STR."\{bool} is is not false"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThan_shouldThrow_WhenFirstValueIsEqualToOtherValue() {
        try {
            int value = 5;
            int value2 = 5;

            assertThrowsExactly(AssertException.class, () -> isGreaterThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThan_shouldReturnValue_WhenFirstValueIsGreaterThanOtherValue() {
        try {
            int value = 5;
            int value2 = 4;

            assertEquals(value, isGreaterThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThan_shouldThrow_WhenFirstValueIsLowerThanOtherValue() {
        try {
            int value = 4;
            int value2 = 5;

            assertThrowsExactly(AssertException.class, () -> isGreaterThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isGreaterThan_shouldThrow_WhenNullIsPassed() {
        try {
            Double value = null;
            int value2 = 5;

            assertThrowsExactly(AssertException.class, () -> isGreaterThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThan_shouldThrow_WhenFirstValueIsEqualToOtherValue() {
        try {
            int value = 5;
            int value2 = 5;

            assertThrowsExactly(AssertException.class, () -> isLowerThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThan_shouldReturnValue_WhenFirstValueIsLowerThanOtherValue() {
        try {
            int value = 4;
            int value2 = 5;

            assertEquals(value, isLowerThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThan_shouldThrow_WhenFirstValueIsGreaterThanOtherValue() {
        try {
            int value = 6;
            int value2 = 3;

            assertThrowsExactly(AssertException.class, () -> isLowerThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isLowerThan_shouldThrow_WhenNullIsPassed() {
        try {
            Double value = null;
            int value2 = 5;

            assertThrowsExactly(AssertException.class, () -> isLowerThan(value, "value", value2, "value2"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxSize_shouldThrow_WhenSizeIsGreaterThanMaxLength() {
        try {
            HashSet<Integer> value = new HashSet<>();
            value.add(1);
            value.add(2);
            value.add(3);
            value.add(4);
            value.add(5);
            value.add(6);

            assertThrowsExactly(AssertException.class, () -> hasMaxSize(value, 5, "hashset"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxSize_shouldThrow_WhenNullIsPassed() {
        try {
            HashSet<Integer> value = null;

            assertThrowsExactly(AssertException.class, () -> hasMaxSize(value, 5, "hashset"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxSize_shouldReturnValues_WhenSizeIsEqualToMaxLength() {
        try {
            HashSet<Integer> values = new HashSet<>();
            values.add(1);
            values.add(2);
            values.add(3);
            values.add(4);
            values.add(5);

            assertEquals(values, hasMaxSize(values, 5, "hashset"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxSize_shouldReturnValues_WhenSizeIsLowerThanMaxLength() {
        try {
            HashSet<Integer> values = new HashSet<>();
            values.add(1);
            values.add(2);
            values.add(3);
            values.add(4);

            assertEquals(values, hasMaxSize(values, 5, "hashset"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void hasMaxSize_shouldReturnValues_WhenValueIsEmpty() {
        try {
            HashSet<Integer> values = new HashSet<>();

            assertEquals(values, hasMaxSize(values, 5, "hashset"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isNotEmpty_shouldThrow_WhenNullIsPassed() {
        try {
            HashSet<Integer> values = new HashSet<>();

            assertThrowsExactly(AssertException.class, () -> isNotEmpty(null, "values"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isNotEmpty_shouldReturnValues_WhenCollectionIsNotEmpty() {
        try {
            HashSet<Integer> values = new HashSet<>();

            values.add(2);

            assertThrowsExactly(AssertException.class, () -> isNotEmpty(null, "values"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }

    @Test
    void isNotEmpty_shouldThrow_WhenCollectionIsEmpty() {
        try {
            HashSet<Integer> values = new HashSet<>();

            assertThrowsExactly(AssertException.class, () -> isNotEmpty(values, "values"));
        } catch (Exception ex) {
            System.out.println("Unexpected Exception: " + ex.getMessage());
            fail();
        }
    }
}