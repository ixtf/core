package com.gitee.ixtf

import cn.hutool.core.codec.Base58
import cn.hutool.core.date.DateUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.crypto.digest.DigestUtil
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator

fun objectMap(o: String) =
    when (J.extName(o).lowercase()) {
      "yml",
      "yaml" -> YAML_MAPPER
      "toml" -> TOML_MAPPER
      else -> MAPPER
    }

fun File.objectMap() = objectMap(name)
fun File.mainName(): String = FileUtil.mainName(this)
fun File.extName(): String = FileUtil.extName(this)
inline fun <reified T> File.readJson() = objectMap().readValue<T>(this)
inline fun <reified T> File.inputCommand() = J.inputCommand(readJson<T>())
fun File.writeJson(o: Any) {
  FileUtil.mkParentDirs(this)
  objectMap().writerWithDefaultPrettyPrinter().writeValue(this, o)
}
fun File.sha256Hex(): String = DigestUtil.sha256Hex(this)
fun File.sha256Base58(): String = DigestUtil.sha256(this).base58()
fun File.sm3Hex(): String = DigestUtil.digester("sm3").digestHex(this)
fun File.sm3Base58(): String = DigestUtil.digester("sm3").digest(this).base58()

fun String.mainName(): String = FileUtil.mainName(this)
fun String.extName(): String = FileUtil.extName(this)
inline fun <reified T> String.readJson() = MAPPER.readValue<T>(this)
inline fun <reified T> String.inputCommand() = J.inputCommand(readJson<T>())
inline fun <reified T> String.readJsonFile() = J.file(this).readJson<T>()

inline fun <reified T> ByteArray.readJson() = MAPPER.readValue<T>(this)
inline fun <reified T> ByteArray.inputCommand() = J.inputCommand(readJson<T>())
fun ByteArray.base58(): String = Base58.encode(this)

fun Date.ldt(): LocalDateTime = DateUtil.toLocalDateTime(this)
fun Date.ld(): LocalDate = ldt().toLocalDate()
fun Date.lt(): LocalTime = ldt().toLocalTime()

@JvmField
val MAPPER = jsonMapper {
  ServiceLoader.load(com.fasterxml.jackson.databind.Module::class.java).forEach { addModule(it) }
  disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
  serializationInclusion(JsonInclude.Include.NON_NULL)
}

val YAML_MAPPER by lazy {
  YAMLMapper().apply {
    ServiceLoader.load(com.fasterxml.jackson.databind.Module::class.java).forEach {
      registerModule(it)
    }
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
  }
}

val TOML_MAPPER by lazy {
  TomlMapper().apply {
    ServiceLoader.load(com.fasterxml.jackson.databind.Module::class.java).forEach {
      registerModule(it)
    }
    disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    setSerializationInclusion(JsonInclude.Include.NON_NULL)
  }
}

val VALIDATOR: Validator by lazy {
  Validation.byDefaultProvider()
      .configure()
      .messageInterpolator(ParameterMessageInterpolator())
      .buildValidatorFactory()
      .validator
}

data class EntityDTO(@field:NotBlank val id: String)

data class EntitiesDTO(@field:NotNull @field:Size(min = 1) val ids: Set<@NotBlank String>)
