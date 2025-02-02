package de.qwyt.housecontrol.tyche.util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ChangeCheckerTest {
	
	@Test
	public void testHasChanged_ValueChanged() {
		String oldValue = "a";
		String newValue = "b";
		
		boolean result = ChangeChecker.hasChanged(oldValue, newValue);
		
		assertTrue(result, "Expected true when values are different");
	}
	
	@Test
	public void testHasChanged_ValueNotChanged() {
		String oldValue = "a";
		String newValue = "a";
		
		boolean result = ChangeChecker.hasChanged(oldValue, newValue);
		
		assertFalse(result, "Expected false when values are the same");
	}
	
	@Test
	public void testHasChanged_ValueNewValueNull() {
		String oldValue = "a";
		String newValue = null;
		
		boolean result = ChangeChecker.hasChanged(oldValue, newValue);
		
		assertFalse(result, "Expected false when new value is null and old value is not null");
	}
	
	@Test
	public void testHasChanged_ValueOldValueNull() {
		String oldValue = null;
		String newValue = "b";
		
		boolean result = ChangeChecker.hasChanged(oldValue, newValue);
		
		assertTrue(result, "Expected true when old value is null and new value not null");
	}
	
	@Test
	public void testHasChanged_BothValuesNull() {
		String oldValue = null;
		String newValue = null;
		
		boolean result = ChangeChecker.hasChanged(oldValue, newValue);
		
		assertFalse(result, "Expected false when both values are null");
	}

}
