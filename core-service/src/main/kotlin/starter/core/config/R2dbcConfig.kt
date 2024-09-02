package starter.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.dialect.PostgresDialect
import starter.core.converter.JsonToMapAnyConverter

@Configuration
@EnableR2dbcAuditing
class R2dbcConfig {

    @Bean
    fun r2dbcCustomConversions(): R2dbcCustomConversions {
        val converters: MutableList<Converter<*, *>> = ArrayList()
        converters.add(JsonToMapAnyConverter())
        return R2dbcCustomConversions.of(PostgresDialect.INSTANCE, converters)
    }
}