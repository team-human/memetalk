package memetalk.security;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import memetalk.service.UserService;
import org.junit.Before;
import org.junit.Test;

public class JwtFilterTest {
  private static final String AUTHORIZATION_HEADER = "Authorization";

  private UserService userService;
  private JwtFilter jwtFilter;

  @Before
  public void setUp() {
    userService = mock(UserService.class);
    jwtFilter = new JwtFilter(userService);
  }

  @Test
  public void testGetTokenFromRequest() {
    HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
    final String expectedAuthToken = "1234Totken";
    when(httpServletRequest.getHeader(AUTHORIZATION_HEADER))
        .thenReturn("Bearer" + " " + expectedAuthToken);
    Optional<String> actualAuthToken = jwtFilter.getTokenFromRequest(httpServletRequest);

    assertEquals(expectedAuthToken, actualAuthToken.get());
  }
}
