import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepositoryImpl
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@ExperimentalCoroutinesApi
class CoreRepositoryImplTest {

    @Mock
    private lateinit var mockFirebaseFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockFirebaseAuth: FirebaseAuth

    @Mock
    private lateinit var mockFirebaseUser: FirebaseUser

    @Mock
    private lateinit var mockCollectionRef: CollectionReference

    @Mock
    private lateinit var mockDocumentRef: DocumentReference

    @Mock
    private lateinit var mockDocumentSnapshot: DocumentSnapshot

    @Mock
    private lateinit var mockQuerySnapshot: QuerySnapshot

    private lateinit var coreRepositoryImpl: CoreRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        coreRepositoryImpl = CoreRepositoryImpl(mockFirebaseFirestore, mockFirebaseAuth)

        `when`(mockFirebaseFirestore.collection(anyString())).thenReturn(mockCollectionRef)
        `when`(mockCollectionRef.get()).thenReturn(mock(Task::class.java) as Task<QuerySnapshot>)
    }


    @Test
    fun `getCurrentUserID returns correct user id`() {
        // Arrange
        val expectedUserId = "testUserId"
        `when`(mockFirebaseAuth.currentUser).thenReturn(mockFirebaseUser)
        `when`(mockFirebaseUser.uid).thenReturn(expectedUserId)

        // Act
        val userId = coreRepositoryImpl.getCurrentUserID()

        // Assert
        verify(mockFirebaseAuth).currentUser
        assert(userId == expectedUserId)
    }
}