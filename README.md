# JwkAuthentication
Servicio de generación y verificación de tokens validando contra Ldap API

#Crear jks

#API URL
```
http://localhost:8081/sso-auth-server/oauth/token
```

#API AUTHORIZATION
x-www-form-urlencoded
TYPE BASIC
```
username: {CLIENT_ID}
password: {SECRET_KEY}
```



#API BODY
x-www-form-urlencoded
```
grant_type: password
username: {email}
password: {contraseña}
```
