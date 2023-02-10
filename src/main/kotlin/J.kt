package com.gitee.ixtf

import cn.hutool.core.codec.Base58
import cn.hutool.core.collection.CollUtil
import cn.hutool.core.collection.IterUtil
import cn.hutool.core.date.DateUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.StrUtil
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

inline fun <reified T> readJson(o: ByteArray) = MAPPER.readValue<T>(o)

inline fun <reified T> readJson(o: String) = MAPPER.readValue<T>(o)

inline fun <reified T> readJson(o: File) = J.objectMap(o).readValue<T>(o)

inline fun <reified T> readJsonFile(vararg o: String) = readJson<T>(J.file(*o))

fun File.sha256Hex(): String = DigestUtil.sha256Hex(this)

fun File.sm3Hex(): String = DigestUtil.digester("sm3").digestHex(this)

fun Date.ldt(): LocalDateTime = DateUtil.toLocalDateTime(this)

fun Date.ld(): LocalDate = ldt().toLocalDate()

fun Date.lt(): LocalTime = ldt().toLocalTime()

object J {
  @JvmStatic fun mainName(o: File): String = FileUtil.mainName(o)
  @JvmStatic fun mainName(o: String): String = FileUtil.mainName(o)
  /**
   * 获取文件扩展名（后缀名），扩展名不带“.”
   *
   * @param o 文件
   * @return 扩展名
   * @see FileUtil.extName(File)
   */
  @JvmStatic fun extName(o: File): String = FileUtil.extName(o)
  /**
   * 获得文件的扩展名（后缀名），扩展名不带“.”
   *
   * @param o 文件名
   * @return 扩展名
   * @see FileUtil.extName(String)
   */
  @JvmStatic fun extName(o: String): String = FileUtil.extName(o)
  @JvmStatic
  fun objectMap(o: String): ObjectMapper =
      when (extName(o).lowercase()) {
        "yml",
        "yaml" -> YAML_MAPPER
        "toml" -> TOML_MAPPER
        else -> MAPPER
      }
  @JvmStatic fun objectMap(o: File): ObjectMapper = objectMap(o.name)
  @JvmStatic
  fun localIp(): String =
      DatagramSocket().use {
        it.connect(InetAddress.getByName("8.8.8.8"), 10002)
        it.localAddress.hostAddress
      }
  @JvmStatic
  fun <T> checkAndGetCommand(o: T): T =
      o.apply {
        val violations = VALIDATOR.validate(o)
        if (violations.isNotEmpty()) throw ConstraintViolationException(violations)
      }
  @JvmStatic
  fun <T> checkAndGetCommand(o: ByteArray, type: Class<T>): T =
      checkAndGetCommand(readJson(o, type))
  @JvmStatic
  fun <T> checkAndGetCommand(o: String, type: Class<T>): T =
      checkAndGetCommand(readJson(o, type))
  @JvmStatic
  fun <T> checkAndGetCommand(o: File, type: Class<T>): T =
      checkAndGetCommand(readJson(o, type))

  @JvmStatic fun objectNode(): ObjectNode = JsonNodeFactory.instance.objectNode()
  @JvmStatic fun arrayNode(): ArrayNode = JsonNodeFactory.instance.arrayNode()
  @JvmStatic fun readJson(o: ByteArray): JsonNode = MAPPER.readTree(o)
  @JvmStatic fun <T> readJson(o: ByteArray, type: Class<T>): T = MAPPER.readValue(o, type)
  @JvmStatic
  fun <T> readJson(o: ByteArray, ref: TypeReference<T>): T = MAPPER.readValue(o, ref)
  @JvmStatic fun readJson(o: String): JsonNode = MAPPER.readTree(o)
  @JvmStatic fun <T> readJson(o: String, type: Class<T>): T = MAPPER.readValue(o, type)
  @JvmStatic fun <T> readJson(o: String, ref: TypeReference<T>): T = MAPPER.readValue(o, ref)
  @JvmStatic fun readJson(o: File): JsonNode = objectMap(o).readTree(o)
  @JvmStatic fun <T> readJson(o: File, type: Class<T>): T = objectMap(o).readValue(o, type)
  @JvmStatic
  fun <T> readJson(o: File, ref: TypeReference<T>): T = objectMap(o).readValue(o, ref)
  @JvmStatic fun readJsonFile(o: String) = readJson(file(o))
  @JvmStatic fun <T> readJsonFile(o: String, type: Class<T>) = readJson(file(o), type)
  @JvmStatic fun <T> readJsonFile(o: String, ref: TypeReference<T>): T = readJson(file(o), ref)
  @JvmStatic
  fun writeJson(file: File, o: Any) =
      file.apply {
        FileUtil.mkParentDirs(this)
        objectMap(this).writerWithDefaultPrettyPrinter().writeValue(this, o)
      }
  @JvmStatic fun writeJson(s: String, o: Any) = writeJson(file(s), o)
  @JvmStatic fun file(vararg o: String): File = FileUtil.file(*o)
  @JvmStatic
  fun base58(id1: String, id2: String, vararg data: String): String =
      listOf(id1, id2, *data).run {
        val ids = joinToString()
        base58(ids.toByteArray(UTF_8))
      }

  @JvmStatic fun base58(o: ByteArray): String = Base58.encode(o)

  @JvmStatic fun objectId(): String = IdUtil.objectId()

  @JvmStatic fun nanoId(): String = IdUtil.nanoId()

  @JvmStatic fun password(password: String = "123456"): String = DigestUtil.bcrypt(password)

  @JvmStatic
  fun passwordCheck(password: String, hashed: String) = DigestUtil.bcryptCheck(password, hashed)

  @JvmStatic fun date(): Date = DateUtil.date()

  @JvmStatic fun date(o: Long): Date = DateUtil.date(o)

  @JvmStatic fun date(o: TemporalAccessor): Date = DateUtil.date(o)

  @JvmStatic fun ldt(o: Date) = o.ldt()

  @JvmStatic fun ld(o: Date) = o.ld()

  @JvmStatic fun lt(o: Date) = o.lt()
  @JvmStatic fun isBlank(o: CharSequence) = StrUtil.isBlank(o)
  @JvmStatic fun nonBlank(o: CharSequence) = !isBlank(o)
  @JvmStatic
  fun blankToDefault(str: CharSequence, defaultStr: String): String =
      StrUtil.blankToDefault(str, defaultStr)

  /**
   * 集合是否为空
   *
   * @param o 集合
   * @return 是否为空
   */
  @JvmStatic fun isEmpty(o: Collection<*>?) = CollUtil.isEmpty(o)
  @JvmStatic fun isEmpty(o: Iterable<*>?) = IterUtil.isEmpty(o)
  @JvmStatic fun isEmpty(o: Array<*>?) = ArrayUtil.isEmpty(o)
  @JvmStatic fun nonEmpty(o: Collection<*>?) = CollUtil.isNotEmpty(o)
  @JvmStatic fun nonEmpty(o: Iterable<*>?) = IterUtil.isNotEmpty(o)
  @JvmStatic fun nonEmpty(o: Array<*>?) = ArrayUtil.isNotEmpty(o)
  @JvmStatic fun <T> emptyToNull(o: Collection<T>?): Collection<T>? = if (nonEmpty(o)) o else null
  @JvmStatic fun <T> emptyToNull(o: List<T>?): List<T>? = if (nonEmpty(o)) o else null
  @JvmStatic fun <T> emptyToNull(o: Set<T>?): Set<T>? = if (nonEmpty(o)) o else null
  @JvmStatic fun <T> emptyToNull(o: Array<T>?): Array<T>? = if (nonEmpty(o)) o else null

  @JvmStatic fun <T> stream(o: Iterable<T>?) = stream(o, false)
  @JvmStatic
  fun <T> stream(o: Iterable<T>?, parallel: Boolean = false): Stream<T> =
      o?.run { StreamSupport.stream(spliterator(), parallel) } ?: Stream.empty()
  @JvmStatic fun sha256Hex(o: File) = o.sha256Hex()
  @JvmStatic fun sm3Hex(o: File) = o.sm3Hex()
}
