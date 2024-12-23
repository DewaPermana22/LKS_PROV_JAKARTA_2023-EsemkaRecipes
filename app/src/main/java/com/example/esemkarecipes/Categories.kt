package com.example.esemkarecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL


class Categories : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        fetch(view)
        return view
    }

    fun fetch(view: View){
        CoroutineScope(Dispatchers.IO).launch{
            val conn = URL("http://10.0.2.2:5000/api/categories").openStream().bufferedReader().readText()
            val data = JSONArray(conn)
            withContext(Dispatchers.Main){
                val rc = view?.findViewById<RecyclerView>(R.id.rc_categories)
                rc?.layoutManager = GridLayoutManager(context, 2)
                rc?.adapter = adapter_categori(data) { item ->
                    val intent = Intent(requireContext(), RecipesActivity::class.java)
                    intent.putExtra("id", item.getInt("id"))
                    intent.putExtra("name", item.getString("name"))
                    intent.putExtra("icon", item.getString("icon"))
                    startActivity(intent)
                }
            }
        }
    }
}