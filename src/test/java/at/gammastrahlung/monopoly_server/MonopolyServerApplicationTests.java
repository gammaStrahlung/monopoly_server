package at.gammastrahlung.monopoly_server;

import at.gammastrahlung.monopoly_server.network.websocket.MonopolyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MonopolyServerApplicationTests {

	@Autowired
	private MonopolyController monopolyController;

	@Test
	void contextLoads() {
        assertNotNull(monopolyController);
	}

}
