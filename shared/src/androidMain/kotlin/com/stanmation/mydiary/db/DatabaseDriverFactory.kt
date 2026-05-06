package com.stanmation.mydiary.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.stanmation.mydiary.database.DiaryDatabase

class DatabaseDriverFactory(private val context: Context) {

    fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = DiaryDatabase.Companion.Schema,
            context = context,
            name = "diary.db"
        )
    }
}