package com.qiusuo.videoeditor.common.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProjectDao {
    @Query("SELECT * FROM project")
    fun queryAll(): List<Project>

    @Query("SELECT * FROM project WHERE projectId = :id")
    fun queryById(id: String): Project

    @Query("SELECT * FROM project WHERE projectId in (:ids)")
    fun queryByids(ids: Array<String>): List<Project>

    @Delete
    fun delete(project: Project)

    @Insert
    fun insert(project: Project)

    @Insert
    fun inserAll(projects: List<Project>)
}