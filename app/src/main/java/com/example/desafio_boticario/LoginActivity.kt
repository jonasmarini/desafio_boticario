package com.example.desafio_boticario

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.orm.query.Condition
import com.orm.query.Select
import constants.Constants
import entities.UserEntity

class LoginActivity : AppCompatActivity() {

    lateinit var layoutLogin: ConstraintLayout

    lateinit var inputLoginEmail: TextInputLayout
    lateinit var inputLoginPassword: TextInputLayout
    lateinit var editEmail: TextInputEditText
    lateinit var editPassword: TextInputEditText
    lateinit var btnLogin: MaterialButton
    lateinit var layoutRegister: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setComponents()
    }

    private fun setComponents(){
        layoutLogin = findViewById(R.id.layout_login)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        btnLogin = findViewById(R.id.btn_login)
        layoutRegister = findViewById(R.id.layout_register)
        inputLoginEmail = findViewById(R.id.input_login_email)
        inputLoginPassword = findViewById(R.id.input_login_password)

        btnLogin.setOnClickListener {
            login()
        }

        layoutRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val validEmail = validateEmail()
        val validPassword = validatePassword()

        if (validEmail && validPassword) {
            val user = Select.from(UserEntity::class.java)
                .where(Condition.prop("email").eq(editEmail.text.toString()),
                    Condition.prop("password").eq(editPassword.text.toString()))
                .first()

            if(user == null){
                Snackbar.make(layoutRegister, getString(R.string.login_error), Snackbar.LENGTH_LONG).show()
            } else {

                val sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(this)
                val editor = sharedPreferences.edit()
                editor.putLong(Constants.USER_LOGGED_ID, user.id).apply()

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validateEmail(): Boolean {
        val email = editEmail.text.toString()
        if (email.isEmpty()) {
            inputLoginEmail.error = "Digite um email"
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputLoginEmail.error = "Digite um email válido"
            return false
        }
        return true
    }

    private fun validatePassword(): Boolean {
        val password = editPassword.text.toString().trim()
        if (password.isEmpty()) {
            inputLoginPassword.error = "Digite uma senha"
            return false
        }
        if (password.length < 6) {
            inputLoginPassword.error = "A senha deve conter ao mesno 6 dígitos"
            return false
        }
        return true
    }
}