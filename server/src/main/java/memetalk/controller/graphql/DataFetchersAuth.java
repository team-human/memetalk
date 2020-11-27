package memetalk.controller.graphql;

import java.sql.SQLException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.database.DatabaseAdapter;
import memetalk.model.CreateUserInput;
import memetalk.model.LoginUser;
import memetalk.model.Meme;
import memetalk.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/*
 * Any dataFetcher that needs @PreAuthorize should complement them here.
 * @PreAuthorize doesn't work in DataFetchers.class.
 * Note: the annotated methods need to be public
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DataFetchersAuth {
  @NonNull private final UserService userService;
  @NonNull private final AuthenticationProvider authenticationProvider;
  @NonNull private final DatabaseAdapter databaseAdapter;

  @PreAuthorize("@userService.isAnonymous()")
  public LoginUser loginUserAuth(@NonNull final String id, @NonNull final String password) {
    final UsernamePasswordAuthenticationToken credentials =
        new UsernamePasswordAuthenticationToken(id, password);

    try {
      SecurityContextHolder.getContext()
          .setAuthentication(authenticationProvider.authenticate(credentials));
      return userService.getCurrentUser();
    } catch (AuthenticationException ex) {
      throw new BadCredentialsException(id);
    }
  }

  @PreAuthorize("@userService.isAnonymous()")
  public LoginUser createUserAuth(@NonNull final CreateUserInput userInput) {
    return userService.createUser(userInput);
  }

  @PreAuthorize("@userService.isAuthenticated()")
  public void createMemeAuth(@NonNull final Meme meme) throws SQLException {
    databaseAdapter.addMeme(meme);
  }
}
