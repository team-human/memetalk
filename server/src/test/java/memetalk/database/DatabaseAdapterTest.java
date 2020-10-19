package memetalk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
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

    private static Connection connection = null;
    private static ConfigReader configReader = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        configReader = new ConfigReader(DeploymentEnvironment.TEST);
        connectToFakeDatabase();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        disconnectFromFakeDatabase();
    }

    @Before
    public void setUp() throws Exception {
        generateFakeData();
    }

    private static void connectToFakeDatabase() throws Exception {
        connection =
                DriverManager.getConnection(
                        configReader.getConfig("db-url"),
                        configReader.getConfig("db-username"),
                        configReader.getConfig("db-password"));
    }

    private static void disconnectFromFakeDatabase() throws Exception {
        connection.close();
    }

    private void generateFakeData() throws Exception {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE meme IF EXISTS;");
        statement.execute("CREATE TABLE meme (url TEXT, image BYTEA);");
        statement.execute(
                "INSERT into meme (url, image) VALUES ('http://www.happy.com', x'ABCD');");
        statement.close();
        connection.commit();
    }

    @Test
    public void testSelectSucceed() throws SQLException {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
        List<Meme> memes = databaseAdapter.getMemes();
        Assert.assertEquals(memes.size(), 1);
        Assert.assertEquals(memes.get(0).getUrl(), "http://www.happy.com");
        Assert.assertEquals(memes.get(0).getImage().length, 2);
        Assert.assertEquals(DatatypeConverter.printHexBinary(memes.get(0).getImage()), "ABCD");
    }

    @Test
    public void testAddMemeSucceed() throws SQLException {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
        byte[] imageBytes = "fake_image".getBytes();
        String url = "http://www.sad.com";
        Meme newMeme = Meme.builder().url(url).image(imageBytes).build();

        databaseAdapter.addMeme(newMeme);

        List<Meme> allMemes = databaseAdapter.getMemes();
        Assert.assertEquals(allMemes.size(), 2);
        Assert.assertEquals(allMemes.get(1).getUrl(), url);
        Assert.assertTrue(Arrays.equals(allMemes.get(1).getImage(), imageBytes));
    }
}
