package com.matteof_mattos.spring_security_passwordGrant.config.supports;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import java.util.*;

public class CustomPasswordAuthenticationConverter implements AuthenticationConverter {

    private static Logger log = LoggerFactory.getLogger(CustomPasswordAuthenticationConverter.class);


    public CustomPasswordAuthenticationConverter() {
        log.error("# Instanciado o método convert em CustomPasswordAuthenticationConverter... ");
    }

    @Override
    public Authentication convert(HttpServletRequest request) {

        String grantType = request.getParameter("grant_type");

        if (!"password".equals(grantType)) return null;


        MultiValueMap<String, String> parameters = getParameters(request);

        // ------ Verificando se há o parâmetro "scope" no formulário de requisição da request.(Opcional)
        String scopeValueList = parameters.getFirst(OAuth2ParameterNames.SCOPE);

        if (StringUtils.hasText(scopeValueList) && parameters.get(OAuth2ParameterNames.SCOPE).size()!=1){
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        Set<String> requiredScopes = null;

        if (StringUtils.hasText(scopeValueList)){
            requiredScopes = new HashSet<>(
                    Arrays.asList(StringUtils.delimitedListToStringArray(scopeValueList, " ")));
        }

        // Para username
        String username = parameters.getFirst(OAuth2ParameterNames.USERNAME);
        if (!StringUtils.hasText(username) ||
                parameters.get(OAuth2ParameterNames.USERNAME).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }

        // Para password
        String password = parameters.getFirst(OAuth2ParameterNames.PASSWORD);
        if (!StringUtils.hasText(password) ||
                parameters.get(OAuth2ParameterNames.PASSWORD).size() != 1) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_REQUEST);
        }


        Map<String,String> additionalParameters = new HashMap<>();

        parameters.forEach((key,value)-> {
            if ((!key.equals(OAuth2ParameterNames.SCOPE) && (!key.equals(OAuth2ParameterNames.GRANT_TYPE)))) {

                additionalParameters.put(key,value.getFirst());
            }
        });

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return new CustomPasswordAuthenticationToken(clientPrincipal, requestedScopes, additionalParameters);
    }


    public MultiValueMap<String,String> getParameters(HttpServletRequest request){

        Map<String, String[]> parameters = request.getParameterMap();

        MultiValueMap<String,String> parametersMap = new LinkedMultiValueMap<>(parameters.size());

        parameters.forEach((key,values)-> {
            
            if ( values.length >0 ){
                for (String value: values){
                    parametersMap.add(key,value);
                }
            }
        });

        return parametersMap;
    }

}
