package memetalk.database;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.annotation.PostConstruct;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import memetalk.data.FakeDataGenerator;
import memetalk.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  // TODO: connect to the real database
  private static Queue<User> concurrentQueue =
      new ConcurrentLinkedQueue<>(FakeDataGenerator.generateFakeUsers());

  @NonNull private final PasswordEncoder passwordEncoder;

  // This is a little bit hacky. After we remove FakeDataGenerator, we can remove this
  @PostConstruct
  public void init() {
    concurrentQueue.forEach(
        user -> {
          user.setPassword(passwordEncoder.encode(user.getPassword()));
        });
  }

  public Optional<User> findUserById(@NonNull final String userId) {
    return concurrentQueue.stream().filter(user -> user.getId().equals(userId)).findFirst();
  }

  public boolean existsById(@NonNull final String userId) {
    return concurrentQueue.stream().anyMatch(user -> user.getId().equals(userId));
  }

  public User storeUser(@NonNull final User user) {
    concurrentQueue.add(user);
    return user;
  }
}
