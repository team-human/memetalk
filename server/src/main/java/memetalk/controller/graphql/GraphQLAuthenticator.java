package memetalk.controller.graphql;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.model.CreateUserInput;
import memetalk.model.LoginUser;
import memetalk.model.User;
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
public class GraphQLAuthenticator {
  @NonNull private final UserService userService;
  @NonNull private final AuthenticationProvider authenticationProvider;

  @PreAuthorize("@userService.isAnonymous()")
  public LoginUser loginUserAuth(@NonNull final String username, @NonNull final String password) {
    final UsernamePasswordAuthenticationToken credentials =
        new UsernamePasswordAuthenticationToken(username, password);

    try {
      SecurityContextHolder.getContext()
          .setAuthentication(authenticationProvider.authenticate(credentials));
      return userService.getCurrentUser();
    } catch (AuthenticationException ex) {
      throw new BadCredentialsException(username);
    }
  }

  @PreAuthorize("@userService.isAnonymous()")
  public LoginUser createUserAuth(@NonNull final CreateUserInput userInput) {
    return userService.createUser(userInput);
  }

  @PreAuthorize("@userService.isAuthenticated()")
  public User getCurrentUser() {
    LoginUser loginUser = userService.getCurrentUser();
    if (loginUser != null) {
      return loginUser.getUser();
    }
    return null;
  }
}
