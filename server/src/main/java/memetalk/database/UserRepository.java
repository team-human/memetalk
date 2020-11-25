package memetalk.database;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import memetalk.data.FakeDataGenerator;
import memetalk.model.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepository {
  // TODO: connect to the real database
  private static Queue<User> concurrentQueue =
      new ConcurrentLinkedQueue<>(FakeDataGenerator.generateFakeUsers());

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
