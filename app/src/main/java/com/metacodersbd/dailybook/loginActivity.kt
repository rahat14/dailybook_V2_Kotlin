package com.metacodersbd.dailybook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Toast
import com.crashlytics.android.Crashlytics

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

import com.google.firebase.database.*
import com.metacodersbd.dailybook.models.modelForTotal
import kotlinx.android.synthetic.main.activity_login.*

class loginActivity : AppCompatActivity() {
    val RC_SIGN_IN: Int = 1
    var uid: String ?= null
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_login)
        firebaseAuth = FirebaseAuth.getInstance()

        setupUI()
        configureGoogleSignIn()
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("220240007199-lsj8p6n4ojk3dkt0q0r4f3rlq99vou36.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
       // Crashlytics.getInstance().crash()
        google_button.setOnClickListener {

            signIn()
            progressBar?.visibility = View.VISIBLE
        }
    }


    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                }
                else {
                    progressBar?.visibility = View.INVISIBLE
                    Toast.makeText(this, "Google sign in Acc null:(", Toast.LENGTH_LONG).show()
                }
            } catch (e: ApiException) {
                progressBar?.visibility = View.INVISIBLE
                Toast.makeText(this, "Google sign in failed:(" + e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
               // progressBar?.visibility = View.INVISIBLE
              // val intent = Intent(applicationContext , MainActivity::class.java)
                //startActivity(intent)
                checkifUserExits()

            } else {
                progressBar?.visibility = View.INVISIBLE
                Toast.makeText(this, "Google sign in failed:(" + it.exception.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkifUserExits() {
        uid = firebaseAuth.uid


        var mref : DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
            .child(uid!!)

       var listener  = object  : ValueEventListener {
           override fun onCancelled(p0: DatabaseError) {


                  }

           override fun onDataChange(dataSnapshot: DataSnapshot) {

               if(dataSnapshot.exists())
               {
                   progressBar?.visibility  = View.INVISIBLE
                    val intent = Intent(applicationContext , MainActivity::class.java)
                   startActivity(intent)
               }
               else {
                  // progressBar?.visibility  = View.INVISIBLE

                   var model = modelForTotal("0", "0","0", "0")

                    mref.child("total").setValue(model).addOnCompleteListener{
                        if(it.isSuccessful)
                        {
                            progressBar?.visibility  = View.INVISIBLE
                             val intent = Intent(applicationContext , MainActivity::class.java)
                            startActivity(intent)

                        }
                        else {
                            progressBar?.visibility  = View.INVISIBLE

                            Toast.makeText(applicationContext, it.exception.toString(), Toast.LENGTH_LONG).show()

                        }
                    }
               }


           }


       }

        mref.addListenerForSingleValueEvent(listener)

    }

    override fun onStart() {
        super.onStart()


        var firbaseuser : FirebaseUser? = FirebaseAuth.getInstance().currentUser

        if(firbaseuser != null ){
            val intent = Intent(applicationContext , MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }
}


