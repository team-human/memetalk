package memetalk.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
    public DatabaseAdapter(ConfigReader configReader) throws Exception {
        connection =
                DriverManager.getConnection(
                        configReader.getConfig("db-url"),
                        configReader.getConfig("db-username"),
                        configReader.getConfig("db-password"));
    }

    /** Returns all memes. We only populate the url field of a meme for now. */
    public List<Meme> getMemes() throws Exception {
        ArrayList<Meme> memes = new ArrayList<Meme>();

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM meme;");
        while (result.next()) {
            memes.add(Meme.builder().url(result.getString("url")).build());
        }
        result.close();
        statement.close();

        return memes;
    }

    public void Shutdown() throws Exception {
        connection.close();
    }

    private Connection connection = null;
}
