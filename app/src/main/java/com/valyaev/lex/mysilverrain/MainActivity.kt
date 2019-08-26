package com.valyaev.lex.mysilverrain


import android.content.Intent
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.valyaev.lex.mysilverrain.controller.Adapter
import com.valyaev.lex.mysilverrain.model.UrlObject
import kotlinx.android.synthetic.main.first_recycle_view.view.*
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {
    val EXTRA_URL= "URL"
    val EXTRA_NAME= "NAME"
    val EXTRA_ICON= "ICON"
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
                val imgID = resources.getIdentifier(strings[2],"drawable",applicationContext.packageName)
                val image = ContextCompat.getDrawable(this,imgID)

                baseUrlObjectArray.add(UrlObject(strings[0],strings[1],imgID,image))
            }
            else break

        }


        val recyclerView :RecyclerView = findViewById(R.id.firstRecycle)
            recyclerView.layoutManager = LinearLayoutManager(this)

        val onProgrammClickListener = object : Adapter.OnProgrammClickListener {
            override fun onClick(urlObject: UrlObject) {
                    //Toast.makeText(applicationContext , urlObject.url,Toast.LENGTH_LONG).show()
                    val intentProgActivity = Intent(this@MainActivity,ProgrammActivity::class.java)
                intentProgActivity.putExtra(EXTRA_URL,urlObject.url).putExtra(EXTRA_NAME,urlObject.text).putExtra(EXTRA_ICON,urlObject.iconID)
                startActivity(intentProgActivity)
            }
        }

            recyclerView.adapter= Adapter(baseUrlObjectArray,onProgrammClickListener)
        val itemDecoration = DividerItemDecoration(this,DividerItemDecoration.VERTICAL)
            recyclerView.addItemDecoration(itemDecoration)


    }

}



