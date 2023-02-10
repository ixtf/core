package com.gitee.ixtf

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
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.fasterxml.jackson.databind.node.ObjectNode
import jakarta.validation.ConstraintViolationException
import java.io.File
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.StandardCharsets.UTF_8
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport

object J {
  @JvmStatic fun file(vararg o: String): File = FileUtil.file(*o)
  @JvmStatic
  fun <T> inputCommand(o: T): T =
      o.apply {
        val violations = VALIDATOR.validate(o)
        if (violations.isNotEmpty()) throw ConstraintViolationException(violations)
      }
  @JvmStatic fun objectId(): String = IdUtil.objectId()
  @JvmStatic fun nanoId(): String = IdUtil.nanoId()
  @JvmStatic fun password(password: String = "123456"): String = DigestUtil.bcrypt(password)
  @JvmStatic
  fun passwordCheck(password: String, hashed: String) = DigestUtil.bcryptCheck(password, hashed)
  @JvmStatic fun objectNode(): ObjectNode = JsonNodeFactory.instance.objectNode()
  @JvmStatic fun arrayNode(): ArrayNode = JsonNodeFactory.instance.arrayNode()
  @JvmStatic
  fun localIp(): String =
      DatagramSocket().use {
        it.connect(InetAddress.getByName("8.8.8.8"), 10002)
        it.localAddress.hostAddress
      }

  /** dsfaf */
  @JvmStatic fun mainName(o: File): String = o.mainName()
  @JvmStatic fun extName(o: File): String = o.extName()
  @JvmStatic fun mainName(o: String): String = o.mainName()
  @JvmStatic fun extName(o: String): String = o.extName()

  @JvmStatic fun <T> inputCommand(o: File, type: Class<T>): T = inputCommand(readJson(o, type))
  @JvmStatic fun <T> inputCommand(o: String, type: Class<T>): T = inputCommand(readJson(o, type))
  @JvmStatic fun <T> inputCommand(o: ByteArray, type: Class<T>): T = inputCommand(readJson(o, type))
  @JvmStatic fun readJson(o: File): JsonNode = o.readJson()
  @JvmStatic fun <T> readJson(o: File, type: Class<T>): T = o.objectMap().readValue(o, type)
  @JvmStatic fun <T> readJson(o: File, ref: TypeReference<T>): T = o.objectMap().readValue(o, ref)
  @JvmStatic fun readJsonFile(o: String) = readJson(file(o))
  @JvmStatic fun <T> readJsonFile(o: String, type: Class<T>) = readJson(file(o), type)
  @JvmStatic fun <T> readJsonFile(o: String, ref: TypeReference<T>): T = readJson(file(o), ref)
  @JvmStatic fun readJson(o: String): JsonNode = o.readJson()
  @JvmStatic fun <T> readJson(o: String, type: Class<T>): T = MAPPER.readValue(o, type)
  @JvmStatic fun <T> readJson(o: String, ref: TypeReference<T>): T = MAPPER.readValue(o, ref)
  @JvmStatic fun readJson(o: ByteArray): JsonNode = o.readJson()
  @JvmStatic fun <T> readJson(o: ByteArray, type: Class<T>): T = MAPPER.readValue(o, type)
  @JvmStatic fun <T> readJson(o: ByteArray, ref: TypeReference<T>): T = MAPPER.readValue(o, ref)
  @JvmStatic fun writeJson(file: File, o: Any) = file.writeJson(o)
  @JvmStatic fun writeJson(s: String, o: Any) = writeJson(file(s), o)

  @JvmStatic fun date(): Date = DateUtil.date()
  @JvmStatic fun date(o: Long): Date = DateUtil.date(o)
  @JvmStatic fun date(o: TemporalAccessor): Date = DateUtil.date(o)
  @JvmStatic fun ldt(o: Date) = o.ldt()
  @JvmStatic fun ld(o: Date) = o.ld()
  @JvmStatic fun lt(o: Date) = o.lt()

  @JvmStatic fun isBlank(o: CharSequence) = StrUtil.isBlank(o)
  @JvmStatic fun nonBlank(o: CharSequence) = !isBlank(o)
  @JvmStatic
  fun blankToDefault(o: CharSequence, str: String): String = StrUtil.blankToDefault(o, str)
  @JvmStatic fun blankToDefault(o: CharSequence): String = blankToDefault(o, StrUtil.EMPTY)

  @JvmStatic fun <T> stream(o: Iterable<T>?) = stream(o, false)
  @JvmStatic
  fun <T> stream(o: Iterable<T>?, parallel: Boolean = false): Stream<T> =
      o?.run { StreamSupport.stream(spliterator(), parallel) } ?: Stream.empty()

  @JvmStatic fun isEmpty(o: Collection<*>?) = CollUtil.isEmpty(o)
  @JvmStatic fun isEmpty(o: Iterable<*>?) = IterUtil.isEmpty(o)
  @JvmStatic fun isEmpty(o: Array<*>?) = ArrayUtil.isEmpty(o)
  @JvmStatic fun isNotEmpty(o: Collection<*>?) = CollUtil.isNotEmpty(o)
  @JvmStatic fun nonEmpty(o: Collection<*>?) = CollUtil.isNotEmpty(o)
  @JvmStatic fun isNotEmpty(o: Iterable<*>?) = IterUtil.isNotEmpty(o)
  @JvmStatic fun nonEmpty(o: Iterable<*>?) = IterUtil.isNotEmpty(o)
  @JvmStatic fun isNotEmpty(o: Array<*>?) = ArrayUtil.isNotEmpty(o)
  @JvmStatic fun nonEmpty(o: Array<*>?) = ArrayUtil.isNotEmpty(o)
  @JvmStatic fun <T> emptyToNull(o: Collection<T>?): Collection<T>? = if (nonEmpty(o)) o else null
  @JvmStatic fun <T> emptyToNull(o: List<T>?): List<T>? = if (nonEmpty(o)) o else null
  @JvmStatic fun <T> emptyToNull(o: Set<T>?): Set<T>? = if (nonEmpty(o)) o else null
  @JvmStatic fun <T> emptyToNull(o: Array<T>?): Array<T>? = if (nonEmpty(o)) o else null

  @JvmStatic fun base58(o: ByteArray): String = o.base58()
  @JvmStatic fun sha256Hex(o: File) = o.sha256Hex()
  @JvmStatic fun sm3Hex(o: File) = o.sm3Hex()

  @JvmStatic
  fun base58(id1: String, id2: String, vararg data: String): String =
      listOf(id1, id2, *data).run {
        val ids = joinToString()
        base58(ids.toByteArray(UTF_8))
      }
}
