package memetalk.service;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import memetalk.database.UserRepository;
import memetalk.model.JwtUserDetails;
import memetalk.model.User;
import memetalk.security.BadTokenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository repository;
  private final JWTVerifier jwtVerifier;

  @Transactional
  public JwtUserDetails loadUserByToken(String token) {
    return getDecodedToken(token)
        .map(DecodedJWT::getSubject)
        .flatMap(repository::findUserByEmail)
        .map(user -> getUserDetails(user, token))
        .orElseThrow(BadTokenException::new);
  }

  @Transactional
  public User getCurrentUser() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .map(Authentication::getName)
        .flatMap(repository::findUserByEmail)
        .orElse(null);
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
