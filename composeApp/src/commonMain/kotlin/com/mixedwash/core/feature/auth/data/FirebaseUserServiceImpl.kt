package com.mixedwash.core.feature.auth.data

import com.mixedwash.core.data.util.AppCoroutineScope
import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.core.domain.models.Result
import com.mixedwash.core.feature.auth.domain.UserService
import com.mixedwash.core.feature.auth.domain.model.AuthState
import com.mixedwash.core.feature.auth.domain.model.User
import com.mixedwash.core.feature.auth.domain.model.UserMetadata
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

const val USER_COLLECTION = "USERS"

class FirebaseUserService(applicationScope: AppCoroutineScope) : UserService {

    private val db = Firebase.firestore

    private val userMutex = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val userStateFlow: StateFlow<User?> = Firebase.auth.authStateChanged
        .flatMapLatest { firebaseUser ->
            return@flatMapLatest try {
                if (firebaseUser == null) {
                    // No user signed in – emit null
                    flowOf(null)
                } else {
                    // Convert firebaseUser to your User model (with null metadata initially)
                    val user = User(
                        uid = firebaseUser.uid,
                        providerId = firebaseUser.providerId,
                        lastSignInTime = firebaseUser.metaData?.lastSignInTime
                    )
                    // Listen to changes in Firestore for this user’s metadata
                    db.collection(USER_COLLECTION)
                        .document(user.uid)
                        .snapshots()
                        .map { snapshot ->
                            if (snapshot.exists) {
                                val metadata = snapshot.data<UserMetadata>()
                                user.copy(userMetadata = metadata)
                            } else {
                                // trigger metadata creation
                                applicationScope.launch {
                                    db.collection(USER_COLLECTION).document(user.uid).set(
                                        data = UserMetadata(
                                            uid = firebaseUser.uid,
                                            phoneNumber = null,
                                            lastOpenTimeStamp = null,
                                            email = firebaseUser.email,
                                            name = firebaseUser.displayName,
                                            photoUrl = firebaseUser.photoURL,
                                            addressList = emptyList()
                                        ),
                                        merge = false,
                                    )
                                }
                                // return baseUser
                                user
                            }
                        }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                flowOf(null)
            }
        }
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    override val currentUser: User?
        get() = userStateFlow.value

    override val authState: Flow<AuthState>
        get() = Firebase.auth.authStateChanged.map { firebaseUser ->
            return@map if (firebaseUser == null) {
                AuthState.Unauthenticated
            } else if (currentUser?.userMetadata == null) {  // currentUserFlow has not caught up with the firebase auth flow
                AuthState.Loading
            } else if (currentUser?.userMetadata?.phoneNumber.isNullOrBlank()) {
                AuthState.Authenticating
            } else {
                AuthState.Authenticated
            }
        }


    override suspend fun updateMetadata(update: (UserMetadata) -> UserMetadata): Result<UserMetadata> {
        try {
            val uid =
                currentUser?.uid ?: return Result.Error(ErrorType.NotFound("User Not Found"))

            val metadata = currentUser?.userMetadata ?: return Result.Error(
                error = ErrorType.NotFound(
                    "User Not Found"
                )
            )
            val updatedUser = metadata.let(update)
            db.collection(USER_COLLECTION).document(uid).update(updatedUser)
            return Result.Success(updatedUser)
        } catch (e: Exception) {
            return Result.Error(error = ErrorType.Unknown("Failed to update user metadata: ${e.message}"))
        }
    }


    override suspend fun deleteMetadata(): Result<Unit> {
        return userMutex.withLock {
            val uid = currentUser?.uid ?: return Result.Error(ErrorType.NotFound("User Not Found"))
            try {
                db.collection(USER_COLLECTION).document(uid).delete()
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(error = ErrorType.Unknown("Failed to delete user: ${e.message}"))
            }
        }
    }

    override val isSignedIn: Boolean
        get() = Firebase.auth.currentUser != null


    override suspend fun signOut(): Result<Unit> {
        return userMutex.withLock {
            try {
                Result.Success(Firebase.auth.signOut())
            } catch (e: Exception) {
                Result.Error(error = ErrorType.Unknown("Sign Out Error"))
            }
        }
    }

}