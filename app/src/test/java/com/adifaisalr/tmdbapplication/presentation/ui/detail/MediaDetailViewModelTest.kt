//package com.adifaisalr.tmdbapplication.presentation.ui.detail
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.Observer
//import com.adifaisalr.tmdbapplication.MainCoroutineRule
//import com.adifaisalr.tmdbapplication.domain.model.Media
//import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
//import com.adifaisalr.tmdbapplication.domain.usecase.GetMediaDetailUseCase
//import com.adifaisalr.tmdbapplication.domain.usecase.GetMediaReviewUseCase
//import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
//import io.mockk.coEvery
//import io.mockk.mockk
//import io.mockk.spyk
//import io.mockk.verifyOrder
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.runBlockingTest
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//
///**
// * Unit tests for the implementation of [MediaDetailViewModel]
// */
//@ExperimentalCoroutinesApi
//class MediaDetailViewModelTest {
//
//    private val getMediaDetailUseCase = mockk<GetMediaDetailUseCase>(relaxed = true)
//    private val getMediaReviewUseCase = mockk<GetMediaReviewUseCase>(relaxed = true)
//
//    // Subject under test
//    private lateinit var viewModel: MediaDetailViewModel
//
//    // Set the main coroutines dispatcher for unit testing.
//    @ExperimentalCoroutinesApi
//    @get:Rule
//    var mainCoroutineRule = MainCoroutineRule()
//
//    // Executes each task synchronously using Architecture Components.
//    @get:Rule
//    var instantExecutorRule = InstantTaskExecutorRule()
//
//    // observers
//    private lateinit var mediaDetailObserver: Observer<DataHolder<Media>>
//
//    @Before
//    fun setupViewModel() {
//        viewModel = spyk(
//            MediaDetailViewModel(
//                getMediaDetailUseCase,
//                getMediaReviewUseCase
//            )
//        )
//        viewModel.mediaType = MediaViewModel.Companion.MediaType.MOVIES
//        viewModel.mediaId = 123
//    }
//
//    @Test
//    fun `getMovieDetail should set DataHolder to loading then set result`() {
//        runBlockingTest {
//            // given
//            observeMediaDetail()
//
//            val mockData = mockk<Media>()
//            val mockResult = DataHolder.Success(mockData)
//            coEvery {
//                getMediaDetailUseCase.invoke(any(), any())
//            } answers {
//                mockResult
//            }
//
//            // when
//            viewModel.getMovieDetail()
//
//            // then
//            verifyOrder {
//                mediaDetailObserver.onChanged(DataHolder.Loading)
//                mediaDetailObserver.onChanged(mockResult)
//            }
//        }
//    }
//
//    /** Observers */
//    private fun observeMediaDetail() {
//        mediaDetailObserver = mockk(relaxed = true)
//        viewModel.mediaDetailResult.observeForever(mediaDetailObserver)
//    }
//}
