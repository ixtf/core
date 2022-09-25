package com.gitee.ixtf
import cn.hutool.core.collection.CollUtil
import cn.hutool.core.collection.IterUtil
import cn.hutool.core.date.DateUtil
import cn.hutool.core.date.LocalDateTimeUtil
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAccessor
import java.util.*
import java.util.stream.Stream
import java.util.stream.StreamSupport

object J {
    @JvmStatic
    fun date(): Date {
        return DateUtil.date()
    }

    @JvmStatic
    fun date(date: Long): Date {
        return DateUtil.date(date)
    }

    @JvmStatic
    fun date(date: TemporalAccessor): Date {
        return DateUtil.date(date)
    }

    @JvmStatic
    fun ldt(date: Date): LocalDateTime {
        return LocalDateTimeUtil.of(date)
    }

    @JvmStatic
    fun ld(date: Date): LocalDate {
        return ldt(date).toLocalDate()
    }

    @JvmStatic
    fun lt(date: Date): LocalTime {
        return ldt(date).toLocalTime()
    }

    /**
     * 集合是否为空
     *
     * @param collection 集合
     * @return 是否为空
     */
    @JvmStatic
    fun isEmpty(collection: Collection<*>?): Boolean {
        return CollUtil.isEmpty(collection)
    }

    @JvmStatic
    fun nonEmpty(collection: Collection<*>?): Boolean {
        return CollUtil.isNotEmpty(collection)
    }

    @JvmStatic
    fun <T>emptyIfNull(collection: List<T>?): List<T> {
        return CollUtil.emptyIfNull(collection)
    }

    @JvmStatic
    fun <T>emptyIfNull(collection: Set<T>?): Set<T> {
        return CollUtil.emptyIfNull(collection)
    }

    @JvmStatic
    fun <T>stream(iterable: Iterable<T>, parallel: Boolean = false): Stream<T> {
        if (IterUtil.isEmpty(iterable)) return Stream.empty()
        return StreamSupport.stream(iterable.spliterator(), parallel)
    }
}
