package com.billioapp.data.mapper

import com.billioapp.domain.model.User
import dev.gitlive.firebase.auth.FirebaseUser

class UserMapper {
    fun map(user: FirebaseUser): User = User(
        id = user.uid,
        email = user.email ?: "",
        displayName = user.displayName
    )

    fun mapOrNull(user: FirebaseUser?): User? = user?.let(::map)
}
