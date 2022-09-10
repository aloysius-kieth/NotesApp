package com.example.noteapp.feature_note.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.noteapp.di.AppModule
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.util.TestTags
import com.example.noteapp.util.TestTags.NOTE_ITEM
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            NoteAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards() {
        // CLick on FAB to transit to add note screen
        composeRule.onNodeWithContentDescription("Add").performClick()

        // Enters text in title and content textfields
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("test-title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .performTextInput("test-content")

        // Save note
        composeRule.onNodeWithContentDescription("Save").performClick()

        // Make sure there is note in list with title and content
        composeRule.onNodeWithText("test-title").assertIsDisplayed()
        // Click on note to edit it
        composeRule.onNodeWithText("test-title").performClick()

        // Make sure title and content text fields contain note title and content
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).assertTextEquals("test-title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).assertTextEquals("test-content")
        // Add the text "2 to the title text field
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).performTextInput("2")
        // Save note
        composeRule.onNodeWithContentDescription("Save").performClick()

        // Make sure update is applied to the list
        composeRule.onNodeWithText("test-title2").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for (i in 1..3) {
            // CLick on FAB to transit to add note screen
            composeRule.onNodeWithContentDescription("Add").performClick()

            // Enters text in title and content textfields
            composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
                .performTextInput(i.toString())
            composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
                .performTextInput(i.toString())

            // Save note
            composeRule.onNodeWithContentDescription("Save").performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        composeRule.onNodeWithContentDescription("Sort")
            .performClick()

        composeRule.onNodeWithContentDescription("Title")
            .performClick()
        composeRule.onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag(NOTE_ITEM)[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag(NOTE_ITEM)[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag(NOTE_ITEM)[2]
            .assertTextContains("1")
    }
}