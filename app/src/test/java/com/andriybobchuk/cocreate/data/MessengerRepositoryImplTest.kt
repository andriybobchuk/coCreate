package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.messages.domain.model.Conversation
import com.andriybobchuk.cocreate.feature.messages.domain.model.Message
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class MessengerRepositoryImplTest {

    @Mock
    private lateinit var coreRepository: CoreRepository

    @Mock
    private lateinit var firebaseFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockDocumentSnapshot: DocumentSnapshot

    @Mock
    private lateinit var mockDocumentReference: DocumentReference

    @Mock
    private lateinit var mockCollectionReference: CollectionReference

    @Mock
    private lateinit var mockQuerySnapshot: QuerySnapshot

    @Mock
    private lateinit var mockQueryDocumentSnapshot: QueryDocumentSnapshot

    private lateinit var messengerRepositoryImpl: MessengerRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        messengerRepositoryImpl = MessengerRepositoryImpl(coreRepository, firebaseFirestore)
    }

    // Write individual test cases here
    // Example for getCurrentUserID
    @Test
    fun `getCurrentUserID returns correct user ID`() {
        // Arrange
        val expectedUserId = "MockUserId"
        `when`(coreRepository.getCurrentUserID()).thenReturn(expectedUserId)

        // Act
        val userId = messengerRepositoryImpl.getCurrentUserID()

        // Assert
        assert(userId == expectedUserId)
    }

    /**
    Initially this test fails and it's absolutely fine.
    This happens because Log.e is not mocked by Mockito library.

    To make it pass, comment out all the ccLog lines in the getConversationsByUserId method
     */
    @Test
    @Ignore("Comment out ccLog in the getConversationsByUserId")
    fun `getConversationsByUserId returns list of conversations`() = runTest {
        // Arrange
        val userId = "TestUserId"
        val mockQuery = mock(Query::class.java)
        val mockQuerySnapshot = mock(QuerySnapshot::class.java)
        val conversation1 = Conversation(/* initialize with test data */)
        val conversation2 = Conversation(/* initialize with test data */)
        val mockDocumentSnapshot1 = mock(DocumentSnapshot::class.java)
        val mockDocumentSnapshot2 = mock(DocumentSnapshot::class.java)

        `when`(firebaseFirestore.collection(Constants.CONVERSATION)).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.whereArrayContains("participants", userId)).thenReturn(mockQuery)
        `when`(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot))
        `when`(mockQuerySnapshot.documents).thenReturn(listOf(mockDocumentSnapshot1, mockDocumentSnapshot2))
        `when`(mockDocumentSnapshot1.toObject(Conversation::class.java)).thenReturn(conversation1)
        `when`(mockDocumentSnapshot2.toObject(Conversation::class.java)).thenReturn(conversation2)

        // Act
        val result = messengerRepositoryImpl.getConversationsByUserId(userId)

        // Assert
        assert(result.size == 2) // assuming there are two conversations returned
        assert(result.containsAll(listOf(conversation1, conversation2))) // check if the returned list contains the mocked conversations
    }

    @Test
    fun `getMessageById returns a specific message`() = runTest {
        // Arrange
        val messageId = "TestMessageId"
        val expectedMessage = Message(
            convoId = "TestConvoId",
            id = messageId,
            senderId = "TestSenderId",
            content = "Test Content",
            time = "Sep 21, '23 at 8:34",
            isMyMessage = false
        )
        val mockDocumentReference = mock(DocumentReference::class.java)
        val mockDocumentSnapshot = mock(DocumentSnapshot::class.java)

        `when`(firebaseFirestore.collection(Constants.MESSAGE)).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.document(messageId)).thenReturn(mockDocumentReference)
        `when`(mockDocumentReference.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))
        `when`(mockDocumentSnapshot.toObject(Message::class.java)).thenReturn(expectedMessage)

        // Act
        val result = messengerRepositoryImpl.getMessageById(messageId)

        // Assert
        assert(result == expectedMessage) // Check that the result matches the expected message
    }

    /**
    Initially this test fails and it's absolutely fine.
    This happens because Log.e is not mocked by Mockito library.

    To make it pass, comment out all the ccLog lines in the getConversationsByUserId method
     */
    @Test
    @Ignore("Comment out ccLog in the getConversationsByUserId")
    fun `getConversationById returns a specific conversation`() = runTest {
        // Arrange
        val chatId = "TestChatId"
        val expectedConversation = Conversation(
            uid = chatId,
            participants = listOf("User1", "User2"),
            lastMessageId = "LastMessage",
            isRead = true
        )
        val mockDocumentReference = mock(DocumentReference::class.java)
        val mockDocumentSnapshot = mock(DocumentSnapshot::class.java)

        `when`(firebaseFirestore.collection(Constants.CONVERSATION)).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.document(chatId)).thenReturn(mockDocumentReference)
        `when`(mockDocumentReference.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))
        `when`(mockDocumentSnapshot.toObject(Conversation::class.java)).thenReturn(expectedConversation)

        // Act
        val result = messengerRepositoryImpl.getConversationById(chatId)

        // Assert
        assert(result == expectedConversation) // Check that the result matches the expected conversation
    }

    @Test
    fun `createNewConversation creates and returns new conversation id`() = runTest {
        // Arrange
        val recipientId = "TestRecipientId"
        val currentUserUid = "TestCurrentUserId"
        val newConversationId = "NewConversationId"

        `when`(coreRepository.getCurrentUserID()).thenReturn(currentUserUid)
        val mockDocumentReference = mock(DocumentReference::class.java)

        `when`(firebaseFirestore.collection(Constants.CONVERSATION)).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.add(anyMap<String, Any>())).thenReturn(Tasks.forResult(mockDocumentReference))
        `when`(mockDocumentReference.id).thenReturn(newConversationId)

        // Ensure the mock returns the mockDocumentReference when document(newConversationId) is called
        `when`(mockCollectionReference.document(newConversationId)).thenReturn(mockDocumentReference)

        // Stub the update method on the mockDocumentReference
        `when`(mockDocumentReference.update("uid", newConversationId)).thenReturn(Tasks.forResult(null))

        // Act
        val result = messengerRepositoryImpl.createNewConversation(recipientId)

        // Assert
        verify(mockCollectionReference).add(anyMap<String, Any>()) // Verify that the conversation was added
        verify(mockDocumentReference).update("uid", newConversationId) // Verify that the conversation's uid was updated
        assert(result == newConversationId) // Check that the result matches the new conversation id
    }

    @Test
    fun `findExistingConversationWithUser returns existing conversation id or null`() = runTest {
        // Arrange
        val userId = "TestUserId"
        val currentUserUid = "TestCurrentUserId"
        val existingConversationId = "ExistingConversationId"

        `when`(coreRepository.getCurrentUserID()).thenReturn(currentUserUid)
        val mockQuery = mock(Query::class.java)
        val mockQuerySnapshot = mock(QuerySnapshot::class.java)
        val mockDocumentSnapshot = mock(DocumentSnapshot::class.java)

        // Simulating a document that represents an existing conversation
        `when`(mockDocumentSnapshot.id).thenReturn(existingConversationId)
        `when`(mockDocumentSnapshot.get("participants")).thenReturn(listOf(currentUserUid, userId))
        `when`(mockDocumentSnapshot.getString("lastMessageId")).thenReturn("SomeLastMessageId")

        `when`(firebaseFirestore.collection(Constants.CONVERSATION)).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.whereArrayContains("participants", currentUserUid)).thenReturn(mockQuery)
        `when`(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot))
        `when`(mockQuerySnapshot.documents).thenReturn(listOf(mockDocumentSnapshot))

        // Act
        val result = messengerRepositoryImpl.findExistingConversationWithUser(userId)

        // Assert
        assert(result == existingConversationId) // Check that the result matches the existing conversation id
    }

}
