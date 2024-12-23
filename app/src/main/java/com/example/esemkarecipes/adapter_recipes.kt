package com.example.esemkarecipes

import android.graphics.BitmapFactory
import android.net.ConnectivityDiagnosticsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class adapter_recipes(val data : JSONArray, val Click : (JSONObject) -> Unit) : RecyclerView.Adapter<adapter_recipes.holder_recipes>() {

    class holder_recipes(view: View) : RecyclerView.ViewHolder(view.rootView){
        val name : TextView = view.findViewById(R.id.name_menu_recipes)
        val desc : TextView = view.findViewById(R.id.desc_recipes)
        val image : ImageView = view.findViewById(R.id.image_menu_recipes)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapter_recipes.holder_recipes {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipes, parent, false)
        return holder_recipes(view)
    }

    override fun onBindViewHolder(holder: adapter_recipes.holder_recipes, position: Int) {
        val response = data.getJSONObject(position)
        val idRecipes = response.getInt("id")
        holder.name.text = response.getString("title")
        holder.desc.text = response.getString("description")
        val nameImg = response.getString("image")
        CoroutineScope(Dispatchers.IO).launch{
            val url = URL("http://10.0.2.2:5000/images/recipes/$nameImg").openStream()
            val imgRes = BitmapFactory.decodeStream(url)
            withContext(Dispatchers.Main){
                holder.image.setImageBitmap(imgRes)
            }
        }
        holder.itemView.setOnClickListener{
            Click(response)
        }
    }

    override fun getItemCount(): Int {
        return data.length()
    }

}