package com.metacodersbd.dailybook.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class modelForTransaction (
    var amount: String? = "",
    var details: String? = "" ,
    var full_date : String ? = "" ,
    var mon_view : String ? = "",
    var time: String? = "",
    var date: String? = "" ,
    var day_Name : String? = ""
//
//            hashMap.put("amount" , amount!!)
//hashMap.put("details", details!!)
//hashMap.put("full_date" , currentDate)
//hashMap.put("mon_view" , monView)
//hashMap.put("time" , timeView)
//hashMap.put("date" , dateVIew)
//hashMap.put("day_Name",dayName)
)