package com.example.demo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory
import java.security.KeyPair
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.KeyUse
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.beans.factory.annotation.Value
import java.security.interfaces.RSAPublicKey


@Configuration
@EnableAuthorizationServer
class JwkAuthServerConfig(
        @Autowired
        @Qualifier("authenticationManagerBean")
        private val authenticationManager: AuthenticationManager
): AuthorizationServerConfigurerAdapter() {

    @Value("\${jwt.CLIENT_ID}")
    private val clientId: String? = ""

    @Value("\${jwt.SECRET_KEY}")
    private val secretKey: String? = ""

    @Value("\${jwt.KEY_STORE_FILE}")
    private val keyStoreFile: String = ""

    @Value("\${jwt.KEY_STORE_PASSWORD}")
    private val keyStorePassword: String = ""

    @Value("\${jwt.KEY_ALIAS}")
    private val keyAlias: String = ""

    @Value("\${jwt.JWK_KID}")
    private val jwdKid: String = ""



    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients.inMemory()
                .withClient(clientId)
                .secret("{noop}$secretKey")
                .scopes("FOO")
                .autoApprove(true)
                .authorities("FOO_READ", "FOO_WRITE")
                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtTokenEnhancer()).authenticationManager(authenticationManager);
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(jwtTokenEnhancer())
    }

    @Bean
    protected fun jwtTokenEnhancer(): JwtAccessTokenConverter {
        val keyStoreKeyFactory = KeyStoreKeyFactory(ClassPathResource(keyStoreFile), keyStorePassword.toCharArray())
        val converter = JwtAccessTokenConverter()
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair(keyAlias))
        return converter
    }

    @Bean
    fun keyPair(): KeyPair {
        val ksFile = ClassPathResource(keyStoreFile)
        val ksFactory = KeyStoreKeyFactory(ksFile, keyStorePassword.toCharArray())
        return ksFactory.getKeyPair(keyAlias)
    }
    @Bean
    fun jwkSet(): JWKSet {
        val builder = RSAKey.Builder(keyPair().public as RSAPublicKey).keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID(jwdKid)
        return JWKSet(builder.build())
    }
}