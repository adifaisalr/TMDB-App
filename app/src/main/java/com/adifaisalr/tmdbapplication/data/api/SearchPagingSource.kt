package com.adifaisalr.tmdbapplication.data.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.adifaisalr.tmdbapplication.domain.model.SearchItem
import com.adifaisalr.tmdbapplication.domain.model.dataholder.DataHolder
import com.adifaisalr.tmdbapplication.domain.model.dataholder.ErrorData
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class SearchPagingSource(
    private val service: TmdbService,
    val keyword: String
) : PagingSource<Int, SearchItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.searchMedia(keyword, position)
            when (response) {
                is DataHolder.Success -> {
                    val searchResult = response.data.results
                    LoadResult.Page(
                        data = response.data.results,
                        prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                        nextKey = if (searchResult.isEmpty()) null else position + 1
                    )
                }
                else -> LoadResult.Error(ErrorData(response.peekError?.message ?: "", 0))
            }
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}