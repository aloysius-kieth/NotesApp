package com.example.noteapp.feature_note.domain.use_case

import com.example.noteapp.feature_note.domain.model.InvalidNoteException
import com.example.noteapp.feature_note.domain.model.Note
import com.example.noteapp.feature_note.repository.FakeNoteRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Before
import org.junit.Test

class AddNoteTest {

    private lateinit var addNote: AddNote
    private lateinit var fakeNoteRepository: FakeNoteRepository

    @Before
    fun setup() {
        fakeNoteRepository = FakeNoteRepository()
        addNote = AddNote(fakeNoteRepository)
    }

    @Test
    fun `Insert note to repository, title and content not empty, throws exception`() = runBlocking {
        addNote(
            Note(
                title = "Some title",
                content = "Some content",
                timestamp = 123,
                color = 1
            )
        )
    }
}