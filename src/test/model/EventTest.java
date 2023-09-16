package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Event class
 */
public class EventTest {
    private Event e1;
    private Event e2;
    private Date d;

    @BeforeEach
    public void runBefore() {
        e1 = new Event("Exercise Added to Workout");
        d = Calendar.getInstance().getTime();
    }

    @Test
    public void testEvent() {
        e1 = new Event("Exercise Added to Workout");
        d = Calendar.getInstance().getTime();

        assertEquals("Exercise Added to Workout", e1.getDescription());
        assertTrue(d.getTime() - e1.getDate().getTime() < 10);
    }

    @Test
    public void testEquals() {
        // testing other object null
        assertFalse(e1.equals(e2));

        // testing different classes
        String diff = "class";
        assertFalse(e1.equals(diff));

        // testing equals T && T
        Event e3 = new Event("Event...");
        Event e7 = new Event("Event...");
        assertTrue(e7.getDate().getTime() - e3.getDate().getTime() < 10);
        assertEquals(e7.getDescription(), e3.getDescription());
        assertTrue(e7.equals(e3));

        // testing equals T && F
        Event e4 = new Event("Event 1");
        Event e5 = new Event("Event 2");
        assertTrue(e5.getDate().getTime() - e4.getDate().getTime() < 10);
        assertNotEquals(e4.getDescription(), e5.getDescription());
        assertFalse(e4.equals(e5));

        // testing equals F && T
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Event e6 = new Event("Event 2");
        assertFalse(e6.getDate().getTime() - e5.getDate().getTime() < 10);
        assertEquals(e6.getDescription(), e5.getDescription());
        assertFalse(e6.equals(e5));

        // testing equals F && F
        assertTrue(e1.getDate().getTime() - e6.getDate().getTime() < 10);
        assertNotEquals(e1.getDescription(), e6.getDescription());
        assertFalse(e1.equals(e6));
    }

    @Test
    void testHashCode() {
        Event e4 = new Event("Event..");
        Event e5 = new Event("Event..");
        int hc4 = e4.hashCode();
        int hc5 = e5.hashCode();

        assertTrue(hc4 == hc5);

        int hc1 = e1.hashCode();
        assertFalse(hc1 == hc4);
    }

    @Test
    public void testToString() {
        assertEquals(d.toString() + "\n" + "Exercise Added to Workout", e1.toString());
    }
}
