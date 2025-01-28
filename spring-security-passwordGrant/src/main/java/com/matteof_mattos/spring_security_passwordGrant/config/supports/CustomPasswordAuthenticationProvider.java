package com.matteof_mattos.spring_security_passwordGrant.config.supports;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.ERROR_URI;

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
        CustomUserAuthorities customUserAuthorities = new CustomUserAuthorities(username, userDetails.getAuthorities());
        clientPrincipal.setDetails(customUserAuthorities);

        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(clientPrincipal);
        SecurityContextHolder.setContext(newContext);

        // ------------------- Token Builder ----------------------
//        OAuth2TokenContext na biblioteca Spring Security OAuth2. Ela fornece um contexto para gerenciar informações relacionadas ao token OAuth2 durante o processo de autorização.
//        Principais características:
//        Extende a interface OAuth2TokenContext, que define métodos para acessar informações como autorização, tipo de concessão de autorização, contexto do servidor de autorização, escopos autorizados, principal registrado e tipo de token.

        // OAuth2 token context
        DefaultOAuth2TokenContext.Builder token_Context_Builder = DefaultOAuth2TokenContext.builder()
                .principal(clientPrincipal)
                .authorizedScopes(authorizedScopes)
                .registeredClient(registeredClient)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .authorizationGrant(customPasswordAuthenticationToken);

//        OAuth2Authorization representa uma solicitação de autorização OAuth2 no Spring Security
//        Contém informações sobre a solicitação de autorização, incluindo:
//        Autoridade de autorização (Authorization Server)
//        Tipo de concessão de autorização
//        Escopos solicitados
//        Dados do cliente que fez a solicitação

        OAuth2Authorization.Builder oauth2AuthorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .principalName(clientPrincipal.getName())
                .attribute(Principal.class.getName(), clientPrincipal);


        //-----------ACCESS TOKEN----------

        DefaultOAuth2TokenContext tokenContext = token_Context_Builder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token token = this.oAuth2TokenGenerator.generate(tokenContext);

         if ( token == null) {
             OAuth2Error oAuth2Error = new OAuth2Error(OAuth2ErrorCodes.SERVER_ERROR, "The token generator failed to generate the access token.", ERROR_URI);
             throw new OAuth2AuthenticationException(oAuth2Error);
         }

        OAuth2AccessToken oAuth2AccessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
                token.getTokenValue(),
                token.getIssuedAt(),
                token.getExpiresAt(),
                tokenContext.getAuthorizedScopes());

        if (token instanceof ClaimAccessor) {
            oauth2AuthorizationBuilder.token(oAuth2AccessToken, (metadata) ->
                    metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) token).getClaims()));
        } else  oauth2AuthorizationBuilder.accessToken(oAuth2AccessToken);


        OAuth2Authorization oauth2_Authorization = oauth2AuthorizationBuilder.build();
        this.oAuth2AuthorizationService.save(oauth2_Authorization);

        return new OAuth2AccessTokenAuthenticationToken(registeredClient,clientPrincipal,oAuth2AccessToken);
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
