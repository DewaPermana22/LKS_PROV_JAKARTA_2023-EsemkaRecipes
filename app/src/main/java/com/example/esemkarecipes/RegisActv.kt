package com.example.esemkarecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date

class RegisActv : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_regis_actv)
         val masuk : TextView = findViewById(R.id.masuk)
        val usrnme : TextView =findViewById(R.id.et_usrname_regis)
        val fulnme : TextView =findViewById(R.id.et_fullname_regis)
        val passw : TextView =findViewById(R.id.et_pw_regis)
        val confpassw : TextView =findViewById(R.id.et_conf_pw)
        val dob : TextView =findViewById(R.id.DOB)
        val  sign : Button = findViewById(R.id.btn_sign)

        masuk.setOnClickListener{
                val regis = Intent(this, MainActivity::class.java)
                regis.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(regis)
                finish()
            }

        sign.setOnClickListener{
            if (usrnme.text.toString() != "" && fulnme.text.toString() != ""){
                if (passw.text.toString() != confpassw.text.toString()){
                    Toast.makeText(this, "Password and Confirm password must be same", Toast.LENGTH_SHORT).show()
                } else {
                    daftar(usrnme.text.toString(), fulnme.text.toString(), passw.text.toString(), dob.text.toString()) { reg ->
                        CoroutineScope(Dispatchers.Main).launch{
                            if (reg == true){
                                Toast.makeText(this@RegisActv, "Berhasil buat akun", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(this@RegisActv, "Akses ditolak", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
    fun daftar(username : String, fullname : String, password : String, dateofbirth : String, berhasil : (Boolean) -> Unit){
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/api/sign-up").openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/json")
            conn.requestMethod = "POST"
            val request = """
            {
                "username" : "$username",
                "fullName" : "$fullname",
                "password" : "$password",
                "dateOfBirth" : "$dateofbirth"
            }
        """.trimIndent()
            Log.d("Request User", "req $request")
            conn.outputStream.use { os ->
                os.write(request.toByteArray())
            }

            val resCode = conn.responseCode
            Log.d("Respon", "res $resCode")
            if (resCode in 200..299){
                val read = InputStreamReader(conn.inputStream).use { r ->
                    r.readText()
                }
                if (read.isNotEmpty()){
                    berhasil(true)
                } else{
                    berhasil(false)
                }
            }
        }
    }
}