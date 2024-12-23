package com.example.esemkarecipes

import android.graphics.BitmapFactory
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

class adapter_categori (val data : JSONArray, val OnsetClick : (JSONObject) -> Unit) : RecyclerView.Adapter<adapter_categori.categori_holder> () {

    class categori_holder(view: View) : RecyclerView.ViewHolder(view.rootView){
        val image = view.findViewById<ImageView>(R.id.gambar_kategori)
        val nama = view.findViewById<TextView>(R.id.nama_kategori)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapter_categori.categori_holder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_categories, parent, false)
        return categori_holder(view)
    }

    override fun onBindViewHolder(holder: adapter_categori.categori_holder, position: Int) {
        val dataJson : JSONObject = data.getJSONObject(position)
        holder.nama.text = dataJson.getString("name")
        val url_image = dataJson.getString("icon")
        CoroutineScope(Dispatchers.IO).launch {
        val fetch_icon = URL("http://10.0.2.2:5000/images/categories/$url_image").openStream()
        val images_fix = BitmapFactory.decodeStream(fetch_icon)
            withContext(Dispatchers.Main){
             holder.image.setImageBitmap(images_fix)
            }
        }
        holder.itemView.setOnClickListener{
            OnsetClick(dataJson)
        }
    }

    override fun getItemCount(): Int {
        return data.length()
    }
}