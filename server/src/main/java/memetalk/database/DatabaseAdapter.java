package memetalk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
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

  /** Returns all memes. We only populate the url field of a meme for now. */
  public List<Meme> getMemes() throws SQLException {
    ArrayList<Meme> memes = new ArrayList<Meme>();

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
}
