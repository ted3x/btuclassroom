package com.c0d3in3.btuclassroom

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jetbrains.anko.db.MapRowParser
import org.jetbrains.anko.db.select
import java.sql.Blob

import java.util.*

class UserInfo(val context: Context) {
    fun getInfo() : ArrayList<UserData> = context.database.use {
        val data = ArrayList<UserData>()

        select("userInfo", "_userImage")
            .parseList(object: MapRowParser<List<UserData>> {
                override fun parseRow(columns: Map<String, Any?>): List<UserData> {
                    val userImage = columns.getValue("_userImage")


                    val schedule = UserData(userImage as ByteArray)

                    data.add(schedule)

                    return data
                }
            })

        data
    }
}

@Parcelize
data class UserData(val _userImage: ByteArray): Parcelable{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserData

        if (!_userImage.contentEquals(other._userImage)) return false

        return true
    }

    override fun hashCode(): Int {
        return _userImage.contentHashCode()
    }
}