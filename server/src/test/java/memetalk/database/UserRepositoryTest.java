package memetalk.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import memetalk.model.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserRepositoryTest {
  private UserRepository userRepository;
  private PasswordEncoder passwordEncoder;

  @Before
  public void setUp() {
    passwordEncoder = new BCryptPasswordEncoder(10);
    userRepository = new UserRepository(passwordEncoder);
    userRepository.init();
  }

  @Test
  public void testStoreUserAndFindUser() {
    User expectedUser =
        User.builder()
            .id("id")
            .userName("userName")
            .password("password")
            .name("name")
            .roles(ImmutableSet.of("USER"))
            .build();

    userRepository.storeUser(expectedUser);

    final String actualId = userRepository.findUserById(expectedUser.getId()).get().getId();
    assertEquals(expectedUser.getId(), actualId);
  }

  @Test
  public void testExistsById() {
    User expectedUser =
        User.builder()
            .id("id")
            .userName("userName")
            .password("password")
            .name("name")
            .roles(ImmutableSet.of("USER"))
            .build();

    userRepository.storeUser(expectedUser);

    assertTrue(userRepository.existsById(expectedUser.getId()));
  }
}
