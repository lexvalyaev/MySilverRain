package com.valyaev.lex.mysilverrain

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.valyaev.lex.mysilverrain.model.UrlObject
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    val baseUrlObjectArray = arrayListOf<UrlObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val assetInputStream = assets.open("urls.txt")
        val br = BufferedReader(InputStreamReader(assetInputStream))


        while (true)
        {
          var word = br.readLine()
            if(word!=null)
            {
                val strings = word.split(";")
                baseUrlObjectArray.add(UrlObject(strings[0],strings[1]))
            }
            else break

        }

        val recyclerView :RecyclerView = findViewById(R.id.firstRecycle)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter= Adapter(baseUrlObjectArray)

    }

}



class Adapter(private val values: List<UrlObject>):RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun getItemCount() =  values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent?.context).inflate(R.layout.first_recycle_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.textView?.text = values[position].text
        Log.d("RecycleView",values[position].text)
        holder?.textView2?.text = values[position].url
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        var textView2: TextView?=null

        init {
            textView = itemView.findViewById(R.id.textView)
            textView2 = itemView.findViewById(R.id.textView2)
        }

    }
}
