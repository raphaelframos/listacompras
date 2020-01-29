package com.powellapps.compraparamim

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.powellapps.compraparamim.model.User
import com.powellapps.compraparamim.repository.FirebaseRepository
import com.powellapps.compraparamim.utils.Utils
import java.lang.Exception
import kotlin.math.sign

class LoginActivity : AppCompatActivity() {

    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var gso: GoogleSignInOptions
    lateinit var firebaseAuth: FirebaseAuth
    val RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        val signIn = findViewById<View>(R.id.google_button) as SignInButton;
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    override fun onResume() {
        super.onResume()
        if(!FirebaseRepository().existUser()){
            signInGoogle()
        }

    }

    private fun signInGoogle() {
        val signInIntent : Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener{
            Utils().show("Auth " + it.isSuccessful)
            if(it.isSuccessful){
                checkUser()
            }
        }
    }


    private fun checkUser() {
        var firebaseUser : FirebaseUser? = FirebaseAuth.getInstance().currentUser
        var getId : DocumentReference = FirebaseRepository().getDB().collection("users").document(firebaseUser?.uid.toString())
        getId.get().addOnCompleteListener{
            Utils().show("User " + it.isSuccessful)
            if(it.isSuccessful) {
                Utils().show("suc " + it.result)
                val document = it.result

                if(!document?.exists()!!) {
                    val id = firebaseUser?.uid
                    val name = firebaseUser?.displayName
                    val picture = firebaseUser?.photoUrl
                    val user = User(id,name, picture.toString());

                    FirebaseRepository().saveUser(user)
                    finish()

                }else{
                    Utils().show("fim " + firebaseUser?.displayName)
                    val user = it.result!!.toObject(User::class.java)
                    finish()
                }

            }
        }

    }

}
