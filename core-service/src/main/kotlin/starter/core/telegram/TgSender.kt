package starter.core.telegram

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup
import com.pengrad.telegrambot.model.request.ParseMode
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup
import com.pengrad.telegrambot.request.SendDocument
import com.pengrad.telegrambot.request.SendMessage
import com.pengrad.telegrambot.response.SendResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class TgSender(
    private var tgBot: TelegramBot,
    @Value("\${app.telegram.chatId}")
    private var chatId: Long,
    @Value("\${app.telegram.pingDevs}")
    private var pingDevs: String,
    @Value("\${app.telegram.enable}")
    private var enableBot: Boolean,
) {

    fun sendChatMessage(
        text: String,
        telegramChatId: String,
        replyKeyboardMarkup: ReplyKeyboardMarkup? = null,
        inlineKeyboardMarkup: InlineKeyboardMarkup? = null,
    ): SendResponse {
        val message = SendMessage(telegramChatId, text)
            .parseMode(ParseMode.HTML)
            .disableWebPagePreview(true)
        replyKeyboardMarkup?.let {
            message.replyMarkup(replyKeyboardMarkup)
        }
        inlineKeyboardMarkup?.let {
            message.replyMarkup(inlineKeyboardMarkup)
        }

        return tgBot.execute(message)
    }

    fun sendException(ex: Throwable): Boolean {
        if (!enableBot) return false
        val stacktrace = ex.stackTraceToString()
        val caption = wrapExceptionCaption(ex.toString())
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")
        val filename = "log-${LocalDateTime.now().format(formatter)}.txt"
        return sendFile(stacktrace, caption, filename)
    }

    fun sendMessage(text: String): Boolean = tgBot.execute(buildMessageRequest(text)).isOk

    fun sendMessageWithInfo(text: String): SendResponse? = tgBot.execute(buildMessageRequest(text))

    fun sendFile(data: String, caption: String?, filename: String?): Boolean {
        val request = SendDocument(chatId, data.toByteArray())
            .parseMode(ParseMode.HTML)
            .disableNotification(true)
            .caption(caption)
            .fileName(filename)
        val execute = tgBot.execute(request)
        return execute.isOk
    }

    private fun wrapExceptionCaption(message: String) =
        "#alert #news\n&#8252$pingDevs\nSomething went wrong\n\n$message"

    private fun buildMessageRequest(text: String) = SendMessage(chatId, text)
        .parseMode(ParseMode.HTML)
        .disableWebPagePreview(true)
        .disableNotification(true)
}

