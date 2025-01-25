package com.matteof_mattos.spring_security_passwordGrant.config;

import com.matteof_mattos.spring_security_passwordGrant.config.supports.CustomPasswordAuthenticationConverter;
import com.matteof_mattos.spring_security_passwordGrant.config.supports.CustomPasswordAuthenticationProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class AuthorizationServer {

    @Value("${security.client-id}")
    private String clientId;

    @Value("${security.client-password}")
    private String clientSecret;

    @Value("${security.jwt.duration}")
    private String jwtDurationSeconds;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        //---- Registra todos os componentes de infraestrutura necessários para o funcionamento do servidor de autorização.
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);

        httpSecurity.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                // --- Recebe a request de acesso e converte em um "CustomPasswordAuthenticationToken"
                .tokenEndpoint(oAuth2TokenEndpointConfigurer -> oAuth2TokenEndpointConfigurer.accessTokenRequestConverter(new CustomPasswordAuthenticationConverter())
                .authenticationProvider(new CustomPasswordAuthenticationProvider(authorizationService(), tokenGenerator(), userDetailsService, passwordEncoder())));

        httpSecurity.oauth2ResourceServer(oAuth2ResourceServerConfigurer ->
                oAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()));

        return httpSecurity.build();
    }

    @Bean
    public OAuth2AuthorizationService authorizationService() {

//        1- Recebe e valida pedidos de autorização de aplicativos cliente,
//        processa e gerencia as decisões de autorização.

//       2 - Armazena informações sobre autorizações concedidas ou negadas.
//                Recupera essas informações para futuras referências.

        return new InMemoryOAuth2AuthorizationService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }



    // -----------// jwt token generation //------------

    @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
        NimbusJwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource());
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        jwtGenerator.setJwtCustomizer(tokenCustomizer());
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, accessTokenGenerator);
    }


    private OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return null;
    }


    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = generateRsa();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
    }

    private static RSAKey generateRsa() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(UUID.randomUUID().toString()).build();
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }
}
