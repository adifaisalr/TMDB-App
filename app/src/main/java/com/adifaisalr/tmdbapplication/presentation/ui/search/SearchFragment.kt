package com.adifaisalr.tmdbapplication.presentation.ui.search

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.adifaisalr.tmdbapplication.databinding.FragmentSearchBinding
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.presentation.ui.MainViewModel
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.SearchLoadStateAdapter
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.SearchResultPagingAdapter
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()
    val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: SearchResultPagingAdapter
    var searchResults: ArrayList<SearchItem> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        mainViewModel.updateBottomNav(false)
        initSearchInputListener()
        initRecyclerView()
        observeViewModel()
    }

    private fun initSearchInputListener() {
        binding.input.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.input.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.searchBtn.setOnClickListener { view ->
            doSearch(view)
        }
    }

    private fun clearAdapter() {
        searchResults.clear()
        adapter.notifyDataSetChanged()
    }

    private fun observeViewModel() {
        viewModel.listViewStateLiveData.observe(viewLifecycleOwner) { state ->
            state.loadingStateVisibility?.let { binding.loading.visibility = it }
            lifecycleScope.launch {
                state.page?.let { adapter.submitData(it) }
            }
            state.errorVisibility?.let {
                binding.mainListErrorMsg.visibility = it
                binding.retryButton.visibility = it
                state.errorMessage?.let { binding.mainListErrorMsg.text = state.errorMessage }
                state.errorMessageResource?.let {
                    binding.mainListErrorMsg.text = getString(state.errorMessageResource)
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = SearchResultPagingAdapter { searchItem ->
            val mediaType = MediaViewModel.Companion.MediaType.values().find { it.type == searchItem.mediaType }
            mediaType?.let {
                val action = SearchFragmentDirections.actionSearchFragmentToMediaDetailFragment(searchItem.id, it)
                findNavController().safeNavigate(action)
            }
        }
        binding.userList.adapter = adapter.withLoadStateHeaderAndFooter(
            header = SearchLoadStateAdapter { adapter.retry() },
            footer = SearchLoadStateAdapter { adapter.retry() }
        )
    }

    private fun doSearch(v: View) {
        val query = binding.input.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        viewModel.setQuery(query)
//        viewModel.searchMedia()
        viewModel.searchMediaPaging()
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setLoadMore(isLoading: Boolean) {
        binding.loadMoreBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setError(errorMessage: String?) {
        binding.error.visibility = if (errorMessage.isNullOrEmpty()) View.GONE else View.VISIBLE
        binding.error.text = errorMessage
    }
}