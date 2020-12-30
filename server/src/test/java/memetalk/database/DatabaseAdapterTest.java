package memetalk.database;

import com.google.common.collect.ImmutableSet;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.xml.bind.DatatypeConverter;
import memetalk.ConfigReader;
import memetalk.model.Meme;
import memetalk.model.User;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * DatabaseAdapterTest tests the functionalities of DatabaseAdapter by injecting a fake in-memory
 * database. DatabaseAdapterTest connects to a fake database before running any test case,
 * genereates fake data before running each test case, and disconnects the database after running
 * all tests.
 */
public class DatabaseAdapterTest {

  private static Connection connection = null;
  private static ConfigReader configReader = null;
  private DatabaseAdapter databaseAdapter;

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
    databaseAdapter = new DatabaseAdapter(configReader);
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
    generateFakeMemeUserTable(statement);
    generateFakeMemeTable(statement);
    generateFakeMemeToTagTable(statement);
    statement.close();
    connection.commit();
  }

  private void generateFakeMemeUserTable(Statement statement) throws Exception {
    statement.execute("DROP TABLE meme_user IF EXISTS;");
    statement.execute(
        "CREATE TABLE meme_user (id SERIAL PRIMARY KEY, username VARCHAR(64), name VARCHAR(64), password VARCHAR(64), roles VARCHAR(128));");
    // password is hashed, so '$2a...UuU3a' is `1234`, and '$2a...n6.QG' is `abcd`
    statement.execute(
        "INSERT INTO meme_user (username, name, password, roles) VALUES ('john', 'Harry Potter', '$2a$10$w4Op9AHpvs.MMc0c.oZAQeYRKxd0qfom8YxRP5bYmE.doyagUuU3a', 'USER');");
    statement.execute(
        "INSERT INTO meme_user (username, name, password, roles) VALUES ('marry', 'Hermione Granger', '$2a$10$rXMooigm9.Zwtib6bIMnu.xoMH7gLvyVOa29yyc6Z2kqIejKn6.QG', 'USER');");
  }

  private void generateFakeMemeTable(Statement statement) throws Exception {
    statement.execute("DROP TABLE meme IF EXISTS;");
    statement.execute(
        "CREATE TABLE meme (id SERIAL PRIMARY KEY, image BYTEA, create_time TIMESTAMP, user_id INTEGER);");
    statement.execute(
        "INSERT INTO meme (image, create_time, user_id) VALUES (x'ABCD', {ts '2020-10-20 12:34:56.78'}, 1);");
    statement.execute(
        "INSERT INTO meme (image, create_time, user_id) VALUES (x'EF12', {ts '2020-10-20 12:34:57.87'}, 2);");
    statement.execute(
        "INSERT INTO meme (image, create_time, user_id) VALUES (x'DCBA', {ts '2020-10-20 12:21:12.34'}, 1);");
  }

  private void generateFakeMemeToTagTable(Statement statement) throws Exception {
    statement.execute("DROP TABLE meme_to_tag IF EXISTS;");
    statement.execute("CREATE TABLE meme_to_tag (meme_id INTEGER, tag VARCHAR(20));");
    statement.execute("INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'funny');");
    statement.execute("INSERT INTO meme_to_tag (meme_id, tag) VALUES (1, 'humor');");
    statement.execute("INSERT INTO meme_to_tag (meme_id, tag) VALUES (2, 'humor');");
  }

  @Test
  public void testGetMemesSucceed() throws URISyntaxException, SQLException {
    List<Meme> memes = databaseAdapter.getMemes();
    Assert.assertEquals(3, memes.size());
    Assert.assertEquals("ABCD", DatatypeConverter.printHexBinary(memes.get(0).getImage()));
    Assert.assertEquals("EF12", DatatypeConverter.printHexBinary(memes.get(1).getImage()));
    Assert.assertEquals("DCBA", DatatypeConverter.printHexBinary(memes.get(2).getImage()));
  }

  @Test
  public void testAddMemeSucceed() throws URISyntaxException, SQLException {
    byte[] imageBytes = "fake_image".getBytes();
    Meme newMeme = Meme.builder().image(imageBytes).build();

    databaseAdapter.addMeme(newMeme);

    List<Meme> allMemes = databaseAdapter.getMemes();
    Assert.assertEquals(4, allMemes.size());
    Assert.assertTrue(Arrays.equals(imageBytes, allMemes.get(3).getImage()));
  }

  @Test
  public void testGetMemesByTagSingleResultSucceed() throws URISyntaxException, SQLException {
    List<Meme> memes = databaseAdapter.getMemesByTag("funny");
    Assert.assertEquals(1, memes.size());
    Assert.assertEquals("1", memes.get(0).getId());
    Assert.assertEquals(2, memes.get(0).getTags().size());
    Assert.assertEquals("funny", memes.get(0).getTags().get(0));
    Assert.assertEquals("humor", memes.get(0).getTags().get(1));
    Assert.assertEquals("2020-10-20 12:34:56.78", memes.get(0).getCreateTime());
    Assert.assertEquals("Harry Potter", memes.get(0).getAuthor().getName());
  }

  @Test
  public void testGetMemesByTagMultipleResultSucceed() throws URISyntaxException, SQLException {
    List<Meme> memes = databaseAdapter.getMemesByTag("humor");
    Assert.assertEquals(2, memes.size());
  }

  @Test
  public void testGetMemesByUserIdSingleResultSucceed() throws URISyntaxException, SQLException {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
    List<Meme> memes = databaseAdapter.getMemesByUserId("2");
    Assert.assertEquals(1, memes.size());

    Assert.assertEquals("2", memes.get(0).getId());
    Assert.assertEquals(1, memes.get(0).getTags().size());
    Assert.assertEquals("humor", memes.get(0).getTags().get(0));
    Assert.assertEquals("2020-10-20 12:34:57.87", memes.get(0).getCreateTime());
    Assert.assertEquals("Hermione Granger", memes.get(0).getAuthor().getName());
  }

  @Test
  public void testGetMemesByUserIdMultipleResultSucceed() throws URISyntaxException, SQLException {
    DatabaseAdapter databaseAdapter = new DatabaseAdapter(configReader);
    List<Meme> memes = databaseAdapter.getMemesByUserId("1");
    Assert.assertEquals(2, memes.size());
  }

  @Test
  public void testGetTagsSucceed() throws URISyntaxException, SQLException {
    List<String> tags = databaseAdapter.getTags();
    Assert.assertEquals(2, tags.size());
    Assert.assertEquals("humor", tags.get(0));
    Assert.assertEquals("funny", tags.get(1));
  }

  @Test
  public void testCheckUserNameDoesExist() throws SQLException {
    Assert.assertTrue(databaseAdapter.checkUserNameExist("john"));
  }

  @Test
  public void testCheckUserNameNotExist() throws SQLException {
    Assert.assertFalse(databaseAdapter.checkUserNameExist("abbccddd"));
  }

  @Test
  public void testFindUserByUsernameDoesExit() throws SQLException {
    User user = databaseAdapter.findUserByUsername("john").get();
    Assert.assertEquals("Harry Potter", user.getName());
  }

  @Test
  public void testFindUserByUsernameNotExit() throws SQLException {
    Optional<User> user = databaseAdapter.findUserByUsername("adfdfd");
    Assert.assertFalse(user.isPresent());
  }

  @Test
  public void createUser() throws SQLException {
    final String password = "1234";
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    User user =
        User.builder()
            .password(passwordEncoder.encode(password))
            .roles(ImmutableSet.of("USER"))
            .username("username")
            .name("name")
            .build();

    databaseAdapter.createUser(user);
  }

  @Test
  public void testSerializeUserRolesWithTwoRoles() {
    Set<String> expectedRoles = ImmutableSet.of("USER", "ADMIN");
    Set<String> actualRoles =
        DatabaseAdapter.deserializeUserRoles(DatabaseAdapter.serializeUserRoles(expectedRoles));

    Assert.assertEquals(expectedRoles, actualRoles);
  }

  @Test
  public void testSerializeUserRolesEmpty() {
    Set<String> expectedRoles = ImmutableSet.of("USER");
    Set<String> actualRoles =
        DatabaseAdapter.deserializeUserRoles(DatabaseAdapter.serializeUserRoles(ImmutableSet.of()));
    Assert.assertEquals(expectedRoles, actualRoles);

    actualRoles = DatabaseAdapter.deserializeUserRoles("");
    Assert.assertEquals(expectedRoles, actualRoles);
  }
}
