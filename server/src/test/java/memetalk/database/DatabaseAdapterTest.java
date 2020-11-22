package memetalk.database;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import memetalk.ConfigReader;
import memetalk.model.Meme;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * DatabaseAdapterTest tests the functionalities of DatabaseAdapter by injecting
 * a fake in-memory database. DatabaseAdapterTest connects to a fake database
 * before running any test case, genereates fake data before running each test
 * case, and disconnects the database after running all tests.
 */
public class DatabaseAdapterTest {

  private static Connection connection = null;
  private static ConfigReader configReader = null;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    configReader = ConfigReader.getInstance();
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
    connection = DriverManager.getConnection(
        configReader.getConfig("db-url"), configReader.getConfig("db-username"),
        configReader.getConfig("db-password"));
  }

  private static void disconnectFromFakeDatabase() throws Exception {
    connection.close();
  }

  private void generateFakeData() throws Exception {
    Statement statement = connection.createStatement();
    generateFakeMemeTable(statement);
    generateFakeMemeToTagTable(statement);
    statement.close();
    connection.commit();
  }

  private void generateFakeMemeTable(Statement statement) throws Exception {
    statement.execute("DROP TABLE meme IF EXISTS;");
    statement.execute(
        "CREATE TABLE meme (id SERIAL PRIMARY KEY, image BYTEA);");
    statement.execute("INSERT INTO meme (image) VALUES (x'ABCD');");
    statement.execute("INSERT INTO meme (image) VALUES (x'EF12');");
  }

  private void generateFakeMemeToTagTable(Statement statement)
      throws Exception {
    statement.execute("DROP TABLE meme_to_tag IF EXISTS;");
    statement.execute(
        "CREATE TABLE meme_to_tag (meme_id INTEGER, tag VARCHAR(20));");
    statement.execute(
        "INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'funny');");
    statement.execute(
        "INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'humor');");
    statement.execute(
        "INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'humor');");
  }

  @Test
  public void testGetMemesSucceed() throws URISyntaxException, SQLException {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
    List<Meme> memes = databaseAdapter.getMemes();
    Assert.assertEquals(2, memes.size());
    Assert.assertEquals(
        "ABCD", DatatypeConverter.printHexBinary(memes.get(0).getImage()));
    Assert.assertEquals(
        "EF12", DatatypeConverter.printHexBinary(memes.get(1).getImage()));
  }

  @Test
  public void testAddMemeSucceed() throws URISyntaxException, SQLException {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
    byte[] imageBytes = "fake_image".getBytes();
    Meme newMeme = Meme.builder().image(imageBytes).build();

    databaseAdapter.addMeme(newMeme);

    List<Meme> allMemes = databaseAdapter.getMemes();
    Assert.assertEquals(3, allMemes.size());
    Assert.assertTrue(Arrays.equals(imageBytes, allMemes.get(2).getImage()));
  }

  @Test
  public void testGetMemesByTagSingleResultSucceed()
      throws URISyntaxException, SQLException {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
    List<Meme> memes = databaseAdapter.getMemesByTag("funny");
    Assert.assertEquals(1, memes.size());
    Assert.assertEquals("1", memes.get(0).getId());
    Assert.assertEquals(2, memes.get(0).getTags().size());
    Assert.assertEquals("funny", memes.get(0).getTags().get(0));
    Assert.assertEquals("humor", memes.get(0).getTags().get(1));
  }

  @Test
  public void testGetMemesByTagMultipleResultSucceed()
      throws URISyntaxException, SQLException {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
    List<Meme> memes = databaseAdapter.getMemesByTag("humor");
    Assert.assertEquals(2, memes.size());
  }

  @Test
  public void testGetTagsSucceed() throws URISyntaxException, SQLException {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
    List<String> tags = databaseAdapter.getTags();
    Assert.assertEquals(2, tags.size());
    Assert.assertEquals("humor", tags.get(0));
    Assert.assertEquals("funny", tags.get(1));
  }
}
