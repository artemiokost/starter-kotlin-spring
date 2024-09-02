package starter.common.util

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import starter.common.error.SomethingWentWrongException
import java.time.Clock
import java.time.Instant
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Utils {

    private const val GAME_HASH_KEY = "6kYxA6SMuM"

    fun decodeBase64(s: String) = String(Base64.getDecoder().decode(s.toByteArray()))

    fun encodeBase64(s: String) = String(Base64.getEncoder().encode(s.toByteArray()))

    val jsonMapper: JsonMapper = JsonMapper.builder()
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY)
        .enable(SerializationFeature.INDENT_OUTPUT)
        .build()

    fun generateGameHash(rq: Any): String {
        val typeRef: TypeReference<MutableMap<String, Any>> = object : TypeReference<MutableMap<String, Any>>() {}
        val payload: MutableMap<String, Any> = jsonMapper.convertValue(rq, typeRef)
        val serializedString = payload.toSortedMap().toList()
            .joinToString("\n") {
                "${it.first}=${it.second}"
            }
        return hex(hmac_sha256(serializedString, GAME_HASH_KEY.toByteArray()))
    }

    fun validateGameHash(rq: Any, gameHash: String) {
        val hash = generateGameHash(rq)
        if (gameHash != hash) {
            throw SomethingWentWrongException()
        }
    }

    fun hashPlaydeckData(payload: Map<String, Any>, botToken: String): String {
        val serializedString = payload.toSortedMap().toList()
            .filter { it.first != "hash" }
            .joinToString("\n") {
                "${it.first}=${it.second}"
            }
        return hex(hmac_sha256(serializedString, hmac_sha256(botToken, "WebAppData".toByteArray())))
    }

    private fun hex(bytes: ByteArray): String {
        val digits = "0123456789abcdef".toCharArray()
        return buildString(bytes.size * 2) {
            bytes.forEach { byte ->
                val b = byte.toInt() and 0xFF
                append(digits[b shr 4])
                append(digits[b and 0x0F])
            }
        }
    }

    private fun hmac_sha256(data: String, key: ByteArray): ByteArray {
        val hmacSha256: Mac = Mac.getInstance("HmacSHA256")
        hmacSha256.init(SecretKeySpec(key, "HmacSHA256"))
        return hmacSha256.doFinal(data.toByteArray())
    }

    fun utcNow(): Instant = Instant.now(Clock.systemUTC())

    fun utcNowMilli() = utcNow().toEpochMilli()
}