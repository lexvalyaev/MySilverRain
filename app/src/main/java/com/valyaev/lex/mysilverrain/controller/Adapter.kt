package com.valyaev.lex.mysilverrain.controller

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.valyaev.lex.mysilverrain.R
import com.valyaev.lex.mysilverrain.model.UrlObject

class Adapter(private val values: List<UrlObject>, private val onClickListener: OnProgrammClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<Adapter.ViewHolder>() {



    interface OnProgrammClickListener{
        fun onClick(urlObject: UrlObject)
    }

    override fun getItemCount() = values.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.first_recycle_view, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView?.text = values[position].text
        val image = holder.imgView?.context?.let { ContextCompat.getDrawable(it, values[position].iconID) }
        holder.imgView?.setImageDrawable(image)
       }


    inner class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        var imgView: ImageView? = null


        init {
            textView = itemView.findViewById(R.id.name_text_view)
            imgView = itemView.findViewById(R.id.imageView)
            itemView.setOnClickListener {
                val pos = layoutPosition
                val urlObj = values.get(pos)
                onClickListener.onClick(urlObj)
            }
        }


    }
}