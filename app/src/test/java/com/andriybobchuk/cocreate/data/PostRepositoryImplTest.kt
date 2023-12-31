package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.core.domain.model.Comment
import com.andriybobchuk.cocreate.core.domain.model.Post
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.andriybobchuk.cocreate.util.getCurrentDateTime
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class PostRepositoryImplTest {

    // Mock the dependencies
    @Mock
    private lateinit var coreRepository: CoreRepository

    @Mock
    private lateinit var firebaseFirestore: FirebaseFirestore

    @Mock
    private lateinit var documentReference: DocumentReference

    private lateinit var postRepositoryImpl: PostRepositoryImpl

    @Mock
    private lateinit var mockPostsCollection: QuerySnapshot

    @Mock
    private lateinit var mockDocumentSnapshot: DocumentSnapshot

    @Mock
    private lateinit var mockQuery: Query

    @Mock
    private lateinit var mockQuerySnapshot: QuerySnapshot

    // Sample data for testing
    private val testUserID = "testUserId"
    private val testProfileData = ProfileData(/* initialize with test data */)
    private val testPost = Post(/* initialize with test data */)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(firebaseFirestore.collection(any())).thenReturn(mock(CollectionReference::class.java))
        `when`(firebaseFirestore.collection(any()).document()).thenReturn(documentReference)

        postRepositoryImpl = PostRepositoryImpl(coreRepository, firebaseFirestore)
    }

    @Test
    fun `getCurrentUserID returns correct id`() {
        // Arrange
        val expectedUserId = "12345"
        `when`(coreRepository.getCurrentUserID()).thenReturn(expectedUserId)

        // Act
        val resultUserId = postRepositoryImpl.getCurrentUserID()

        // Assert
        verify(coreRepository).getCurrentUserID() // Verify the interaction with coreRepository
        assert(resultUserId == expectedUserId) // Assert that the result is as expected
    }

    @Test
    fun `addNewPost adds post and returns true on success`() = runTest {
        // Arrange
        val mockTitle = "Mock Title"
        val mockDesc = "Mock Description"
        val mockTags = listOf("Tag1", "Tag2")
        val mockUserId = "MockUserId"
        val mockDocumentId = "MockDocumentId"

        `when`(coreRepository.getCurrentUserID()).thenReturn(mockUserId)
        `when`(documentReference.id).thenReturn(mockDocumentId) // Mocking the ID return of the new document reference
        `when`(firebaseFirestore.collection(any()).document()).thenReturn(documentReference)

        // Mocking the set operation to simulate success
        `when`(documentReference.set(any())).thenReturn(Tasks.forResult(null))

        // Act
        val isSuccess = postRepositoryImpl.addNewPost(mockTitle, mockDesc, mockTags)

        // Assert
        // Verify that set was called with a Post object containing the expected properties
        verify(documentReference).set(
            argThat { post: Post ->
                post.uid == mockDocumentId
                        && post.author == mockUserId
                        && post.title == mockTitle
                        && post.desc == mockDesc
                        && post.tags == mockTags
                        && post.likes == 0
                        && post.comments == 0
            }
        )
        assert(isSuccess) // Assert that the function returns true
    }


    @Test
    fun `updatePost updates post and returns true on success`() = runTest {
        // Arrange
        val mockPostId = "MockPostId"
        val updatedTitle = "Updated Title"
        val updatedDesc = "Updated Description"
        val updatedTags = listOf("UpdatedTag1", "UpdatedTag2")
        val mockCurrentDateTime = getCurrentDateTime() // Assuming this is a function that gets the current date and time

        // Mocking Firestore interactions
        `when`(firebaseFirestore.collection(Constants.POSTS).document(mockPostId)).thenReturn(documentReference)

        // Mocking successful update operation
        val successfulUpdateTask: Task<Void> = Tasks.forResult(null)
        `when`(documentReference.update(anyMap())).thenReturn(successfulUpdateTask)


        // Act
        val isSuccess = postRepositoryImpl.updatePost(mockPostId, updatedTitle, updatedDesc, updatedTags)

        // Assert
        // Verify that update was called on the document reference with the expected properties
        verify(documentReference).update(
            argThat { map: Map<String, Any> ->
                map["title"] == updatedTitle &&
                        map["desc"] == updatedDesc &&
                        map["tags"] == updatedTags &&
                        map["published"] != null // checking if published date is set
            }
        )
        assert(isSuccess) // Assert that the function returns true
    }

    @Test
    fun `deletePost deletes post and returns true on success`() = runTest {
        // Arrange
        val mockPostId = "MockPostId"

        // Mocking Firestore interactions
        `when`(firebaseFirestore.collection(Constants.POSTS).document(mockPostId)).thenReturn(documentReference)

        // Mocking successful delete operation
        val successfulDeleteTask: Task<Void> = Tasks.forResult(null)
        `when`(documentReference.delete()).thenReturn(successfulDeleteTask)

        // Act
        val isSuccess = postRepositoryImpl.deletePost(mockPostId)

        // Assert
        verify(documentReference).delete() // Verify that delete was called on the document reference
        assert(isSuccess) // Assert that the function returns true
    }

    @Test
    fun `getAllPosts returns list of posts`() = runTest {
        // Arrange
        val mockUserId = "MockUserId"
        val mockPostId = "MockPostId"
        val mockPost = Post(
            uid = mockPostId,
            author = mockUserId,
            title = "Test Title",
            desc = "Test Description",
            published = "Test Date",
            tags = listOf("Tag1", "Tag2"),
            likes = 5,
            comments = 2,
            isMine = false
        )

        `when`(coreRepository.getCurrentUserID()).thenReturn(mockUserId)
        `when`(firebaseFirestore.collection("posts").get()).thenReturn(Tasks.forResult(mockPostsCollection))
        `when`(mockPostsCollection.documents).thenReturn(listOf(mockDocumentSnapshot))
        `when`(mockDocumentSnapshot.toObject(Post::class.java)).thenReturn(mockPost)

        // Act
        val result = postRepositoryImpl.getAllPosts()

        // Assert
        assert(result.isNotEmpty()) // Assert that the result is not empty
        assert(result.first().isMine) // Assert that the post is marked as mine
    }

    @Test
    fun `getMyPosts returns list of my posts`() = runTest {
        // Arrange
        val mockUserId = "MockUserId"
        val mockPost = Post(
            uid = "MockPostId",
            author = mockUserId,
            title = "Test Title",
            desc = "Test Description",
            published = "Test Date",
            tags = listOf("Tag1", "Tag2"),
            likes = 5,
            comments = 2
        )

        `when`(coreRepository.getCurrentUserID()).thenReturn(mockUserId)
        `when`(firebaseFirestore.collection("posts").whereEqualTo("author", mockUserId)).thenReturn(mockQuery)
        `when`(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot))
        `when`(mockQuerySnapshot.documents).thenReturn(listOf(mockDocumentSnapshot))
        `when`(mockDocumentSnapshot.toObject(Post::class.java)).thenReturn(mockPost)

        // Act
        val result = postRepositoryImpl.getMyPosts()

        // Assert
        assert(result.isNotEmpty()) // Assert that the result is not empty
        assert(result.first().author == mockUserId) // Assert that the posts are authored by the current user
    }

    @Test
    fun `getCommentsByPostId returns list of comments`() = runTest {
        // Arrange
        val postId = "MockPostId"
        val mockComment = Comment(
            uid = "MockCommentId",
            postUid = postId,
            author = "MockAuthorId",
            desc = "Mock Comment Description",
            published = "Mock Date"
        )

        `when`(firebaseFirestore.collection("comments").whereEqualTo("postUid", postId)).thenReturn(mockQuery)
        `when`(mockQuery.get()).thenReturn(Tasks.forResult(mockQuerySnapshot))
        `when`(mockQuerySnapshot.documents).thenReturn(listOf(mockDocumentSnapshot))
        `when`(mockDocumentSnapshot.toObject(Comment::class.java)).thenReturn(mockComment)

        // Act
        val result = postRepositoryImpl.getCommentsByPostId(postId)

        // Assert
        assert(result.isNotEmpty()) // Assert that the result is not empty
        assert(result.first().postUid == postId) // Assert that the comments are for the correct post
    }

    @Test
    fun `getPostDataById returns post data successfully`() = runTest {
        // Arrange
        val postId = "MockPostId"
        val expectedPost = Post(uid = postId, title = "Test Post")
        val mockCollectionReference = mock(CollectionReference::class.java)
        val mockDocumentReference = mock(DocumentReference::class.java)

        `when`(firebaseFirestore.collection("post")).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.document(postId)).thenReturn(mockDocumentReference)
        `when`(mockDocumentReference.get()).thenReturn(Tasks.forResult(mockDocumentSnapshot))
        `when`(mockDocumentSnapshot.toObject(Post::class.java)).thenReturn(expectedPost)

        // Act
        val result = postRepositoryImpl.getPostDataById(postId)

        // Assert
        assert(result == expectedPost) // Assert that the result matches the expected post
    }


    @Test
    fun `addComment adds comment and returns true on success`() = runTest {
        // Arrange
        val postUid = "MockPostUid"
        val desc = "Mock Comment Description"
        val mockCommentId = "MockCommentId"
        val mockCurrentDateTime = getCurrentDateTime() // Assuming this is a function that gets the current date and time
        val mockUserId = "MockUserId"

        `when`(coreRepository.getCurrentUserID()).thenReturn(mockUserId)
        `when`(firebaseFirestore.collection("comments").document()).thenReturn(documentReference)
        `when`(documentReference.id).thenReturn(mockCommentId)
        `when`(documentReference.set(any(Comment::class.java))).thenReturn(Tasks.forResult(null))

        // Act
        val isSuccess = postRepositoryImpl.addComment(postUid, desc)

        // Assert
        verify(documentReference).set(any(Comment::class.java)) // Verify that set was called with a Comment object
        assert(isSuccess) // Assert that the function returns true
    }

    @Test
    fun `removeComment removes comment and returns true on success`() = runTest {
        // Arrange
        val commentId = "MockCommentId"
        val mockCollectionReference = mock(CollectionReference::class.java)
        val mockDocumentReference = mock(DocumentReference::class.java)

        `when`(firebaseFirestore.collection("comment")).thenReturn(mockCollectionReference)
        `when`(mockCollectionReference.document(commentId)).thenReturn(mockDocumentReference)
        `when`(mockDocumentReference.delete()).thenReturn(Tasks.forResult(null))

        // Act
        val isSuccess = postRepositoryImpl.removeComment(commentId)

        // Assert
        verify(firebaseFirestore.collection("comment").document(commentId)).delete() // Verify that delete was called on the document reference
        assert(isSuccess) // Assert that the function returns true
    }
}

