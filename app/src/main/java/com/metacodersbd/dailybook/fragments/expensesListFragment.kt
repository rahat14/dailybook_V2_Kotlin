package com.metacodersbd.dailybook.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.metacodersbd.dailybook.R
import com.metacodersbd.dailybook.models.modelForTotal
import com.metacodersbd.dailybook.models.modelForTransaction
import com.metacodersbd.dailybook.viewHolderForTransaction
import kotlinx.android.synthetic.main.activity_dashboard_fragment.*
import kotlinx.android.synthetic.main.custom_view.*
import kotlinx.android.synthetic.main.expence_fragment.*
import kotlinx.android.synthetic.main.expence_fragment.view.*

class expensesListFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var profileref: DatabaseReference
    lateinit var recyclerView : RecyclerView
    var llm : LinearLayoutManager ? =null
    var contextt : Context ? = null
    var uid : String ? = null
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {


        val   view = inflater.inflate(R.layout.expence_fragment, container, false)
        contextt = this.context
        firebaseAuth  = FirebaseAuth.getInstance()
        uid  = firebaseAuth.uid
        database= FirebaseDatabase.getInstance().getReference("users")
            .child(uid!!)
            .child("expenceDb")
        database.keepSynced(true)
        llm = LinearLayoutManager(contextt)

        recyclerView =view.findViewById(R.id.expenceList)
        recyclerView.layoutManager = llm
        llm?.reverseLayout= true
        llm?.stackFromEnd= true
        recyclerView.setHasFixedSize(true)


        //expenceList


        loadDataFromFirebase()

        return  view
    }

    private  fun loadDataFromFirebase()
    {

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