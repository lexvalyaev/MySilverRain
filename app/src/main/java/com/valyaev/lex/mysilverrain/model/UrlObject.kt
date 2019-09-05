package com.valyaev.lex.mysilverrain.model

import android.graphics.drawable.Drawable



open class UrlObject (
    val text: String,
    val url: String,
    val iconID :Int
    //val icon: Drawable?
)
{
    override fun toString(): String {
        return "$text     $url \n"
    }
}
