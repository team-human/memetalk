package memetalk.security;

import lombok.Builder;
import lombok.Getter;
import memetalk.model.JwtUserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

@Getter
public class JwtPreAuthenticationToken extends PreAuthenticatedAuthenticationToken {
  private static final long serialVersionUID = 8416353006421761801L;

  @Builder
  public JwtPreAuthenticationToken(JwtUserDetails principal, WebAuthenticationDetails details) {
    super(principal, null, principal.getAuthorities());
    super.setDetails(details);
  }

  @Override
  public Object getCredentials() {
    return null;
  }
}
