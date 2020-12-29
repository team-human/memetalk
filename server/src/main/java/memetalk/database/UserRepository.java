package memetalk.database;

import java.sql.SQLException;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.exception.SQLExecutionException;
import memetalk.model.User;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
  @NonNull private final DatabaseAdapter databaseAdapter;

  public Optional<User> findUserByUserName(@NonNull final String userName) {
    try {
      return databaseAdapter.findUserByUserName(userName);
    } catch (SQLException ex) {
      log.error("Find user by username encounter errors", ex);
      return Optional.empty();
    }
  }

  public boolean checkUserNameExist(@NonNull final String userName) {
    try {
      return databaseAdapter.checkUserNameExist(userName);
    } catch (SQLException ex) {
      throw new SQLExecutionException(ex);
    }
  }

  public void createUser(@NonNull final User user) {
    try {
      databaseAdapter.createUser(user);
    } catch (SQLException ex) {
      throw new SQLExecutionException(ex);
    }
  }
}
