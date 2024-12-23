package com.example.esemkarecipes

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class Profile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profile(view)
        fecth_liked_recipes(view)
        return view
    }

    fun profile(view: View){
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/api/me").openStream().bufferedReader().readText()
            val res = JSONObject(conn)
            val nameImage = res.getString("image")
            val urlImage = URL("http://10.0.2.2:5000/images/profiles/$nameImage").openStream()
            val resImg = BitmapFactory.decodeStream(urlImage)
            withContext(Dispatchers.Main){
                view.findViewById<TextView>(R.id.name_user).text = res.getString("fullName")
                view.findViewById<ImageView>(R.id.profile_pict).setImageBitmap(resImg)
            }
        }
    }

    fun fecth_liked_recipes(view: View){
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/api/me/liked-recipes").openStream().bufferedReader().readText()
            val res = JSONArray(conn)
            withContext(Dispatchers.Main){
               val rc = view.findViewById<RecyclerView>(R.id.rc_liked_recipes)
                   rc.layoutManager = GridLayoutManager(context, 2)
                rc.adapter = adapter_liked(res) { it ->
                    val intent = Intent(context, DetailRecipes::class.java)
                    intent.putExtra("id", it.getInt("id"))
                    val jsonObjRec = it.getJSONObject("category")
                    intent.putExtra("name", jsonObjRec.getString("name"))
                    intent.putExtra("icon", jsonObjRec.getString("icon"))
                    startActivity(intent)
                }
            }
        }
    }
}