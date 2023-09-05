package com.qiusuo.videoeditor.common.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Project::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
}