package starter.core.telegram

import com.pengrad.telegrambot.TelegramBot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TelegramBotConfiguration(
    @Value("\${app.telegram.token}")
    private var token: String,
) {

    @Bean
    fun tgBot(): TelegramBot = TelegramBot(token)
}