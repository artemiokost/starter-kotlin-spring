package starter.common.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.Instant

open class PersistEntity : Serializable {
    @Id
    var id: Long? = null

    @CreatedDate
    var created: Instant = Instant.now()

    @LastModifiedDate
    var updated: Instant = Instant.now()
}
