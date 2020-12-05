package com.c0d3in3.btuclassroom.data.local.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(

    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "full_name") val fullName: String?,
    @ColumnInfo(name = "user_image") val userImage: ByteArray?,
    @ColumnInfo(name = "rating") val userRating : Long?,
    @ColumnInfo(name = "credits") val userCredits : Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userImage != null) {
            if (other.userImage == null) return false
            if (!userImage.contentEquals(other.userImage)) return false
        } else if (other.userImage != null) return false

        return true
    }

    override fun hashCode(): Int {
        return userImage?.contentHashCode() ?: 0
    }
}