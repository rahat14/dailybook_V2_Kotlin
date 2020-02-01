package com.metacodersbd.dailybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.view.*

class SearchActivity : AppCompatActivity() {

    var searchCategory : String  ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        byDetail.setOnCheckedChangeListener{ buttonView, isChecked ->

            if(byDetail.isChecked)
            {
                byDate.isChecked = false
                byAmount.isChecked = false

                searchCategory = "details"
                Toast.makeText(applicationContext , searchCategory ,Toast.LENGTH_LONG).show()
            }

        }
        byAmount.setOnCheckedChangeListener { buttonView, isChecked ->
            if(byAmount.isChecked){

                byDate.isChecked = false
                byDetail.isChecked = false
                searchCategory = "amount"
                Toast.makeText(applicationContext , searchCategory ,Toast.LENGTH_LONG).show()

            }

        }
        byDate.setOnCheckedChangeListener { buttonView, isChecked ->

            if(byDate.isChecked){
                byDetail.isChecked = false
                byAmount.isChecked = false

                searchCategory = "full_date"
                Toast.makeText(applicationContext , searchCategory ,Toast.LENGTH_LONG).show()

            }



        }




    }
}
