package memetalk;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ServerTest {
    @Test
    public void testServerHasAGreeting() {
        Server classUnderTest = new Server();
        assertNotNull("Server should have a greeting", classUnderTest.getGreeting());
    }
}
