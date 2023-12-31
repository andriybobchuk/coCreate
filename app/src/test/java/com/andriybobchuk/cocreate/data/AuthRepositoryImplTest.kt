
import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.auth.data.repository.AuthRepositoryImpl
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.Resource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class AuthRepositoryImplTest {

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirebaseFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockCoreRepository: CoreRepository

    private lateinit var authRepository: AuthRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        authRepository = AuthRepositoryImpl(mockFirebaseAuth, mockFirebaseFirestore, mockCoreRepository)
    }

    @Test
    fun `login user emits success with valid credentials`() = runBlockingTest {
        // Arrange
        val email = "user@example.com"
        val password = "password123"
        val mockAuthResult = mock(AuthResult::class.java)
        val mockTask = Tasks.forResult(mockAuthResult) // Mocking a successful task
        `when`(mockFirebaseAuth.signInWithEmailAndPassword(email, password)).thenReturn(mockTask)

        val emissions = mutableListOf<Resource<AuthResult>>()
        // Act
        authRepository.loginUser(email, password).collect { resource ->
            println("Resource emitted: $resource")
            emissions.add(resource)
        }

        // Assert
        // Check if any of the emissions is a success
        val hasSuccess = emissions.any { it is Resource.Success }
        assertTrue(hasSuccess, "Expected at least one emission to be Resource.Success")
    }

    @Test
    fun `register user emits success with valid data`() = runBlockingTest {
        // Arrange
        val name = "New User"
        val email = "newuser@example.com"
        val password = "password123"
        val mockAuthResult = mock(AuthResult::class.java)
        val mockTask = Tasks.forResult(mockAuthResult) // Mocking a successful task
        val mockCollectionRef = mock(CollectionReference::class.java)
        val mockDocumentRef = mock(DocumentReference::class.java)

        `when`(mockFirebaseAuth.createUserWithEmailAndPassword(email, password)).thenReturn(mockTask)
        `when`(mockCoreRepository.getCurrentUserID()).thenReturn("UserID")
        `when`(mockFirebaseFirestore.collection(anyString())).thenReturn(mockCollectionRef)
        `when`(mockCollectionRef.document(anyString())).thenReturn(mockDocumentRef)

        val emissions = mutableListOf<Resource<AuthResult>>()

        // Act
        authRepository.registerUser(name, email, password).collect { resource ->
            println("Resource emitted: $resource")
            if (resource is Resource.Error) {
                println("Error: ${resource.message}") // Log error details
            }
            emissions.add(resource)
        }

        // Assert
        assertTrue(emissions.any { it is Resource.Success }, "Expected at least one emission to be Resource.Success")
    }

    @Test
    fun `create basic profile in Firestore calls Firestore with correct data`() {
        // Arrange
        val profileData = ProfileData("UserID", "New User", "newuser@example.com", "")
        val userID = "UserID"
        `when`(mockCoreRepository.getCurrentUserID()).thenReturn(userID)

        // Mocking Firestore interaction
        val mockCollectionRef = mock(CollectionReference::class.java)
        val mockDocumentRef = mock(DocumentReference::class.java)
        `when`(mockFirebaseFirestore.collection(Constants.PROFILE_DATA)).thenReturn(mockCollectionRef)
        `when`(mockCollectionRef.document(userID)).thenReturn(mockDocumentRef)

        // Act
        authRepository.createBasicProfileInFirestore(profileData)

        // Assert
        verify(mockCollectionRef).document(userID)
        verify(mockDocumentRef).set(eq(profileData), any(SetOptions::class.java))
    }

    @Test
    fun `google login emits success with valid credentials`() = runBlockingTest {
        // Arrange
        val mockAuthCredential = mock(AuthCredential::class.java)
        val mockAuthResult = mock(AuthResult::class.java)
        val mockTask = Tasks.forResult(mockAuthResult)
        `when`(mockFirebaseAuth.signInWithCredential(mockAuthCredential)).thenReturn(mockTask)

        val emissions = mutableListOf<Resource<AuthResult>>()

        // Act
        authRepository.googleLogin(mockAuthCredential).collect { resource ->
            println("Resource emitted: $resource")
            emissions.add(resource)
        }

        // Assert
        assertTrue(emissions.any { it is Resource.Success }, "Expected at least one emission to be Resource.Success")
    }

    @Test
    fun `log out calls signOut on FirebaseAuth`() {
        // Act
        authRepository.logOut()

        // Assert
        verify(mockFirebaseAuth).signOut()
    }

}

