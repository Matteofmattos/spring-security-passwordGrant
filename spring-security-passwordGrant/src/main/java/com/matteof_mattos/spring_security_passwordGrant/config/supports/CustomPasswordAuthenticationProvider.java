package com.matteof_mattos.spring_security_passwordGrant.config.supports;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomPasswordAuthenticationProvider implements AuthenticationProvider {

    OAuth2AuthorizationService oAuth2AuthorizationService;

    OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator;

    UserDetailsService userDetailsService;

    PasswordEncoder passwordEncoder;

    public CustomPasswordAuthenticationProvider(OAuth2AuthorizationService oAuth2AuthorizationService, OAuth2TokenGenerator<? extends OAuth2Token> oAuth2TokenGenerator, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {

        Assert.notNull(oAuth2AuthorizationService, "authorizationService cannot be null");
        Assert.notNull(oAuth2TokenGenerator, "TokenGenerator cannot be null");
        Assert.notNull(userDetailsService, "UserDetailsService cannot be null");
        Assert.notNull(passwordEncoder, "PasswordEncoder cannot be null");
        this.oAuth2AuthorizationService = oAuth2AuthorizationService;
        this.passwordEncoder = passwordEncoder;
        this.oAuth2TokenGenerator = oAuth2TokenGenerator;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        CustomPasswordAuthenticationToken customPasswordAuthenticationToken = (CustomPasswordAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal = getOAuth2ClientAuthenticationToken(authentication);

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();

        String username = customPasswordAuthenticationToken.getUsername();
        String password = customPasswordAuthenticationToken.getPassword();
        UserDetails userDetails;


        try { userDetails = userDetailsService.loadUserByUsername(username);}
        catch (UsernameNotFoundException exc){ throw new OAuth2AuthenticationException("# Invalid credentials! ");}


        if (!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new OAuth2AuthenticationException("# Invalid password! ");
        }


        Set<String> authorizedScopes = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(scope -> registeredClient.getScopes().contains(scope)).collect(Collectors.toSet());



        //-----------Create a new Security Context ----------


        return null;
    }


    private OAuth2ClientAuthenticationToken getOAuth2ClientAuthenticationToken(Authentication authentication) {

        OAuth2ClientAuthenticationToken principal = null;

        //		O método isAssignableFrom() retorna verdadeiro se o objeto do tipo especificado pode ser atribuído ao objeto deste tipo.
        //		 Ele verifica se há compatibilidade entre os tipos.
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {

            principal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }

        if (principal != null && principal.isAuthenticated()) {
            return principal;
        }

        else throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_CLIENT);

    }


    @Override
    public boolean supports(Class<?> authentication) {
        return CustomPasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
