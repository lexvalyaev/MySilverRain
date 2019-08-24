package com.valyaev.lex.mysilverrain.controller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.valyaev.lex.mysilverrain.R
import com.valyaev.lex.mysilverrain.model.ProgUrlObject

class ProgAdapter(private val listProgUrlObject: ArrayList<ProgUrlObject>): RecyclerView.Adapter<ProgAdapter.ViewHolder>() {

    override fun getItemCount()=listProgUrlObject.size

    fun setItems(items:ArrayList<ProgUrlObject>)
    {
        listProgUrlObject.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.prog_rview,parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView?.text = listProgUrlObject[position].text
        holder.textView2?.text = listProgUrlObject[position].url
    }

   inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
       var textView: TextView? = null
       var textView2: TextView? = null

       init {
            textView=itemView.findViewById(R.id.textView)
            textView2=itemView.findViewById(R.id.textView2)

       }
    }
}