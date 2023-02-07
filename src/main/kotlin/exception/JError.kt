package com.gitee.ixtf.exception

import com.fasterxml.jackson.databind.JsonNode
import com.gitee.ixtf.MAPPER

class JError : Error {
  private val errorCode: String
  private val params: Map<String, String>?

  constructor(errorCode: String) {
    this.errorCode = errorCode
    params = null
  }

  constructor(errorCode: String, message: String?) : super(message) {
    this.errorCode = errorCode
    params = null
  }

  constructor(errorCode: String, params: Map<String, String>?) {
    this.errorCode = errorCode
    this.params = params
  }

  fun toJsonNode(): JsonNode {
    return MAPPER.createObjectNode().put("errorCode", errorCode)
  }
}
