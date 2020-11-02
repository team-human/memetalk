package memetalk.database;

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
import memetalk.ConfigReader;
import memetalk.model.Meme;

/**
 * DatabaseAdapter keeps a connection with the database and offers methods to read/write the
 * database. Always call `Shutdown()` after you're done using this adapter to safely close the
 * connection with the database.
 */
public class DatabaseAdapter {

  private Connection connection = null;

  public DatabaseAdapter(ConfigReader configReader) throws SQLException {
    connection =
        DriverManager.getConnection(
            configReader.getConfig("db-url"),
            configReader.getConfig("db-username"),
            configReader.getConfig("db-password"));
  }

  /** Returns all memes. */
  public List<Meme> getMemes() throws SQLException {
    List<Meme> memes = new ArrayList<>();

    Statement statement = connection.createStatement();
    ResultSet result = statement.executeQuery("SELECT url, image FROM meme;");
    while (result.next()) {
      memes.add(
          Meme.builder().url(result.getString("url")).image(result.getBytes("image")).build());
    }
    result.close();
    statement.close();

    return memes;
  }

  public List<Meme> getMemesByTag(String tag) throws SQLException {
    return getMemesByIds(getMemeIds(tag));
  }

  /** Add a new meme with an attached image. */
  public void addMeme(Meme meme) throws SQLException {
    PreparedStatement statement =
        connection.prepareStatement("INSERT INTO meme(url, image) VALUES (?, ?);");
    statement.setString(/*url*/ 1, meme.getUrl());
    statement.setBytes(/*image*/ 2, meme.getImage());
    statement.executeUpdate();
    statement.close();
  }

  public void shutdown() throws Exception {
    connection.close();
  }

  private List<String> getMemeIds(String tag) throws SQLException {
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
            "SELECT id, image FROM meme WHERE id IN (" + joinWithComma(meme_ids) + ");");
    while (result.next()) {
      memes.add(
          Meme.builder()
              .id(Integer.toString(result.getInt("id")))
              .image(result.getBytes("image"))
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
                + joinWithComma(meme_ids)
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

  private String joinWithComma(List<String> tokens) {
    if (tokens.size() == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < tokens.size() - 1; i++) {
      sb.append(tokens.get(i) + ",");
    }
    sb.append(tokens.get(tokens.size() - 1));
    return sb.toString();
  }
}
