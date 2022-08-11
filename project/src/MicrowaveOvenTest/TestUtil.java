package MicrowaveOvenTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestUtil {

	public static void call_assertTrue(Object parameter) {
		assertTrue((boolean) parameter);
	}
	
	public static void call_assertFalse(Object parameter) {
		assertFalse((boolean) parameter);
	}
	
	public static void call_assertEquals(Object obj1, Object obj2) {
		assertEquals(obj1, obj2);
	}
	
	public static void call_assertNotEquals(Object obj1, Object obj2) {
		assertNotEquals(obj1, obj2);
	}
	
}
