package com.example.demo.model.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AutenticationLdapRequest {

    var user:String=""
    var password:String=""
}