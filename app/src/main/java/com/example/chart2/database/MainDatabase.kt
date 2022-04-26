package com.example.chart2.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    version = 1,
    entities = [Item::class]
)
abstract class MainDatabase(): RoomDatabase(){
    abstract val dao: ItemDao
}