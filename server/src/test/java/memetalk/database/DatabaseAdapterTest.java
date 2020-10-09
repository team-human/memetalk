package memetalk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import memetalk.ConfigReader;
import memetalk.ConfigReader.DeploymentEnvironment;
import memetalk.model.Meme;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DatabaseAdapterTest tests the functionalities of DatabaseAdapter by injecting a fake in-memory
 * database. DatabaseAdapterTest connects to a fake database before running any test case,
 * genereates fake data before running each test case, and disconnects the database after running
 * all tests.
 */
public class DatabaseAdapterTest {
    @BeforeClass
    public static void setUpBeforeClass() {
        configReader = new ConfigReader(DeploymentEnvironment.TEST);
        connectToFakeDatabase();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        disconnectFromFakeDatabase();
    }

    @Before
    public void setUp() {
        generateFakeData();
    }

    private static void connectToFakeDatabase() {
        try {
            connection =
                    DriverManager.getConnection(
                            configReader.getConfig("db-url"),
                            configReader.getConfig("db-username"),
                            configReader.getConfig("db-password"));
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception.");
        }
    }

    private static void disconnectFromFakeDatabase() {
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception.");
        }
    }

    private void generateFakeData() {
        try {
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE meme IF EXISTS;");
            statement.execute("CREATE TABLE meme (url TEXT);");
            statement.execute("INSERT into meme (url) VALUES ('http://www.happy.com');");
            statement.close();
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception.");
        }
    }

    @Test
    public void testSelectSucceed() {
        try {
            DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
            List<Meme> memes = databaseAdapter.getMemes();
            Assert.assertEquals(memes.size(), 1);
            Assert.assertEquals(memes.get(0).getUrl(), "http://www.happy.com");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Unexcepted exception.");
        }
    }

    private static Connection connection = null;
    private static ConfigReader configReader = null;
}
