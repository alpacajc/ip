package echo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class EchoTest {
    @Test
    public void commandWordTest1() {
        assertEquals("BYE", Echo.CommandWord.fromString("bye").toString());
    }

    @Test
    public void commandWordTest2() {
        assertEquals("INVALID", Echo.CommandWord.fromString("").toString());
    }

    @Test
    public void parserTest1() {
        assertArrayEquals(new String[]{"get milk"}, Parser.getTaskArgs("todo get milk", "todo"));
    }

    @Test
    public void parserTest2() {
        assertArrayEquals(new String[]{"do task", "1212-12-12"},
                Parser.getTaskArgs("deadline do task /1212-12-12",
                "deadline"));
    }

    @Test
    public void parserTest3() {
        assertEquals(2,
                Parser.getTaskNum(3,
                        Parser.convertInputToArgs("delete 2")));
    }

    @Test
    public void parserTest4() {
        Parser.convertInputToArgs("mark 5");
        assertEquals(5,
                Parser.getTaskNum(7,
                        Parser.convertInputToArgs("delete 5")));
    }
}
