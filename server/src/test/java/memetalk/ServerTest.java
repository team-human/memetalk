package memetalk;

import org.junit.Test;
import static org.junit.Assert.*;

public class ServerTest {
    @Test public void testServerHasAGreeting() {
        Server classUnderTest = new Server();
        assertNotNull("Server should have a greeting", classUnderTest.getGreeting());
    }
}
