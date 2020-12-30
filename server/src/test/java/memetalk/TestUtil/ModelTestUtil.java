package memetalk.TestUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import lombok.NonNull;
import memetalk.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ModelTestUtil {

  public static void checkUserIsEqual(
      @NonNull User expectedUser,
      @NonNull User actualUser,
      @NonNull PasswordEncoder passwordEncoder) {
    assertEquals(expectedUser.getName(), actualUser.getName());
    assertTrue(passwordEncoder.matches(expectedUser.getPassword(), actualUser.getPassword()));
    assertEquals(expectedUser.getRoles(), actualUser.getRoles());
    assertEquals(expectedUser.getRoles(), actualUser.getRoles());
  }
}
