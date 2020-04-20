package com.valyaev.lex.mysilverrain.model

import android.graphics.drawable.Drawable



open class UrlObject (
    val text: String,
    val url: String,
    val iconName: String,
    val iconID : Int = 0
    //val icon: Drawable?
)
{
    override fun toString(): String {
        return "$text     $url \n"
    }
}
