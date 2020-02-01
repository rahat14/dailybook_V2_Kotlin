package com.metacodersbd.dailybook


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.metacodersbd.dailybook.databinding.ActivityMainBinding
import com.metacodersbd.dailybook.fragments.dashboardFragment
import com.metacodersbd.dailybook.fragments.storeFragment
import com.metacodersbd.dailybook.models.modelForTotal
import kotlinx.android.synthetic.main.activity_dashboard_fragment.*
import kotlinx.android.synthetic.main.custom_view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private  lateinit var  profileref: DatabaseReference
    var fragment: Fragment? = null
    private  lateinit var  mainActivitybinder: ActivityMainBinding
    private  lateinit var  mref:DatabaseReference
    var totalDeposit : Double ?= null
    var falg  :String? =  null
    var details   :String? =  null
    var amount   :String? =  null
    var uid : String?= ""
    var dialog : MaterialDialog ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivitybinder = DataBindingUtil.setContentView(this ,R.layout.activity_main)
            // TODO uid need to init
            uid =FirebaseAuth.getInstance().uid


            fragment = dashboardFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.viewPagerContainer, dashboardFragment(), "dashboard")
            transaction.commit()



        mainActivitybinder.dashboardButton.setOnClickListener {
                val transactionn = supportFragmentManager.beginTransaction()
                transactionn.replace(R.id.viewPagerContainer, dashboardFragment(), "dashboard")

                transactionn.commit()


        }
        mainActivitybinder.addBtn.setOnClickListener{



        showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))

        }


        mainActivitybinder.loanButton.setOnClickListener{

//            val transactionb  = supportFragmentManager.beginTransaction()
//            transactionb.replace(R.id.viewPagerContainer ,storeFragment()  , "store")
//
//            transactionb.commit()

            val intent = Intent(applicationContext , SearchActivity::class.java)
            startActivity(intent)



        }





    }


    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {

         dialog = MaterialDialog(this, dialogBehavior).show {
            title(R.string.diaogechooser)
            customView(R.layout.custom_view, scrollable = true, horizontalPadding = true
            )
            cornerRadius(16f)
            setPeekHeight(res = R.dimen.my_default_peek_height)

          /*
            positiveButton(R.string.app_name) { dialog ->
                // Pull the password out of the custom view when the positive button is pressed
                val passwordInput: EditText = dialog.getCustomView()
                    .findViewById(R.id.password)
            }
            */
          //  negativeButton(android.R.string.cancel)
          //  lifecycleOwner(this@MainActivity)
         //   debugMode(debugMode)
        }

        dialog!!.progressBarDialouge.visibility = View.GONE
        // Setup custom view content
        val customview = dialog!!.getCustomView()

        dialog!!.submitPanel.visibility = View.GONE

        dialog!!.depositButton.setOnClickListener{
            falg = "depositDb"
            dialog!!.title(R.string.dialogueDetails)
            dialog!!.setPeekHeight(res = R.dimen.peekRateWithEditText)
             dialog!!.buttonPanel.visibility = View.GONE
            dialog!!.submitPanel.visibility = View.VISIBLE

        }
        dialog!!.expensesButton.setOnClickListener{
            falg = "expenceDb"
            dialog!!.title(R.string.dialogueDetails)
            dialog!!.setPeekHeight(res = R.dimen.peekRateWithEditText)
            dialog!!.buttonPanel.visibility = View.GONE
            dialog!!.submitPanel.visibility = View.VISIBLE

        }


        dialog!!.submitBtn.setOnClickListener {

            details = dialog!!.deatails.text?.toString()
            amount = dialog!!.amount.text?.toString()

            if(TextUtils.isEmpty(details)|| TextUtils.isEmpty(amount))
            {
                Toast.makeText(this , "Please Insert Value" ,Toast.LENGTH_SHORT)
                    .show()

            }
            else {

                // send data to firebase
                writingDataToServer(details , amount , falg)

            }


        }
        /*
        val customView = dialog.getCustomView()
        val passwordInput: EditText = customView.findViewById(R.id.password)
        val showPasswordCheck: CheckBox = customView.findViewById(R.id.showPassword)
        showPasswordCheck.setOnCheckedChangeListener { _, isChecked ->
            passwordInput.inputType =
                if (!isChecked) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT
            passwordInput.transformationMethod =
                if (!isChecked) PasswordTransformationMethod.getInstance() else null
        }

         */
    }

    private fun writingDataToServer(details: String?, amount: String?, falg: String?) {
                var key:String ? = null
        // changing the  title

        dialog!!.progressBarDialouge.visibility = View.VISIBLE
                mref = FirebaseDatabase.getInstance().getReference("users").child(uid!!).child(falg!!)
                // get the key
                key = mref.push().key
                 val hashMap : HashMap<String , String>
                = HashMap<String , String> ()
        // getting time now
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val date = SimpleDateFormat("dd")
        val time = SimpleDateFormat("hh:mm a")
        val mon = SimpleDateFormat("MMM")
        val dayNameFormat = SimpleDateFormat("EEE")
      // Getting the value from the date formats
        val dateVIew = date.format(Date())
        val timeView = time.format(Date())
        val monView = mon.format(Date())
        val  dayName = dayNameFormat.format(Date())
        val currentDate = sdf.format(Date())


        hashMap.put("amount" , amount!!)
        hashMap.put("details", details!!)
        hashMap.put("full_date" , currentDate)
        hashMap.put("time" , timeView)
        hashMap.put("date" , dateVIew)
        hashMap.put("day_Name",dayName)
        hashMap.put("mon_view" , monView)

            // call for the server to know the values  Text View

       if(falg.equals("depositDb")){
           profileref = FirebaseDatabase.getInstance().getReference("users").child(uid!!).child("total")
           // download the data
           val postListener = object : ValueEventListener {
               override fun onDataChange(dataSnapshot: DataSnapshot) {
                   // Get Post object and use the values to update the UI
                   val post = dataSnapshot.getValue(modelForTotal::class.java)
                   // get the total deposit 1st
                   totalDeposit =(post?.totalDeposit.toString()).toDouble()

                   // add them with current deposit
                   totalDeposit = amount.toDouble()+ totalDeposit!!

                   // upload the list
                   mref.child(key!!).setValue(hashMap)
                       .addOnCompleteListener{

                           // updated  the total value
                           profileref.child("totalDeposit").setValue(totalDeposit.toString())
                               .addOnCompleteListener{

                                   Toast.makeText(applicationContext , "Uploaded The Amount!!" ,Toast.LENGTH_SHORT)
                                       .show()
                                   // hiding the spinner
                                   dialog!!.progressBarDialouge.visibility = View.GONE
                                   // canceling the dialogue
                                   dialog!!.cancel()

                               }

                       }.addOnFailureListener {
                           dialog!!.title(R.string.dialogueDetails)
                           dialog!!.progressBarDialouge.visibility = View.GONE
                           Toast.makeText(applicationContext , "Error: $it",Toast.LENGTH_SHORT)
                               .show()

                       }







               }

               override fun onCancelled(databaseError: DatabaseError) {
                   // Getting Post failed, log a message
                   Toast.makeText(applicationContext, databaseError.message, Toast.LENGTH_SHORT)
                       .show()
                   // ...
               }


           }

           profileref.addListenerForSingleValueEvent(postListener)







       }
        else {
           // if flag contains  expense db

           profileref = FirebaseDatabase.getInstance().getReference("users").child(uid!!).child("total")
           // download the data
           val postListener = object : ValueEventListener {
               override fun onDataChange(dataSnapshot: DataSnapshot) {
                   // Get Post object and use the values to update the UI
                   val post = dataSnapshot.getValue(modelForTotal::class.java)
                   // get the total deposit 1st
                   totalDeposit =(post?.totalWithdraw.toString()).toDouble()

                   // add them with current deposit
                   totalDeposit = amount.toDouble()+ totalDeposit!!

                   // upload the list
                   mref.child(key!!).setValue(hashMap)
                       .addOnCompleteListener{

                           // updated  the total value
                           profileref.child("totalWithdraw").setValue(totalDeposit.toString())
                               .addOnCompleteListener{

                                   Toast.makeText(applicationContext , "Uploaded The Amount!!" ,Toast.LENGTH_SHORT)
                                       .show()
                                   // hiding the spinner
                                   dialog!!.progressBarDialouge.visibility = View.GONE
                                   // canceling the dialogue
                                   dialog!!.cancel()

                               }

                       }.addOnFailureListener {
                           dialog!!.title(R.string.dialogueDetails)
                           dialog!!.progressBarDialouge.visibility = View.GONE
                           Toast.makeText(applicationContext , "Error: $it",Toast.LENGTH_SHORT)
                               .show()

                       }







               }

               override fun onCancelled(databaseError: DatabaseError) {
                   // Getting Post failed, log a message
                   Toast.makeText(applicationContext, databaseError.message, Toast.LENGTH_SHORT)
                       .show()
                   // ...
               }


           }

           profileref.addListenerForSingleValueEvent(postListener)




       }




    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//        val builder = AlertDialog.Builder(this@MainActivity)
//
//        builder.setTitle("Test")
//
//        // Finally, make the alert dialog using builder
//        val dialog: AlertDialog = builder.create()
//
//        // Display the alert dialog on app interface
//        dialog.show()
//    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {

        val builder = AlertDialog.Builder(this@MainActivity)

        builder.setTitle("Alert!")
        builder.setMessage("Do You Want Exit This APP ? ")
        builder.setPositiveButton("Yes"){
            dialog, which ->  finish()
        }
        builder.setNegativeButton("No"){
            dialog, which ->
            dialog.dismiss()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
        dialog.setCancelable(false)
        return true
    }
}
