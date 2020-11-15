package memetalk.database;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import memetalk.data.FakeDataGenerator;
import memetalk.model.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepository {
  // TODO: connect to the real database
  public Optional<User> findUserByEmail(@NonNull final String email) {
    return FakeDataGenerator.generateFakeUsers().stream()
        .filter(user -> user.getEmail().equals(email))
        .findFirst();
  }
}
