package starter.core.converter

import com.fasterxml.jackson.databind.type.MapType
import io.r2dbc.postgresql.codec.Json
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.stereotype.Component
import starter.common.util.Utils.jsonMapper

@Component
@ReadingConverter
class JsonToMapAnyConverter : Converter<Json, Map<String, Any?>> {

    companion object {
        val mapType: MapType = jsonMapper.typeFactory.constructMapType(Map::class.java, String::class.java, Any::class.java)
    }

    override fun convert(source: Json): Map<String, Any?>? {
        return jsonMapper.readValue(source.asString(), mapType)
    }
}