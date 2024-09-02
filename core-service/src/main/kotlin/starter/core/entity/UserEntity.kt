package starter.core.entity

import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import starter.common.entity.PersistEntity
import starter.common.type.UserStatusType

@Table(schema = "public", name = "user")
class UserEntity : PersistEntity() {

    @Column("avatar")
    var avatar: String? = null

    @Column("username")
    var username: String? = null

    var status: UserStatusType = UserStatusType.ACTIVE
}