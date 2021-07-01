package com.example.desafio_boticario

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity() {

    lateinit var inputRegisterName: TextInputLayout
    lateinit var inputRegisterEmail: TextInputLayout
    lateinit var inputRegisterPassword: TextInputLayout
    lateinit var inputRegisterConfirmPassword: TextInputLayout

    lateinit var editRegisterName: TextInputEditText
    lateinit var editRegisterEmail: EditText
    lateinit var editRegisterPassword: TextInputEditText
    lateinit var editRegisterConfirmPassword: TextInputEditText

    lateinit var btnRegister: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setComponents()
    }

    private fun setComponents() {

        inputRegisterName = findViewById(R.id.input_register_name)
        inputRegisterEmail = findViewById(R.id.input_register_email)
        inputRegisterPassword = findViewById(R.id.input_register_password)
        inputRegisterConfirmPassword = findViewById(R.id.input_register_confirm_password)

        editRegisterName = findViewById(R.id.register_edit_name)
        editRegisterEmail = findViewById(R.id.register_edit_email)
        editRegisterPassword = findViewById(R.id.register_edit_password)
        editRegisterConfirmPassword = findViewById(R.id.register_edit_confirm_password)

        btnRegister = findViewById(R.id.btn_register)

        btnRegister.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val validName = validateName()
        val validEmail = validateEmail()
        val validPassword = validatePassword()
        val validConfirmPassword = validateConfirmPassword()

        if (validName && validEmail && validPassword && validConfirmPassword) {
            //TODO faça login
            print("Faça login")
        }

    }

    private fun validateName(): Boolean {
        val name = editRegisterName.text.toString().trim()
        if (name.isEmpty()) {
            inputRegisterName.error = "Digite o nome"
            return false
        }
        return true
    }

    private fun validateEmail(): Boolean {
        val email = editRegisterEmail.text.toString()
        if (email.isEmpty()) {
            inputRegisterEmail.error = "Digite um email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputRegisterEmail.error = "Digite um email válido"
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        val password = editRegisterPassword.text.toString().trim()
        if (password.isEmpty()) {
            inputRegisterPassword.error = "Digite uma senha"
            return false
        }
        if (password.length < 6) {
            inputRegisterPassword.error = "A senha deve conter ao mesno 6 dígitos"
            return false
        }
        return true
    }

    private fun validateConfirmPassword(): Boolean {
        val password = editRegisterPassword.text.toString().trim()
        val confirmPassword = editRegisterConfirmPassword.text.toString().trim()
        if (confirmPassword.isEmpty()) {
            inputRegisterConfirmPassword.error = "Digite a confirmação da senha"
            return false
        }
        if (confirmPassword != password) {
            inputRegisterConfirmPassword.error = "As senhas não são iguais"
            return false
        }
        return true
    }
}


//