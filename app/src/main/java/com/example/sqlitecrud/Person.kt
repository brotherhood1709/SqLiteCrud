package com.example.sqlitecrud

data class Person(
    val id: Long = 0,
    val name: String,
    val surname: String,
    val mobile: String,
    val age: Int
)