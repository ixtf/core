package com.gitee.ixtf

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import jakarta.validation.Validation
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.util.*
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator

@JvmField
val VALIDATOR =
    Validation.byDefaultProvider()
        .configure()
        .messageInterpolator(ParameterMessageInterpolator())
        .buildValidatorFactory()
        .validator

@JvmField
val MAPPER = jsonMapper {
  ServiceLoader.load(com.fasterxml.jackson.databind.Module::class.java).forEach { addModule(it) }
  disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  serializationInclusion(JsonInclude.Include.NON_NULL)
}

@JvmField
val YAML_MAPPER =
    YAMLMapper().apply {
      ServiceLoader.load(com.fasterxml.jackson.databind.Module::class.java).forEach {
        registerModule(it)
      }
      disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

@JvmField
val TOML_MAPPER =
    TomlMapper().apply {
      ServiceLoader.load(com.fasterxml.jackson.databind.Module::class.java).forEach {
        registerModule(it)
      }
      disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

data class EntityDTO(@field:NotBlank val id: String)

data class EntitiesDTO(@field:NotNull @field:Size(min = 1) val ids: Set<@NotBlank String>)
