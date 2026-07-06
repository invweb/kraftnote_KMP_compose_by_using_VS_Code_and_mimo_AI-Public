package com.complex

import app.cash.paparazzi.Paparazzi
import com.complex.model.Page
import com.complex.viewmodel.PageViewModel
import org.junit.Rule
import org.junit.Test

class MarkdownEditorScreenScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun emptyEditor() {
        val viewModel = PageViewModel(FakePageRepository())
        paparazzi.snapshot {
            MaterialThemeWrapper {
                MarkdownEditorScreenForTest(viewModel = viewModel)
            }
        }
    }

    @Test
    fun editorWithMarkdownContent() {
        val page = Page(
            id = "1",
            title = "My Document",
            content = "# Main Title\n\n## Subtitle\n\nThis is a paragraph with some text.\n\n- Item 1\n- Item 2\n- Item 3\n\n**Bold text** and *italic text*.",
            createdAt = 1700000000000L,
            updatedAt = 1700000100000L
        )
        val repository = FakePageRepository(listOf(page))
        val viewModel = PageViewModel(repository)
        viewModel.selectPage(page.id)
        paparazzi.snapshot {
            MaterialThemeWrapper {
                MarkdownEditorScreenForTest(viewModel = viewModel)
            }
        }
    }
}
