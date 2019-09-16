package com.example.demo

import com.example.demo.model.json.AutenticationLdapRequest
import com.example.demo.model.json.AutenticationLdapResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate


@Component
class ApiConnectionProvider
    : AuthenticationProvider {

    @Value("\${ldap.uri}")
    var uri:String=""


    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication? {
        val quote: AutenticationLdapResponse
        try {
            var request: AutenticationLdapRequest = AutenticationLdapRequest()
            request.user = authentication.name
            request.password = authentication.credentials.toString()
            val restTemplate = RestTemplate()
            quote = restTemplate.postForObject(uri, request,
                    AutenticationLdapResponse::class.java)!!

        } catch (e: Exception) {
            throw Exception(e.message)
        }
       if(quote!=null && quote.rersponseCode=="0") {
           return (UsernamePasswordAuthenticationToken(
                   "", "", ArrayList<GrantedAuthority>()))
       }
        else {
           throw Exception("Error en autenticación")
       }

    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }


}