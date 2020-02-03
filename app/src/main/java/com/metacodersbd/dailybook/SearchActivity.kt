package com.metacodersbd.dailybook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.metacodersbd.dailybook.models.modelForTransaction
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.view.*

class SearchActivity : AppCompatActivity() {

    var searchCategory : String   = "details"
    var searchFilter : String   = "depositDb"
    lateinit var recyclerView : RecyclerView
    var llm : LinearLayoutManager? =null
    var uid : String  ? = null
    private  lateinit var  databaseReference: DatabaseReference
    private  lateinit var  firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.title = "Search Transaction"
        search_bar.onActionViewExpanded()
        firebaseAuth = FirebaseAuth.getInstance()
        uid = firebaseAuth.uid
        llm = LinearLayoutManager(applicationContext)

        recyclerView =findViewById(R.id.searchList)
        recyclerView.layoutManager = llm
        llm?.reverseLayout= true
        llm?.stackFromEnd= true
        recyclerView.setHasFixedSize(true)



        byDetail.setOnCheckedChangeListener{ buttonView, isChecked ->

            if(byDetail.isChecked)
            {
                byDate.isChecked = false
                byAmount.isChecked = false
                search_bar.queryHint = "Search By Details ....."
                searchCategory = "details"
                serachNow(searchFilter , searchCategory , search_bar.query.toString())
            }

        }
        byAmount.setOnCheckedChangeListener { buttonView, isChecked ->
            if(byAmount.isChecked){

                byDate.isChecked = false
                byDetail.isChecked = false
                searchCategory = "amount"
                search_bar.queryHint = "Search By Amount ....."
                serachNow(searchFilter , searchCategory , search_bar.query.toString())

            }

        }
        byDate.setOnCheckedChangeListener { buttonView, isChecked ->

            if(byDate.isChecked){
                byDetail.isChecked = false
                byAmount.isChecked = false
                search_bar.queryHint = "Search By Date Ex.. 3/6/2012"
                searchCategory = "full_date"
                serachNow(searchFilter , searchCategory , search_bar.query.toString())
              //  Toast.makeText(applicationContext , searchCategory ,Toast.LENGTH_LONG).show()

            }
        }

        // listening for filter

        byDeposit.setOnCheckedChangeListener { buttonView , isChecked->

            if(byDeposit.isChecked)
            {
                byExpense.isChecked = false
                searchFilter = "depositDb"
                serachNow(searchFilter , searchCategory , search_bar.query.toString())


            }
        }

        byExpense.setOnCheckedChangeListener{
                buttonView , isChecked->

            if(byExpense.isChecked)
            {
                byDeposit.isChecked = false
                searchFilter = "expenceDb"
                serachNow(searchFilter , searchCategory , search_bar.query.toString())

            }

        }
        search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                serachNow(searchFilter , searchCategory , query.toString())

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                serachNow(searchFilter , searchCategory , newText.toString())
                return true
            }

        }

        )


        serachNow(searchFilter , searchCategory , "")

    }

    private  fun serachNow(filter : String , searchCat: String , searchText :String){
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(uid!!).child(filter)
        databaseReference.keepSynced(true)
        Toast.makeText(applicationContext , "Searching In The " + filter + "Database"  + " For " + searchCat+" "  + searchText ,Toast.LENGTH_SHORT).show()

        var query : Query  = databaseReference.orderByChild(searchCat).startAt(searchText).endAt(searchText+ "\uf8ff")





            var options = FirebaseRecyclerOptions.Builder<modelForTransaction>()
                .setQuery(query, modelForTransaction::class.java)
                .setLifecycleOwner(this)
                .build()





        val adapters = object : FirebaseRecyclerAdapter<modelForTransaction, viewHolderForTransaction>(options){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int): viewHolderForTransaction {

                return viewHolderForTransaction(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.trans_row, parent, false))
            }

            override fun onBindViewHolder(
                holder: viewHolderForTransaction,
                position: Int,
                model: modelForTransaction
            ) {
                holder.setData(model.amount.toString() , model.details.toString() , model.mon_view.toString()
                    , model.day_Name.toString() , model.time.toString() , model.date.toString())

//


            }

        }

        recyclerView.layoutManager = llm



        adapters.startListening()
        recyclerView.adapter = adapters

    }



}
