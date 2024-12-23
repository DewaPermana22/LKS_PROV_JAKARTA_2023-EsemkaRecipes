package com.example.esemkarecipes

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class adapter_liked (val data : JSONArray, val click : (JSONObject) -> Unit ) : RecyclerView.Adapter<adapter_liked.viewHolder>() {

    class viewHolder(view: View) : RecyclerView.ViewHolder(view){
        val image : ImageView = view.findViewById(R.id.img_liked)
        //val imgLike = view.findViewById<ImageView>(R.id.isLike)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapter_liked.viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_liked, parent, false)
        return viewHolder(view)
    }

    override fun onBindViewHolder(holder: adapter_liked.viewHolder, position: Int) {
        val res = data.getJSONObject(position)
        val nameImage = res.getString("image")
        val recipeId = res.getInt("id")
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/images/recipes/$nameImage").openStream()
            //val urlRecipes = URL("http://10.0.2.2:5000/api/recipes/like-recipe?recipeId=$recipeId").openStream().bufferedReader().readText().trim().toBoolean()
            val imageRes = BitmapFactory.decodeStream(conn)
            withContext(Dispatchers.Main){
                holder.image.setImageBitmap(imageRes)
                /*if (urlRecipes == true){
                    holder.imgLike.setImageResource(R.drawable.liked)
                } else {
                    holder.imgLike.setImageResource(R.drawable.like)
                }*/
            }
        }
        holder.itemView.setOnClickListener{
            click(res)
        }
    }

    override fun getItemCount(): Int {
        return data.length()
    }

}