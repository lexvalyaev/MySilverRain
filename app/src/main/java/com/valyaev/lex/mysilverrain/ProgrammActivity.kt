package com.valyaev.lex.mysilverrain


import android.graphics.drawable.Drawable
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.valyaev.lex.mysilverrain.controller.ProgAdapter
import com.valyaev.lex.mysilverrain.helper.DbHelper
import com.valyaev.lex.mysilverrain.model.ProgUrlObject
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgrammActivity : AppCompatActivity() {
    val EXTRA_URL= "URL"
    val EXTRA_NAME= "NAME"
    val EXTRA_ICON= "ICON"
    val programmsList = arrayListOf<ProgUrlObject>()

    var urlProgramm = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programm)
        val intentMain = intent
        urlProgramm = intentMain.extras.getString(EXTRA_URL)
        val nameProgramm = intentMain.getStringExtra(EXTRA_NAME)
        val imgID = intentMain.extras.getInt(EXTRA_ICON)

        val recyclerView : androidx.recyclerview.widget.RecyclerView = findViewById(R.id.prog_recycle)


        val onDowloadClickListener = object : ProgAdapter.OnDownloadClickListener{
            override fun onClick(progUrlObject: ProgUrlObject) {
                val text = "clik on dowload"
                Toast.makeText(applicationContext, text,Toast.LENGTH_SHORT)
            }

        }

        val progAdapter = ProgAdapter(programmsList,onDowloadClickListener)

        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.adapter=progAdapter

        val itemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
            this,
            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(itemDecoration)

        doAsync{
            getProgList(urlProgramm,programmsList,imgID)

        }.execute()
        progAdapter.setItems(programmsList)
    }

    inner class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
        }


    }


    //формируем список выпусков выбранной программы в programmsList
    fun getProgList(url:String, pList:ArrayList<ProgUrlObject>, imgID: Int) {

        var defIcon = ContextCompat.getDrawable(this,imgID)
        if (defIcon == null) {
            val defIcon = ContextCompat.getDrawable(this@ProgrammActivity, R.drawable.ic_default)
            }

        var hasNextPage: Boolean
        val regex = Regex(pattern = """\d+""")
        var nextUrl: String
        nextUrl = url
        var count = 0

        val locale = Locale("ru")
        val sdf = SimpleDateFormat("dd MMMM yyyy",locale)
        do {
            hasNextPage = false
            val doc = Jsoup.connect(nextUrl).get()
            val elements = doc.select("div.blog").select("a[href]")
            for (e in elements) {

                if (e.text() == "Загрузить еще") {
                    hasNextPage = true
                    nextUrl = "https://www.silver.ru" + e.attr("href")

                }
                if (e.text() != "Загрузить еще" && e.text() != "" && !(regex.matches(e.text()))) {
                    val docHTML = Jsoup.connect("https://www.silver.ru" + e.attr("href")).get()
                    val elementBlog = docHTML.selectFirst("div.blog-detail")
                    val mp3url = elementBlog.getElementsByTag("audio").attr("src")
                    val stringData = elementBlog.getElementsByTag("span").last().text()
                    val date = sdf.parse(stringData)
                    val progUrlObject = ProgUrlObject(
                        text = e.text(),
                        programs_url = urlProgramm,
                        url = e.attr("href"),
                        mp3 = mp3url,
                        date = date,
                        imgID = imgID
                    )

                    val dbHelper = DbHelper(applicationContext)
                    dbHelper.insert(progUrlObject)
                    pList.add(progUrlObject)
                    count++
                    Log.d("GETURL", count.toString())
                    if(count >10) break
                }

            }

        }while (hasNextPage )
    }
}

