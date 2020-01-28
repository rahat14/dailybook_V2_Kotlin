package com.metacodersbd.dailybook


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.metacodersbd.dailybook.databinding.ActivityMainBinding
import com.metacodersbd.dailybook.fragments.dashboardFragment
import com.metacodersbd.dailybook.fragments.storeFragment
import kotlinx.android.synthetic.main.custom_view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {

    var fragment: Fragment? = null
    private  lateinit var  mainActivitybinder: ActivityMainBinding
    private  lateinit var  mref:DatabaseReference

    var falg  :String? =  null
    var details   :String? =  null
    var amount   :String? =  null
    var uid : String?= "Test ID"
    var dialog : MaterialDialog ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainActivitybinder = DataBindingUtil.setContentView(this ,R.layout.activity_main)
            // TODO uid need to init



            fragment = dashboardFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.viewPagerContainer, dashboardFragment(), "dashboard")
            transaction.addToBackStack(null)
            transaction.commit()



        mainActivitybinder.dashboardButton.setOnClickListener {
                val transactionn = supportFragmentManager.beginTransaction()
                transactionn.replace(R.id.viewPagerContainer, dashboardFragment(), "dashboard")
                transactionn.addToBackStack(null)
                transactionn.commit()


        }
        mainActivitybinder.addBtn.setOnClickListener{



        showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))

        }


        mainActivitybinder.loanButton.setOnClickListener{

            val transactionb  = supportFragmentManager.beginTransaction()
            transactionb.replace(R.id.viewPagerContainer ,storeFragment()  , "store")
            transactionb.addToBackStack(null)
            transactionb.commit()



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
        val sdf = SimpleDateFormat("hh:mm dd/M/yyyy")
        val currentDate = sdf.format(Date())


        hashMap.put("amount" , amount!!)
        hashMap.put("details", details!!)
        hashMap.put("date" , currentDate)


        mref.child(key!!).setValue(hashMap)
            .addOnCompleteListener{
            Toast.makeText(this , "Uploaded The Amount" ,Toast.LENGTH_SHORT)
                .show()
                // hiding the spinner
                dialog!!.progressBarDialouge.visibility = View.GONE
                // canceling the dialogue
                dialog!!.cancel()
          }.addOnFailureListener {
                dialog!!.title(R.string.dialogueDetails)
                dialog!!.progressBarDialouge.visibility = View.GONE
            Toast.makeText(this , "Error: $it",Toast.LENGTH_SHORT)
            .show() }




    }




}
