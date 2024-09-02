package starter.core.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.PageRequest
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer

@Configuration
class PageableConfig {

    @Bean
    fun pageableResolverCustomizer(): PageableHandlerMethodArgumentResolverCustomizer {
        return PageableHandlerMethodArgumentResolverCustomizer { pageableResolver ->
            pageableResolver.setMaxPageSize(999999999)
            pageableResolver.setFallbackPageable(PageRequest.of(0, 999999999))
        }
    }
}