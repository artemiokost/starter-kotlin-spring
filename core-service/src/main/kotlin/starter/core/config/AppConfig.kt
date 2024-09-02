package starter.core.config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.coroutines.CoroutineContext

@Configuration
class AppConfig {

    @Bean
    fun coroutineContext(): CoroutineContext = Dispatchers.IO + SupervisorJob()
}