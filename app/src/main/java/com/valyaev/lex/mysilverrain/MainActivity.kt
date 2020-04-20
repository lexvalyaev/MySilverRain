package com.valyaev.lex.mysilverrain


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.facebook.stetho.Stetho
import com.valyaev.lex.mysilverrain.controller.Adapter
import com.valyaev.lex.mysilverrain.helper.DbHelper
import com.valyaev.lex.mysilverrain.model.UrlObject
import com.valyaev.lex.mysilverrain.room.ProgDB
import kotlinx.android.synthetic.main.activity_programm.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    val EXTRA_URL = "URL"
    val EXTRA_NAME = "NAME"
    val EXTRA_ICON = "ICON"
    val baseUrlObjectArray = arrayListOf<UrlObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(getApplicationContext())
        val assetInputStream = assets.open("urls.txt")
        val br = BufferedReader(InputStreamReader(assetInputStream))
        //val dbHelper = DbHelper(applicationContext)
        //dbHelper.readableDatabase


        val db = Room.databaseBuilder(
            applicationContext,
            ProgDB::class.java, "silver.db"
        ).createFromAsset("databases/silver.db").build()

        val listProg = db.progDAO().getAllPrograms()
        for (prog in listProg) {
            if (prog.icon_id == null)
                prog.icon_id = resources.getIdentifier(
                    prog.icon_name,
                    "drawable",
                    applicationContext.packageName
                )
            db.progDAO().updateProg(prog)
        }

        /*for (prog in listProg) {
            println("""${prog.program_name} ${prog._id}""")
        }
        while (true)
        {
          var word = br.readLine()
            if(word!=null)
            {
                val strings = word.split(";")
                val imgID = resources.getIdentifier(strings[2],"drawable",applicationContext.packageName)
                val urlObject = UrlObject(strings[0],strings[1],imgID)
                dbHelper.insert(urlObject)

            }
            else break

        }*/


        /*baseUrlObjectArray.clear()

        baseUrlObjectArray.addAll(dbHelper.getAllPrograms())
        print(baseUrlObjectArray)

        val recyclerView: androidx.recyclerview.widget.RecyclerView =
            findViewById(R.id.firstRecycle)
        recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)

        val onProgrammClickListener = object : Adapter.OnProgrammClickListener {
            override fun onClick(urlObject: UrlObject) {
                //Toast.makeText(applicationContext , urlObject.url,Toast.LENGTH_LONG).show()
                val intentProgActivity = Intent(this@MainActivity, ProgrammActivity::class.java)
                intentProgActivity.putExtra(EXTRA_URL, urlObject.url)
                    .putExtra(EXTRA_NAME, urlObject.text).putExtra(EXTRA_ICON, urlObject.iconID)
                startActivity(intentProgActivity)
            }
        }

        recyclerView.adapter = Adapter(baseUrlObjectArray, onProgrammClickListener)
        val itemDecoration = androidx.recyclerview.widget.DividerItemDecoration(
            this,
            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
        )
        recyclerView.addItemDecoration(itemDecoration)*/


    }

}



