package com.example.sqlitecrud

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PersonDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "person.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "persons"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SURNAME = "surname"
        private const val COLUMN_MOBILE = "mobile"
        private const val COLUMN_AGE = "age"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_SURNAME TEXT,
                $COLUMN_MOBILE TEXT,
                $COLUMN_AGE INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    // CREATE
    fun insertPerson(person: Person): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, person.name)
            put(COLUMN_SURNAME, person.surname)
            put(COLUMN_MOBILE, person.mobile)
            put(COLUMN_AGE, person.age)
        }
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    // READ (Get all persons)
    fun getAllPersons(): List<Person> {
        val persons = mutableListOf<Person>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val person = Person(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                surname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME)),
                mobile = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
            )
            persons.add(person)
        }
        cursor.close()
        db.close()
        return persons
    }

    // READ (Get single person by ID)
    fun getPersonById(id: Long): Person? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val person = Person(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                surname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SURNAME)),
                mobile = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MOBILE)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE))
            )
            cursor.close()
            db.close()
            person
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    // UPDATE
    fun updatePerson(person: Person): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, person.name)
            put(COLUMN_SURNAME, person.surname)
            put(COLUMN_MOBILE, person.mobile)
            put(COLUMN_AGE, person.age)
        }
        val rowsAffected = db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(person.id.toString())
        )
        db.close()
        return rowsAffected
    }

    // DELETE
    fun deletePerson(id: Long): Int {
        val db = writableDatabase
        val rowsAffected = db.delete(
            TABLE_NAME,
            "$COLUMN_ID = ?",
            arrayOf(id.toString())
        )
        db.close()
        return rowsAffected
    }
}