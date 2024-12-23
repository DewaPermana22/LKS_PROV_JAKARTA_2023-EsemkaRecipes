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
import org.json.JSONObject
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val session = getSharedPreferences("user_session", MODE_PRIVATE)
        //getSharedPreferences("user_session", MODE_PRIVATE).edit().remove("id").apply()
        val id_sesion = session.getInt("id", -1)
        Log.d("Session", "User Id $id_sesion")
        if ( id_sesion != -1){
            startActivity(Intent(this, Home::class.java))
        } else {
            Toast.makeText(this, "Silahkan Login Ulang!", Toast.LENGTH_SHORT).show()
        }
        val reg : TextView = findViewById(R.id.to_reg)
        val username : TextView = findViewById(R.id.et_usrname)
        val password : TextView = findViewById(R.id.et_pw)
        val login : Button = findViewById(R.id.btn_login)

        login.setOnClickListener{
            if (username.text.toString() != "" && password.text.toString() != ""){
                masuk(username.text.toString(), password.text.toString()) { reg ->
                    CoroutineScope(Dispatchers.Main).launch{
                        if (reg == true){
                            Toast.makeText(this@MainActivity, "Berhasil Masuk", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, Home::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Akses ditolak", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        reg.setOnClickListener{
            val regis = Intent(this, RegisActv::class.java)
            regis.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(regis)
            finish()
        }


    }
    fun masuk(username : String, password : String, berhasil : (Boolean) -> Unit){
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/api/sign-in").openConnection() as HttpURLConnection
            conn.setRequestProperty("Content-Type", "application/json")
            conn.requestMethod = "POST"
            val request = """
            {
                "username" : "$username",
                "password" : "$password"
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
                    val id = JSONObject(read)
                    val usr_id = id.getInt("id")
                    val sharpref = getSharedPreferences("user_session", MODE_PRIVATE)
                    sharpref.edit().putInt("id", usr_id).apply()
                    Log.d("Session User", "User Id In Session : $usr_id")
                    Log.d("Log", "Isi Read $read")
                } else{
                    berhasil(false)
                }
            }
        }
    }
}