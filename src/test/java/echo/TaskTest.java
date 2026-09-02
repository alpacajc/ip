package echo;

import org.junit.jupiter.api.Test;

import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TaskTest {
    @Test
    public void deadlineTestDate() {
        Deadline deadline = new Deadline("do work", "2023-12-23");
        assertEquals("[D][ ] do work (By: 23 Dec 2023)", deadline.toString());
    }
    @Test
    public void deadlineTestDateTime() {
        Deadline deadline = new Deadline("do work", "2023-12-23", "1800");
        assertEquals("[D][ ] do work (By: 23 Dec 2023 0600pm)", deadline.toString());
    }
    @Test
    public void deadlineTestInvalidInput1() {
        assertThrows(DateTimeParseException.class, () -> new Deadline("do work", "203-1-23"));
    }
    @Test
    public void deadlineTestInvalidInput2() {
        assertThrows(DateTimeParseException.class, () -> new Deadline("do work", ""));
    }
    @Test
    public void deadlineTestInvalidInput3() {
        assertThrows(DateTimeParseException.class, () -> new Deadline("do work", ""));
    }
}
