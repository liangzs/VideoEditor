package com.qiusuo.videoeditor.common.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Project::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}