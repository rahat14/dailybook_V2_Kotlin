package com.metacodersbd.dailybook.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.metacodersbd.dailybook.R
import com.metacodersbd.dailybook.adapter.viewPagerAdapter
import com.metacodersbd.dailybook.models.modelForTotal
import kotlinx.android.synthetic.main.activity_dashboard_fragment.*
import kotlinx.android.synthetic.main.activity_dashboard_fragment.view.*
import java.text.DecimalFormat

class  dashboardFragment : Fragment()
{
    private  lateinit var  profileref: DatabaseReference
    var uid : String?= "Test ID"
    var testString : Double?= null
    var totalDeposit : Double ?= null
    var depositInDouble  : Double?= null
    val dec = DecimalFormat("#,###.##")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        val   view = inflater.inflate(R.layout.activity_dashboard_fragment, container, false)

        //set up the viewpager and load them
        val adapter  = viewPagerAdapter(this.childFragmentManager)
        adapter.addFrament(expensesListFragment() , "Expenses")
        adapter.addFrament(earnedFragment() , "Earning")

        view.viewPagerID.adapter = adapter
        view.TablaoutId.setupWithViewPager(view.viewPagerID)



       return  view
    }

    override fun onStart() {
        super.onStart()
        downloadTheDataOfTotal()
    }

    private fun downloadTheDataOfTotal() {
        profileref = FirebaseDatabase.getInstance().getReference("users").child(uid!!).child("total")
        profileref.keepSynced(true)
        // download the data
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(modelForTotal::class.java)
                // ...
                testString =   post?.totalDeposit?.toDouble()
                depositTv.text = "$" + dec.format(testString).toString()
                withdrawTv.text ="$" +dec.format(post?.totalWithdraw?.toDouble()).toString()
                depositInDouble = post?.totalDeposit?.toDouble()?.minus(post.totalWithdraw?.toDouble()!!)
                profitId.text = "$" +depositInDouble.toString()
                totalDeposit = post?.totalDeposit?.toDouble()
                loanTv.text ="$" + dec.format(post?.totalLoanTaken?.toDouble()).toString()



            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Toast.makeText(context, databaseError.message, Toast.LENGTH_SHORT)
                    .show()
                // ...
            }


        }

        profileref.addValueEventListener(postListener)


    }

    fun getTotalDepositValue(): Double? {


        return totalDeposit
    }


}



