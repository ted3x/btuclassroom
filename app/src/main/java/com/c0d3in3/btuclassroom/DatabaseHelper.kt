package com.c0d3in3.btuclassroom

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class DatabaseHelper private constructor(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    init {
        instance = this
    }

    companion object {
        private var instance: DatabaseHelper? = null

        @Synchronized
        fun getInstance(ctx: Context) = instance ?: DatabaseHelper(ctx.applicationContext)
    }

    override fun onCreate(db: SQLiteDatabase) {
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}

val Context.database: DatabaseHelper
    get() = DatabaseHelper.getInstance(this)