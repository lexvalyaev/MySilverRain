package com.valyaev.lex.mysilverrain.model

class UrlObject(
    val text: String,
    val url: String
) {
    override fun toString(): String {
        return "$text     $url \n"
    }
}
