package com.metacodersbd.dailybook

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class viewHolderForTransaction (itemView: View ): RecyclerView.ViewHolder(itemView){


    fun setData( title : String , amount: String , monname : String , day : String , timee:String , datee : String ){
        val txtName = itemView.findViewById<TextView>(R.id.amountTv)
        val txtTitle = itemView.findViewById<TextView>(R.id.reasonTv)
        val monName  = itemView.findViewById<TextView>(R.id.monthNameTv)
        val dayname = itemView.findViewById<TextView>(R.id.dayNameTv)
        val time  = itemView.findViewById<TextView>(R.id.timeTv)
        val date  = itemView.findViewById<TextView>(R.id.dateTv)


        txtName.text ="$" +title
        txtTitle.text =  amount
        monName.text = monname
        dayname.text = day
        time.text = timee
        date.text = datee

    }







}


