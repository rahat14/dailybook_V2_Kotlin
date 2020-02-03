package com.metacodersbd.dailybook.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.metacodersbd.dailybook.R
import com.metacodersbd.dailybook.models.modelForTransaction
import com.metacodersbd.dailybook.viewHolderForTransaction
import kotlinx.android.synthetic.main.earning_fragment.*

class earnedFragment : Fragment() {

    private lateinit var database: DatabaseReference
    lateinit var recyclerView : RecyclerView
    lateinit var  pbar : ProgressBar
    var llm : LinearLayoutManager? =null
    var contextt : Context? = null
    var uid:String ?=null
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        val   view = inflater.inflate(R.layout.earning_fragment, container, false)

        contextt = this.context
        firebaseAuth  = FirebaseAuth.getInstance()
        uid = firebaseAuth.uid
        llm = LinearLayoutManager(contextt)
        llm?.reverseLayout= true
        llm?.stackFromEnd= true
        recyclerView =view.findViewById(R.id.depositList)
        pbar = view.findViewById(R.id.progressInDeposit)
        recyclerView.layoutManager = llm
        recyclerView.setHasFixedSize(true)

        pbar.visibility = View.VISIBLE
        //expenceList


        loadDataFromFirebase()


        return  view
    }
    private  fun loadDataFromFirebase()
    {
        database= FirebaseDatabase.getInstance().getReference("users")
            .child(uid!!)
            .child("depositDb")
        database.keepSynced(true)

        val options = FirebaseRecyclerOptions.Builder<modelForTransaction>()
            .setQuery(database, modelForTransaction::class.java)
            .setLifecycleOwner(this)
            .build()

        val adapters = object : FirebaseRecyclerAdapter<modelForTransaction, viewHolderForTransaction>(options){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int): viewHolderForTransaction {

                return viewHolderForTransaction(LayoutInflater.from(parent.context)
                    .inflate(R.layout.trans_row, parent, false))
            }

            override fun onBindViewHolder(
                holder: viewHolderForTransaction,
                position: Int,
                model: modelForTransaction
            ) {
                pbar.visibility = View.INVISIBLE
                holder.setData(model.amount.toString() , model.details.toString() , model.mon_view.toString()
                    , model.day_Name.toString() , model.time.toString() , model.date.toString())

//                var reason = getItem(position).details
//                var amount = getItem(position).amount

                // monName : String , day : String , time :String , date : String
                holder.itemView.setOnClickListener{
                    /// Toast.makeText(context ,reason + " " +amount +" "  , Toast.LENGTH_LONG).show() ;
                }



            }

        }

        recyclerView.layoutManager = llm



        adapters.startListening()
        recyclerView.adapter = adapters
    }

}