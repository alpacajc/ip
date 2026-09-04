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
        Parser parser = new Parser();
        assertArrayEquals(new String[]{"get milk"}, parser.parseTask("todo get milk", "todo"));
    }

    @Test
    public void parserTest2() {
        Parser parser = new Parser();
        assertArrayEquals(new String[]{"do task", "1212-12-12"}, parser.parseTask("deadline do task /1212-12-12",
                "deadline"));
    }

    @Test
    public void parserTest3() {
        Parser parser = new Parser();
        parser.parseInput("delete 2");
        assertEquals(2, parser.parseTaskNum());
    }

    @Test
    public void parserTest4() {
        Parser parser = new Parser();
        parser.parseInput("mark 5");
        assertEquals(5, parser.parseTaskNum());
    }
}
