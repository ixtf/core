package com.gitee.ixtf

import cn.hutool.core.codec.Base58
import cn.hutool.core.collection.CollUtil
import cn.hutool.core.collection.IterUtil
import cn.hutool.core.date.DateUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.IdUtil
import cn.hutool.crypto.digest.DigestUtil
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.validation.ConstraintViolationException
import java.io.File
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.StandardCharsets.UTF_8
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport

inline fun <reified T> readJson(bytes: ByteArray) = MAPPER.readValue<T>(bytes)

inline fun <reified T> readJson(json: String) = MAPPER.readValue<T>(json)

inline fun <reified T> readJson(file: File) = MAPPER.readValue<T>(file)

inline fun <reified T> readJsonFile(vararg path: String) = readJson<T>(J.file(*path))

fun File.sha256Hex(): String = DigestUtil.sha256Hex(this)

fun File.sm3Hex(): String = DigestUtil.digester("sm3").digestHex(this)

fun Date.ldt(): LocalDateTime = DateUtil.toLocalDateTime(this)

fun Date.ld(): LocalDate = ldt().toLocalDate()

fun Date.lt(): LocalTime = ldt().toLocalTime()

object J {
  @JvmStatic fun mainName(file: File): String = FileUtil.mainName(file)
  @JvmStatic fun mainName(fileName: String): String = FileUtil.mainName(fileName)
  @JvmStatic fun extName(file: File): String = FileUtil.extName(file)
  @JvmStatic fun extName(fileName: String): String = FileUtil.extName(fileName)
  @JvmStatic
  fun objectMap(fileName: String): ObjectMapper =
      when (extName(fileName).lowercase()) {
        "yml",
        "yaml" -> YAML_MAPPER
        "toml" -> TOML_MAPPER
        else -> MAPPER
      }
  @JvmStatic fun objectMap(file: File): ObjectMapper = objectMap(file.name)
  @JvmStatic
  fun localIp(): String =
      DatagramSocket().use {
        it.connect(InetAddress.getByName("8.8.8.8"), 10002)
        it.localAddress.hostAddress
      }
  @JvmStatic
  fun <T> checkAndGetCommand(command: T): T =
      command.apply {
        val violations = VALIDATOR.validate(command)
        if (violations.isNotEmpty()) throw ConstraintViolationException(violations)
      }
  @JvmStatic
  fun <T> checkAndGetCommand(bytes: ByteArray, type: Class<T>): T =
      checkAndGetCommand(readJson(bytes, type))
  @JvmStatic
  fun <T> checkAndGetCommand(json: String, type: Class<T>): T =
      checkAndGetCommand(readJson(json, type))
  @JvmStatic
  fun <T> checkAndGetCommand(file: File, type: Class<T>): T =
      checkAndGetCommand(readJson(file, type))

  @JvmStatic fun objectNode(): ObjectNode = JsonNodeFactory.instance.objectNode()
  @JvmStatic fun arrayNode(): ArrayNode = JsonNodeFactory.instance.arrayNode()
  @JvmStatic fun readJson(bytes: ByteArray): JsonNode = MAPPER.readTree(bytes)
  @JvmStatic fun <T> readJson(bytes: ByteArray, type: Class<T>): T = MAPPER.readValue(bytes, type)
  @JvmStatic
  fun <T> readJson(bytes: ByteArray, ref: TypeReference<T>): T = MAPPER.readValue(bytes, ref)
  @JvmStatic fun readJson(json: String): JsonNode = MAPPER.readTree(json)
  @JvmStatic fun <T> readJson(json: String, type: Class<T>): T = MAPPER.readValue(json, type)
  @JvmStatic fun <T> readJson(json: String, ref: TypeReference<T>): T = MAPPER.readValue(json, ref)
  @JvmStatic fun readJson(file: File): JsonNode = objectMap(file).readTree(file)
  @JvmStatic fun <T> readJson(file: File, type: Class<T>): T = objectMap(file).readValue(file, type)
  @JvmStatic
  fun <T> readJson(file: File, ref: TypeReference<T>): T = objectMap(file).readValue(file, ref)
  @JvmStatic fun readJsonFile(s: String) = readJson(file(s))
  @JvmStatic fun <T> readJsonFile(s: String, type: Class<T>) = readJson(file(s), type)
  @JvmStatic fun <T> readJsonFile(s: String, ref: TypeReference<T>): T = readJson(file(s), ref)
  @JvmStatic
  fun writeJson(file: File, o: Any) {
    FileUtil.mkParentDirs(file)
    objectMap(file).writerWithDefaultPrettyPrinter().writeValue(file, o)
  }
  @JvmStatic fun writeJson(s: String, o: Any) = writeJson(file(s), o)
  @JvmStatic fun file(vararg path: String): File = FileUtil.file(*path)
  @JvmStatic
  fun base58(id1: String, id2: String, vararg data: String): String =
      listOf(id1, id2, *data).run {
        val ids = joinToString()
        base58(ids.toByteArray(UTF_8))
      }

  @JvmStatic fun base58(data: ByteArray): String = Base58.encode(data)

  @JvmStatic fun objectId(): String = IdUtil.objectId()

  @JvmStatic fun nanoId(): String = IdUtil.nanoId()

  @JvmStatic fun password(password: String = "123456"): String = DigestUtil.bcrypt(password)

  @JvmStatic
  fun passwordCheck(password: String, hashed: String) = DigestUtil.bcryptCheck(password, hashed)

  @JvmStatic fun date(): Date = DateUtil.date()

  @JvmStatic fun date(date: Long): Date = DateUtil.date(date)

  @JvmStatic fun date(date: TemporalAccessor): Date = DateUtil.date(date)

  @JvmStatic fun ldt(date: Date) = date.ldt()

  @JvmStatic fun ld(date: Date) = date.ld()

  @JvmStatic fun lt(date: Date) = date.lt()

  /**
   * 集合是否为空
   *
   * @param collection 集合
   * @return 是否为空
   */
  @JvmStatic fun isEmpty(collection: Collection<*>?) = CollUtil.isEmpty(collection)

  @JvmStatic fun nonEmpty(collection: Collection<*>?) = CollUtil.isNotEmpty(collection)

  @JvmStatic fun <T> emptyIfNull(collection: List<T>?): List<T> = CollUtil.emptyIfNull(collection)

  @JvmStatic fun <T> emptyIfNull(collection: Set<T>?): Set<T> = CollUtil.emptyIfNull(collection)

  @JvmStatic
  fun <T> stream(iterable: Iterable<T>, parallel: Boolean = false): Stream<T> =
      if (IterUtil.isEmpty(iterable)) Stream.empty()
      else StreamSupport.stream(iterable.spliterator(), parallel)
  @JvmStatic fun sha256Hex(file: File) = file.sha256Hex()
  @JvmStatic fun sm3Hex(file: File) = file.sm3Hex()
}
