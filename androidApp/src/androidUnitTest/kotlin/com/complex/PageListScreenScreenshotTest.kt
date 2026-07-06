package com.complex

import app.cash.paparazzi.Paparazzi
import com.complex.model.Page
import com.complex.viewmodel.PageViewModel
import org.junit.Rule
import org.junit.Test

class PageListScreenScreenshotTest {

    @get:Rule
    val paparazzi = Paparazzi()

    @Test
    fun emptyPageList() {
        val viewModel = PageViewModel(FakePageRepository())
        paparazzi.snapshot {
            MaterialThemeWrapper {
                PageListScreenForTest(viewModel = viewModel)
            }
        }
    }

    @Test
    fun pageListWithPages() {
        val pages = listOf(
            Page(
                id = "1",
                title = "First Page",
                content = "This is the content of the first page with some markdown.",
                createdAt = 1700000000000L,
                updatedAt = 1700000100000L
            ),
            Page(
                id = "2",
                title = "Second Page",
                content = "# Heading\n\nSome content here.",
                createdAt = 1700000200000L,
                updatedAt = 1700000300000L
            ),
            Page(
                id = "3",
                title = "",
                content = "",
                createdAt = 1700000400000L,
                updatedAt = 1700000500000L
            )
        )
        val viewModel = PageViewModel(FakePageRepository(pages))
        paparazzi.snapshot {
            MaterialThemeWrapper {
                PageListScreenForTest(viewModel = viewModel)
            }
        }
    }

    @Test
    fun pageListWithOnePage() {
        val pages = listOf(
            Page(
                id = "1",
                title = "My First Note",
                content = "This is a simple note with some content.",
                createdAt = 1700000000000L,
                updatedAt = 1700000100000L
            )
        )
        val viewModel = PageViewModel(FakePageRepository(pages))
        paparazzi.snapshot {
            MaterialThemeWrapper {
                PageListScreenForTest(viewModel = viewModel)
            }
        }
    }
}
