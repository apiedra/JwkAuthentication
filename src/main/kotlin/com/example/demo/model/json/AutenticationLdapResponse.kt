package com.example.demo.model.json

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class AutenticationLdapResponse {

    var rersponseCode:String=""
}