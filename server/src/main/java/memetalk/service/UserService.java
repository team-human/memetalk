package memetalk.service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.database.UserRepository;
import memetalk.exception.BadTokenException;
import memetalk.exception.UserExistsException;
import memetalk.model.CreateUserInput;
import memetalk.model.JwtUserDetails;
import memetalk.model.User;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private static final String USER_AUTHORITY = "USER";
  private static final String ADMIN_AUTHORITY = "ADMIN";
  private final UserRepository repository;
  private final JWTVerifier jwtVerifier;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public JwtUserDetails loadUserByToken(String token) {
    return getDecodedToken(token)
        .map(DecodedJWT::getSubject)
        .flatMap(repository::findUserById)
        .map(user -> getUserDetails(user, token))
        .orElseThrow(BadTokenException::new);
  }

  @Transactional
  public User getCurrentUser() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName)
        .flatMap(repository::findUserById)
        .orElse(null);
  }

  public boolean isAdmin() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getAuthorities)
        .stream()
        .flatMap(Collection::stream)
        .map(GrantedAuthority::getAuthority)
        .anyMatch(ADMIN_AUTHORITY::equals);
  }

  public boolean isAuthenticated() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .filter(auth -> !isAnonymous(auth))
        .isPresent();
  }

  private boolean isAnonymous(Authentication authentication) {
    return authentication instanceof AnonymousAuthenticationToken;
  }

  @Transactional
  public User createUser(CreateUserInput input) {
    if (!exists(input)) {
      return repository.storeUser(
          User.builder()
              .password(passwordEncoder.encode(input.getPassword()))
              .roles(ImmutableSet.of(USER_AUTHORITY))
              .id(input.getId())
              .build());
    } else {
      throw new UserExistsException("Creating a new User encounters error. Id is " + input.getId());
    }
  }

  private boolean exists(CreateUserInput input) {
    return repository.existsById(input.getId());
  }

  private JwtUserDetails getUserDetails(User user, String token) {
    return JwtUserDetails.builder()
        .username(user.getId())
        .password(user.getPassword())
        .authorities(getSimpleGrantedAuthorities(user.getRoles()))
        .token(token)
        .build();
  }

  private Optional<DecodedJWT> getDecodedToken(String token) {
    try {
      return Optional.of(jwtVerifier.verify(token));
    } catch (JWTVerificationException ex) {
      log.warn("Jwt verification failed", ex);
      return Optional.empty();
    }
  }

  private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Set<String> roles) {
    if (CollectionUtils.isEmpty(roles)) {
      return ImmutableList.of();
    } else {
      return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }
  }
}
