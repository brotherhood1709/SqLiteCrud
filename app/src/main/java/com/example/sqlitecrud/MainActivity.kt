package com.example.sqlitecrud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: PersonDatabaseHelper
    private lateinit var nameInput: EditText
    private lateinit var surnameInput: EditText
    private lateinit var mobileInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var idInput: EditText
    private lateinit var outputText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = PersonDatabaseHelper(this)

        // Initialize views
        nameInput = findViewById(R.id.nameInput)
        surnameInput = findViewById(R.id.surnameInput)
        mobileInput = findViewById(R.id.mobileInput)
        ageInput = findViewById(R.id.ageInput)
        idInput = findViewById(R.id.idInput)
        outputText = findViewById(R.id.outputText)

        // Buttons
        findViewById<Button>(R.id.insertButton).setOnClickListener { insertPerson() }
        findViewById<Button>(R.id.readAllButton).setOnClickListener { readAllPersons() }
        findViewById<Button>(R.id.readByIdButton).setOnClickListener { readPersonById() }
        findViewById<Button>(R.id.updateButton).setOnClickListener { updatePerson() }
        findViewById<Button>(R.id.deleteButton).setOnClickListener { deletePerson() }
    }

    private fun insertPerson() {
        try {
            val person = Person(
                name = nameInput.text.toString(),
                surname = surnameInput.text.toString(),
                mobile = mobileInput.text.toString(),
                age = ageInput.text.toString().toInt()
            )
            val id = dbHelper.insertPerson(person)
            Toast.makeText(this, "Person inserted with ID: $id", Toast.LENGTH_SHORT).show()
            clearInputs()
        } catch (e: Exception) {
            Toast.makeText(this, "Error inserting person: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readAllPersons() {
        val persons = dbHelper.getAllPersons()
        outputText.text = persons.joinToString("\n") {
            "ID: ${it.id}, Name: ${it.name}, Surname: ${it.surname}, Mobile: ${it.mobile}, Age: ${it.age}"
        }
    }

    private fun readPersonById() {
        try {
            val id = idInput.text.toString().toLong()
            dbHelper.getPersonById(id)?.let { person ->
                outputText.text = "ID: ${person.id}, Name: ${person.name}, Surname: ${person.surname}, Mobile: ${person.mobile}, Age: ${person.age}"
            } ?: Toast.makeText(this, "Person not found", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading person: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePerson() {
        try {
            val id = idInput.text.toString().toLong()
            val person = Person(
                id = id,
                name = nameInput.text.toString(),
                surname = surnameInput.text.toString(),
                mobile = mobileInput.text.toString(),
                age = ageInput.text.toString().toInt()
            )
            val rowsAffected = dbHelper.updatePerson(person)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Person updated", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this, "Person not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error updating person: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deletePerson() {
        try {
            val id = idInput.text.toString().toLong()
            val rowsAffected = dbHelper.deletePerson(id)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Person deleted", Toast.LENGTH_SHORT).show()
                clearInputs()
            } else {
                Toast.makeText(this, "Person not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error deleting person: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputs() {
        nameInput.text.clear()
        surnameInput.text.clear()
        mobileInput.text.clear()
        ageInput.text.clear()
        idInput.text.clear()
    }
}