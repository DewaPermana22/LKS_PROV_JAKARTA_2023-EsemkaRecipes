package com.example.esemkarecipes

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text
import java.net.URL

class DetailRecipes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_recipes)
        val id = intent.getIntExtra("id", 0)
        val name_category = intent.getStringExtra("name").toString()
        val imgCategory = intent.getStringExtra("icon").toString()

        Log.d("Value Dari Recipes : ", "Value : $id")
        fetchDetail(id, name_category, imgCategory, id)
    }

    fun fetchDetail(id : Int, nameCategory : String, icon : String, recipeID : Int){
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/api/recipes/detail/$id").openStream().bufferedReader().readText()
            val urlRecipes = URL("http://10.0.2.2:5000/api/recipes/like-recipe?recipeId=$recipeID").openStream().bufferedReader().readText().trim().toBoolean()
            val obj = JSONObject(conn)
            Log.d("Response", "Res : $obj")
            val imgName = obj.getString("image")
            val url = URL("http://10.0.2.2:5000/images/recipes/$imgName").openStream()
            val urlImgCategory = URL("http://10.0.2.2:5000/images/categories/$icon").openStream()
            val gambar = BitmapFactory.decodeStream(url)
            val gambarCategory = BitmapFactory.decodeStream(urlImgCategory)
            val name = obj.getString("title")
            val time = obj.getString("cookingTimeEstimate")
            val price = obj.getString("priceEstimate")
            val desc = obj.getString("description")
            val ingredients = obj.getJSONArray("ingredients")
            val list_ingredients = mutableListOf<String>()
            for (i in 0 until ingredients.length()){
                list_ingredients.add(ingredients.getString(i))
            }
            val steps = obj.getJSONArray("steps")
            val list_steps = mutableListOf<String>()
            for ( i in 0 until steps.length()){
                list_steps.add(steps.getString(i))
            }
            Log.d("Islike?", "Ini dilike? : $urlRecipes")
            withContext(Dispatchers.Main){
                findViewById<TextView>(R.id.des_menu).text = desc.toString()
                findViewById<TextView>(R.id.Judul_menu).text = name.toString()
                findViewById<TextView>(R.id.nameMenu).text = name.toString()
                findViewById<ImageView>(R.id.img_Menu).setImageBitmap(gambar)
                findViewById<TextView>(R.id.estimate_time).text = "± $time Minutes"
                findViewById<TextView>(R.id.price_estimate).text = "$$price"
                findViewById<TextView>(R.id.name_categories).text = nameCategory
                findViewById<ImageView>(R.id.img_categories).setImageBitmap(gambarCategory)
                findViewById<TextView>(R.id.ingredients).text = list_ingredients.joinToString(separator = "\n")
                findViewById<TextView>(R.id.steps).text = list_steps.joinToString(separator = "\n")
                val imgLike = findViewById<ImageView>(R.id.isLike)
                if (urlRecipes == true){
                    imgLike.setImageResource(R.drawable.liked)
                } else {
                    imgLike.setImageResource(R.drawable.like)
                }
            }
        }
    }
}