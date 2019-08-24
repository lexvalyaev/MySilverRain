package com.valyaev.lex.mysilverrain


import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.valyaev.lex.mysilverrain.controller.ProgAdapter
import com.valyaev.lex.mysilverrain.model.ProgUrlObject
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ProgrammActivity : AppCompatActivity() {
    val EXTRA_URL= "URL"
    val EXTRA_NAME= "NAME"
    val programmsList = arrayListOf<ProgUrlObject>()
    val progAdapter = ProgAdapter(programmsList)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_programm)
        val intentMain = intent
        val urlProgramm = intentMain.extras.getString(EXTRA_URL)
        val nameProgramm = intentMain.getStringExtra(EXTRA_NAME)

        val recyclerView : RecyclerView = findViewById(R.id.prog_recycle)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter=progAdapter

        doAsync{
            getProgList(urlProgramm,programmsList)
        }.execute()



    }

    inner class doAsync(val handler: () -> Unit) : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            handler()
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            progAdapter.setItems(programmsList)
        }
    }

    //формируем список выпусков выбранной программы в programmsList
    fun getProgList(url:String, pList:ArrayList<ProgUrlObject>) {
        val defIcon = ContextCompat.getDrawable(this@ProgrammActivity, R.drawable.ic_default)
        var hasNextPage: Boolean
        val regex = Regex(pattern = """\d+""")
        var nextUrl: String
        nextUrl = url

        val locale = Locale("ru")
        val sdf = SimpleDateFormat("dd MMMM yyyy",locale)
        do {
            hasNextPage = false
            val doc = Jsoup.connect(nextUrl).get()
            val elements = doc.select("div.blog").select("a[href]")
            for (e in elements) {

                if (e.text() == "Загрузить еще") {
                    hasNextPage = true
                    nextUrl = "http://www.silver.ru" + e.attr("href")

                }



                if (e.text() != "Загрузить еще" && e.text() != "" && !(regex.matches(e.text()))) {
                    val docHTML = Jsoup.connect("http://www.silver.ru" + e.attr("href")).get()
                    val elementBlog = docHTML.selectFirst("div.blog-detail")
                    val mp3url = elementBlog.getElementsByTag("audio").attr("src")
                    val stringData = elementBlog.getElementsByTag("span").last().text()
                    val date = sdf.parse(stringData)



                    pList.add(
                        ProgUrlObject(
                            text = e.text(),
                            url = e.attr("href"),
                            icon = defIcon,
                            mp3 = mp3url,
                            date = date
                        )
                    )

                }

            }
        }while (hasNextPage)
    }
}

