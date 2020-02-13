package com.valyaev.lex.mysilverrain.helper

import android.content.Context
import android.os.AsyncTask
import java.io.BufferedInputStream
import java.net.URL

class DownloadAudioFromUrl (val context: Context):AsyncTask<String,String,String>(){

    override fun doInBackground(vararg params: String): String {
        val urlString = "www.silver.ru" + params[0]
        val url = URL(urlString)
        val connection = url.openConnection()
        connection.connect()
        val inputStream = BufferedInputStream(url.openStream())
        val fileName = params[0]?.replace("/upload/medialibrary/","")
        val outputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE)
        val data = ByteArray(1024)
        var count = inputStream.read(data)
        var total = count
        while (count != -1) {
            outputStream.write(data, 0, count)
            count = inputStream.read(data)
            total += count
        }
        outputStream.flush()
        outputStream.close()
        inputStream.close()
        val dbHelper = DbHelper(context)
        dbHelper.insertMp3InternalStorage(params[0],fileName)
        println("finished saving audio.mp3 to internal storage")
        return "Success"
    }



}