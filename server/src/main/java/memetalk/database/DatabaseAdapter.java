package memetalk.database;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import memetalk.ConfigReader;
import memetalk.model.Meme;
import memetalk.model.User;
import org.springframework.stereotype.Repository;

/**
 * DatabaseAdapter keeps a connection with the database and offers methods to read/write the
 * database. Always call `Shutdown()` after you're done using this adapter to safely close the
 * connection with the database. TODO: Add lock on each public method to promise data consistency.
 */
@Slf4j
@Repository
public class DatabaseAdapter {
  private final Connection connection;

  public DatabaseAdapter(ConfigReader configReader) throws URISyntaxException, SQLException {
    String database_url = System.getenv("DATABASE_URL");
    if (database_url != null && database_url != "") {
      log.info("Connecting to database from env url.");
      connection = getConnectionFromDatabaseUrl(database_url);
    } else {
      log.info("Connecting to database from config reader.");
      connection = getConnectionFromConfigReader(configReader);
    }
  }

  private static Connection getConnectionFromDatabaseUrl(String database_url)
      throws URISyntaxException, SQLException {
    URI dbUri = new URI(database_url);

    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

    return DriverManager.getConnection(dbUrl, username, password);
  }

  private static Connection getConnectionFromConfigReader(ConfigReader configReader)
      throws SQLException {
    return DriverManager.getConnection(
        configReader.getConfig("db-url"),
        configReader.getConfig("db-username"),
        configReader.getConfig("db-password"));
  }

  /**
   * Returns all memes. DEPRECATED: Reasons are (1) it populates incompleted fields of a meme, (2)
   * it shows unfiltered data and they will be too many.
   */
  public List<Meme> getMemes() throws SQLException {
    List<Meme> memes = new ArrayList<>();

    Statement statement = connection.createStatement();
    ResultSet result = statement.executeQuery("SELECT image FROM meme;");
    while (result.next()) {
      memes.add(Meme.builder().image(result.getBytes("image")).build());
    }
    result.close();
    statement.close();

    return memes;
  }

  /** Returns all tags ordered by popularity. */
  public List<String> getTags() throws SQLException {
    List<String> tags = new ArrayList<>();

    Statement statement = connection.createStatement();
    ResultSet result =
        statement.executeQuery(
            "SELECT tag, COUNT(*) amount FROM meme_to_tag GROUP BY tag ORDER BY amount DESC;");
    while (result.next()) {
      tags.add(result.getString("tag"));
    }
    result.close();
    statement.close();

    return tags;
  }

  /** Returns all memes that has the given tag. */
  public List<Meme> getMemesByTag(String tag) throws SQLException {
    return getMemesByIds(getMemeIdsByTag(tag));
  }

  /** Adds a new meme with an attached image. */
  public void addMeme(Meme meme) throws SQLException {
    PreparedStatement statement =
        connection.prepareStatement("INSERT INTO meme(image) VALUES (?);");
    statement.setBytes(/*image*/ 1, meme.getImage());
    statement.executeUpdate();
    statement.close();
  }

  public void shutdown() throws Exception {
    connection.close();
  }

  private List<String> getMemeIdsByTag(String tag) throws SQLException {
    List<String> meme_ids = new ArrayList<>();

    PreparedStatement statement =
        connection.prepareStatement("SELECT meme_id FROM meme_to_tag WHERE tag = ?;");
    statement.setString(1, tag);
    ResultSet result = statement.executeQuery();
    while (result.next()) {
      meme_ids.add(Integer.toString(result.getInt("meme_id")));
    }
    result.close();
    statement.close();

    return meme_ids;
  }

  private List<Meme> getMemesByIds(List<String> meme_ids) throws SQLException {
    List<Meme> memes = new ArrayList<>();

    Statement statement = connection.createStatement();
    ResultSet result =
        statement.executeQuery(
            "SELECT id, image, create_time, user_id FROM meme WHERE id IN ("
                + String.join(",", meme_ids)
                + ");");
    while (result.next()) {
      User author = getUserById(result.getInt("user_id"));
      memes.add(
          Meme.builder()
              .id(Integer.toString(result.getInt("id")))
              .author(author)
              .image(result.getBytes("image"))
              .createTime(result.getString("create_time"))
              .build());
    }
    result.close();
    statement.close();

    fillTagsIntoMeme(memes);

    return memes;
  }

  private List<Meme> fillTagsIntoMeme(List<Meme> memes) throws SQLException {
    Map<String, List<String>> meme_id_to_tag = new HashMap<>();

    List<String> meme_ids = new ArrayList<>();
    for (Meme meme : memes) {
      meme_ids.add(meme.getId());
    }

    Statement statement = connection.createStatement();
    ResultSet result =
        statement.executeQuery(
            "SELECT meme_id, tag FROM meme_to_tag WHERE meme_id IN ("
                + String.join(",", meme_ids)
                + ");");
    while (result.next()) {
      String meme_id = Integer.toString(result.getInt("meme_id"));
      String tag = result.getString("tag");
      meme_id_to_tag.computeIfAbsent(meme_id, k -> new ArrayList<>()).add(tag);
    }
    result.close();
    statement.close();

    for (Meme meme : memes) {
      if (!meme_id_to_tag.containsKey(meme.getId())) {
        continue;
      }
      for (String tag : meme_id_to_tag.get(meme.getId())) {
        meme.getTags().add(tag);
      }
    }
    return memes;
  }

  private User getUserById(int id) throws SQLException {
    PreparedStatement statement =
        connection.prepareStatement("SELECT id, name FROM meme_user WHERE id = ?;");
    statement.setInt(1, id);
    ResultSet result = statement.executeQuery();

    User user = null;
    if (result.next()) {
      user =
          User.builder()
              .id(Integer.toString(result.getInt("id")))
              .name(result.getString("name"))
              .build();
    }

    // If we didn't find 'exact one' user with the given id, we log error and
    // continue.
    if (result.next() || user == null) {
      log.error("Failed to find exact one user record with id={}", id);
    }

    result.close();
    statement.close();

    return user;
  }
}
