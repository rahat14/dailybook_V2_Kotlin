package com.metacodersbd.dailybook.models

import com.google.firebase.database.IgnoreExtraProperties


@IgnoreExtraProperties
    data class modelForTotal(
        var totalDeposit: String? = "",
        var totalLoanGiven: String? = "" ,
        var totalLoanTaken : String ? = "" ,
        var totalWithdraw : String ? = ""
    )




