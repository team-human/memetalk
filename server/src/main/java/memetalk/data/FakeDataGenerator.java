package memetalk.data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import memetalk.model.User;

/**
 * FakeDataGenerator generates fake data to service. We use FakeDataGenerator to provide data for
 * the client test against while we are building the real data servered by a database.
 */
public class FakeDataGenerator {
  public static List<User> generateFakeUsers() {
    return Arrays.asList(
        User.builder()
            .id("userId1")
            .username("username1")
            .name("John")
            .password("123")
            .roles(new HashSet<>(Arrays.asList("ADMIN")))
            .build(),
        User.builder()
            .id("userId2")
            .username("username2")
            .name("Alice")
            .password("123")
            .roles(new HashSet<>(Arrays.asList("USER")))
            .build(),
        User.builder()
            .id("userId3")
            .username("username3")
            .name("Bob")
            .password("123")
            .roles(new HashSet<>(Arrays.asList("USER")))
            .build());
  }

  public static List<String> generateFakeTags() {
    return Arrays.asList("#comic", "#movie", "#election");
  }
}
