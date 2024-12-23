package com.example.esemkarecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.w3c.dom.Text
import java.net.URL

class RecipesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipes)
        findViewById<ImageView>(R.id.back).setOnClickListener{
            startActivity(Intent(this, Home::class.java))
        }

        val nf_ly = findViewById<LinearLayout>(R.id.ly_nf)
        val tv_nf = findViewById<TextView>(R.id.tv_nf)
        val img_nf = findViewById<ImageView>(R.id.img_nf)

        val Id = intent.getIntExtra("id", 0).toString()
        val name = intent.getStringExtra("name").toString()
        val name_categories = intent.getStringExtra("icon").toString()
        findViewById<TextView>(R.id.name_menu).text = name
        Log.d("Id daari fragment", "Id : $Id")

        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/api/recipes?categoryId=$Id").openStream().bufferedReader().readText()
            val data = JSONArray(conn)
            withContext(Dispatchers.Main){
                val rc_recipes : RecyclerView = findViewById(R.id.rc_recipes)
                rc_recipes.layoutManager = LinearLayoutManager(this@RecipesActivity)
                rc_recipes.adapter = adapter_recipes(data){ dt ->
                    val intent = Intent(this@RecipesActivity, DetailRecipes::class.java)
                    intent.putExtra("id", dt.getInt("id"))
                    intent.putExtra("name", name)
                    intent.putExtra("icon", name_categories)
                    startActivity(intent)
                }

                if (data.length() == 0){
                    nf_ly.visibility = View.VISIBLE
                    tv_nf.visibility = View.VISIBLE
                    img_nf.visibility = View.VISIBLE
                    rc_recipes.visibility = View.GONE
                } else {
                    nf_ly.visibility = View.GONE
                    tv_nf.visibility = View.GONE
                    img_nf.visibility = View.GONE
                    rc_recipes.visibility = View.VISIBLE
                }
            }

        }
    }
}