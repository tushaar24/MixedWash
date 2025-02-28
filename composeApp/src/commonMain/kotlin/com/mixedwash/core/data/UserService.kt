package com.mixedwash.core.data

import com.mixedwash.core.domain.models.ErrorType
import com.mixedwash.core.domain.models.Result
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserMetadata(
    @SerialName("uid") val uid: String,
    @SerialName("phone_number") val phoneNumber: String?,
    @SerialName("last_open_time_stamp") val lastOpenTimeStamp: Long?,
    @SerialName("email") val email: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("photo_url") val photoUrl: String? = null,
    @SerialName("user_serviceable") val userServiceable: Boolean? = null,
    @SerialName("address_list") val addressList: List<AddressMetadata>
)

@Serializable
data class AddressMetadata(
    @SerialName("title") val title: String,
    @SerialName("google_place_id") val placeId: String
)


interface UserService {
    val authState: Flow<AuthState>

    val userStateFlow: StateFlow<User?>

    val currentUser: User?

    val isSignedIn: Boolean

    suspend fun signOut(): Result<Unit>

    suspend fun updateMetadata(
        phoneNumber: String? = null,
        lastOpenTimeStamp: Long? = null,
        email: String? = null,
        name: String? = null,
        photoUrl: String? = null,
        addressList: List<AddressMetadata>? = null
    ): Result<UserMetadata>

    suspend fun deleteMetadata(): Result<Unit>

}

private const val USER_COLLECTION = "USERS"


class FirebaseUserService(applicationScope: CoroutineScope) : UserService {

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
            started = WhileSubscribed(5000),
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


    override suspend fun updateMetadata(
        phoneNumber: String?,
        lastOpenTimeStamp: Long?,
        email: String?,
        name: String?,
        photoUrl: String?,
        addressList: List<AddressMetadata>?,
    ): Result<UserMetadata> {
        return userMutex.withLock {
            try {
                val uid =
                    currentUser?.uid ?: return Result.Error(ErrorType.NotFound("User Not Found"))

                val metadata = currentUser?.userMetadata ?: return Result.Error(
                    error = ErrorType.NotFound(
                        "User Not Found"
                    )
                )

                val updatedUser = metadata.copy(
                    phoneNumber = phoneNumber ?: metadata.phoneNumber,
                    lastOpenTimeStamp = lastOpenTimeStamp ?: metadata.lastOpenTimeStamp,
                    email = email ?: metadata.email,
                    name = name ?: metadata.name,
                    photoUrl = photoUrl ?: metadata.photoUrl,
                    addressList = addressList ?: metadata.addressList
                )
                db.collection(USER_COLLECTION).document(uid).update(updatedUser)
                return Result.Success(updatedUser)
            } catch (e: Exception) {
                Result.Error(error = ErrorType.Unknown("Failed to update user metadata: ${e.message}"))
            }
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


data class User(
    val uid: String,
    val providerId: String?,
    val lastSignInTime: Double?,
    val userMetadata: UserMetadata? = null
)

sealed interface AuthState {
    data object Loading : AuthState
    data object Authenticated : AuthState
    data object Authenticating : AuthState
    data object Unauthenticated : AuthState
}