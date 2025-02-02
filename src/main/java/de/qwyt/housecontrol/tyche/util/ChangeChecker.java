package de.qwyt.housecontrol.tyche.util;

/**
 * A utility class that provides methods for comparing two values of the same type.
 * This class contains helper methods to check if the values have changed,
 * ensuring that null values are properly handled in comparison logic.
 */
public class ChangeChecker {
	/**
	 * Compares two values to determine if they have changed. 
	 * A change is detected if the new value is not equal to the old value, 
	 * and the new value is not null.
	 * 
	 * @param <T> The type of the values being compared.
	 * @param oldValue The original value to compare against.
	 * @param newValue The new value to compare with the old value.
	 * 
	 * @return {@code true} if the values are different and the new value is not null,
	 *         {@code false} if the values are the same or the new value is null.
	 */
	public static <T> boolean hasChanged(T oldValue, T newValue) {
		if (oldValue == null && newValue == null) return false;
		else if (oldValue == null && newValue != null) return true;
		else if (newValue == null) return false;
		else return !oldValue.equals(newValue);
	}
}
