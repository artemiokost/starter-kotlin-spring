package starter.core.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import starter.common.util.V1


@RestController
@RequestMapping("$V1/hello")
@Tag(name = "Hello", description = "Hello controller")
class HelloController {

    @GetMapping
    fun hello(): String {
        return "Hello HTTP/3!"
    }
}