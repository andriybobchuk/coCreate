package com.andriybobchuk.cocreate.feature.auth.data.repository

import com.andriybobchuk.cocreate.core.Constants
import com.andriybobchuk.cocreate.core.data.repository.CoreRepository
import com.andriybobchuk.cocreate.feature.profile.domain.model.ProfileData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ContactsRepositoryImplTest {

    @Mock
    private lateinit var coreRepository: CoreRepository

    @Mock
    private lateinit var firebaseFirestore: FirebaseFirestore

    @Mock
    private lateinit var mockDocumentSnapshot: DocumentSnapshot

    @Mock
    private lateinit var mockDocumentReference: DocumentReference

    private lateinit var contactsRepositoryImpl: ContactsRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        contactsRepositoryImpl = ContactsRepositoryImpl(coreRepository, firebaseFirestore)
    }

    @Test
    fun `getCurrentUserID returns correct user ID`() {
        // Arrange
        val expectedUserId = "MockUserId"
        `when`(coreRepository.getCurrentUserID()).thenReturn(expectedUserId)

        // Act
        val userId = contactsRepositoryImpl.getCurrentUserID()

        // Assert
        assert(userId == expectedUserId)
    }


    @Test
    fun `getAllPeople returns list of all people`() = runTest {
        // Arrange
        val mockPeopleList = listOf(ProfileData(/*...*/))
        `when`(coreRepository.getAllPeople()).thenReturn(mockPeopleList)

        // Act
        val peopleList = contactsRepositoryImpl.getAllPeople()

        // Assert
        assert(peopleList == mockPeopleList)
    }


    @Test
    fun `getProfileDataById returns correct profile data`() = runTest {
        // Arrange
        val profileId = "MockProfileId"
        val expectedProfileData = ProfileData(/*...*/)
        `when`(coreRepository.getProfileDataById(profileId)).thenReturn(expectedProfileData)

        // Act
        val profileData = contactsRepositoryImpl.getProfileDataById(profileId)

        // Assert
        assert(profileData == expectedProfileData)
    }
}
