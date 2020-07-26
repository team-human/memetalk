package memetalk;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ServerTest {
    @Test
    public void testNothing() {
        log.info("Test slf4j");
    }
}
