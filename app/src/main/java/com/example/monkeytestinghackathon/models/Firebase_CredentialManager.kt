package com.example.monkeytestinghackathon.models

import android.content.Context
import android.credentials.Credential
import android.credentials.GetCredentialRequest
import android.provider.Settings.Global.getString
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.example.monkeytestinghackathon.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class Firebase_CredentialManager(
    private val context: Context
) {
    private val credentialManager: CredentialManager = CredentialManager.create(context)
    private val firebaseAuth: FirebaseAuth by lazy {
        if (FirebaseApp.getApps(context).isEmpty()) {
            FirebaseApp.initializeApp(context)
        }
        FirebaseAuth.getInstance()
    }

    private val googleIdOption: CredentialOption =
        GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.web_client_id))
            .setFilterByAuthorizedAccounts(false)
            .build()

    private val request: androidx.credentials.GetCredentialRequest =
        androidx.credentials.GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

    suspend fun signIn(): SignInResult {
        return try {
            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = result.credential
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                val firebaseCred = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(firebaseCred).await()
                val user = authResult.user

                if (user != null) {
                    SignInResult(
                        data = UserData(
                            userId = user.uid,
                            username = user.displayName,
                            profilePictureUrl = user.photoUrl?.toString()
                        ),
                        errorMessage = null
                    )
                } else {
                    SignInResult(data = null, errorMessage = "Użytkownik jest null po logowaniu.")
                }
            } else {
                SignInResult(data = null, errorMessage = "Niepoprawny typ poświadczenia.")
            }
        } catch (e: GetCredentialException) {
            SignInResult(data = null, errorMessage = e.localizedMessage ?: "Błąd pobierania poświadczeń.")
        } catch (e: Exception) {
            SignInResult(data = null, errorMessage = e.localizedMessage ?: "Błąd logowania.")
        }
    }

}