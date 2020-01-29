package com.metacodersbd.dailybook

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class viewHolderForTransaction (itemView: View ): RecyclerView.ViewHolder(itemView){


    fun setData( title : String , amount: String ){
        val txtName = itemView.findViewById<TextView>(R.id.amountTv)
        val txtTitle = itemView.findViewById<TextView>(R.id.reasonTv)

        txtName.text = title
        txtTitle.text = amount

    }







}


