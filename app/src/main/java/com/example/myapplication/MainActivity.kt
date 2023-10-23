package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import android.widget.Toast



data class User(val username: String, val password: String, val role: String)


class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_adminmenu)
        setContentView(R.layout.activity_bartender)
        setContentView(R.layout.activity_server)

        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            val role = authenticateUser(username, password)

            if (role != null) {
                handleUserRole(role)
            } else {
                showToast("Invalid login! Please try again!")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun authenticateUser(username: String, password: String): String? {
        val users = readUsersFromJSONFile("users.json")
        val user = users.find { it.username == username && it.password == password }
        return user?.role
    }

    private fun handleUserRole(role: String) {
        val layoutId = when (role) {
            "bartender" -> R.layout.activity_bartender
            "server" -> R.layout.activity_server
            "kitchen" -> R.layout.activity_kitchen
            "admin" -> R.layout.activity_admin
            else -> R.layout.activity_main // A default layout for an unknown role
        }

        setContentView(layoutId)
    }


    private fun readUsersFromJSONFile(fileName: String): List<User> {
        val users = mutableListOf<User>()
        try {
            val jsonStr = application.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonStr)
            for (i in 0 until jsonArray.length()) {
                val userJson = jsonArray.getJSONObject(i)
                val username = userJson.getString("username")
                val password = userJson.getString("password")
                val role = userJson.getString("role")
                users.add(User(username, password, role))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return users
    }
}



