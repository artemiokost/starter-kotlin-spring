package starter.common.util

import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant
import java.time.temporal.ChronoUnit

fun BigDecimal.plusPercent(percent: Int): Long {
    val multiply = this.multiply(BigDecimal(percent / 100.00))
    return this.plus(multiply.setScale(1, RoundingMode.HALF_DOWN)).toLong()
}

fun BigDecimal.minusPercent(percent: Int): Long {
    val multiply = this.multiply(BigDecimal(percent / 100.00))
    return this.minus(multiply.setScale(1, RoundingMode.HALF_DOWN)).toLong()
}

fun BigDecimal.getPercent(percent: Int): Long {
    val multiply = this.multiply(BigDecimal(percent / 100.00))
    return multiply.setScale(1, RoundingMode.HALF_DOWN).toLong()
}

fun Instant.prevHour(): Instant {
    return this.truncatedTo(ChronoUnit.HOURS).minus(1, ChronoUnit.HOURS)
}

fun Instant.currentHour(): Instant {
    return this.truncatedTo(ChronoUnit.HOURS)
}

fun Instant.nextHour(): Instant {
    return this.truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.HOURS)
}

fun Long.plusPercent(percent: Int): Long {
    return this.toBigDecimal().plusPercent(percent)
}

fun Long.minusPercent(percent: Int): Long {
    return this.toBigDecimal().minusPercent(percent)
}

fun Long.getPercent(percent: Int): Long {
    return this.toBigDecimal().getPercent(percent)
}