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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adifaisalr.tmdbapplication.R
import com.adifaisalr.tmdbapplication.databinding.FragmentSearchBinding
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.presentation.ui.adapter.SearchResultAdapter
import com.adifaisalr.tmdbapplication.presentation.ui.home.HomeFragmentDirections
import com.adifaisalr.tmdbapplication.presentation.ui.media.MediaViewModel
import com.adifaisalr.tmdbapplication.presentation.util.NavigationUtils.safeNavigate
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: SearchResultAdapter
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
        viewModel.searchResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataHolder.Success -> {
                    setLoading(false)
                    if (result.data?.results.isNullOrEmpty()) {
                        clearAdapter()
                        setError(getString(R.string.empty_search_result, viewModel.query))
                    } else {
                        result.data?.results?.let { searchItems ->
                            searchResults.clear()
                            searchResults.addAll(searchItems.filter { it.mediaType != "person" })
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
                is DataHolder.Loading -> {
                    setLoading(true)
                    setError(null)
                }
                is DataHolder.Failure -> {
                    setLoading(false)
                    clearAdapter()
                    setError(result.errorData.message)
                }
                else -> {
                    setLoading(false)
                    clearAdapter()
                    setError(getString(R.string.general_error))
                }
            }
        }
        viewModel.loadMoreResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is DataHolder.Success -> {
                    setLoadMore(false)
                    result.data?.results?.let { searchItems ->
                        val lastPos = searchResults.size - 1
                        searchResults.addAll(searchItems.filter { it.mediaType != "person" })
                        adapter.notifyItemRangeInserted(lastPos, searchItems.size)
                    }
                }
                is DataHolder.Loading -> {
                    setLoadMore(true)
                }
                is DataHolder.Failure -> {
                    setLoadMore(false)
                    Snackbar.make(binding.loadMoreBar, result.errorData.message, Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    setLoadMore(false)
                    Snackbar.make(binding.loadMoreBar, getString(R.string.general_error), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        adapter = SearchResultAdapter(searchResults) { searchItem ->
            val mediaType = MediaViewModel.Companion.MediaType.values().find { it.type == searchItem.mediaType }
            mediaType?.let {
                val action = SearchFragmentDirections.actionSearchFragmentToMediaDetailFragment(searchItem.id, it)
                findNavController().safeNavigate(action)
            }
        }
        binding.userList.adapter = adapter
        binding.userList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 5) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun doSearch(v: View) {
        val query = binding.input.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        viewModel.setQuery(query)
        viewModel.searchMedia()
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