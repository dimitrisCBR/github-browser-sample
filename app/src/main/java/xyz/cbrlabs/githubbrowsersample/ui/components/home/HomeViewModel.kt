package xyz.cbrlabs.githubbrowsersample.ui.components.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import xyz.cbrlabs.githubbrowsersample.data.GithubRepository
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo
import xyz.cbrlabs.githubbrowsersample.ui.Result
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    private val _mutableStateFlow: MutableStateFlow<HomeState> = MutableStateFlow(HomeState())
    val stateFlow: StateFlow<HomeState> = _mutableStateFlow.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery.asStateFlow()

    val reposFlow: StateFlow<List<Repo>> = _searchQuery
        .debounce(330)
        .filter { it.isNotEmpty() }
        .flatMapLatest { query ->
            createRepoFlow(query)
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private suspend fun createRepoFlow(query: String): Flow<List<Repo>> {
        val nextPage = stateFlow.value.nextPage
        _mutableStateFlow.emit(
            _mutableStateFlow.value.copy(isRefreshing = true)
        )
        githubRepository.searchGithubRepos(query, nextPage)
        _mutableStateFlow.emit(
            _mutableStateFlow.value.copy(
                nextPage = _mutableStateFlow.value.nextPage + 1,
                isRefreshing = false
            )
        )
        return githubRepository.searchReposFlow(query)
    }


    fun onSearchTermChanged(searchTerm: String) {
        _searchQuery.value = searchTerm
    }

    fun loadNextPage() {
        viewModelScope.launch(Dispatchers.IO) {
            _mutableStateFlow.emit(
                _mutableStateFlow.value.copy(isLoadingNextPage = true)
            )
            val currentState = _mutableStateFlow.value
            val result = githubRepository.searchGithubRepos(_searchQuery.value, currentState.nextPage)

            if(result is Result.Error) {
                _mutableStateFlow.emit(
                    _mutableStateFlow.value.copy(
                        error = result.error,
                        isRefreshing = false,
                        isLoadingNextPage = false,
                    )
                )
            } else {
                _mutableStateFlow.emit(
                    _mutableStateFlow.value.copy(
                        isRefreshing = false,
                        isLoadingNextPage = false,
                        nextPage = currentState.nextPage + 1
                    )
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _mutableStateFlow.emit(
                _mutableStateFlow.value.copy(
                    isLoadingNextPage = true,
                    isRefreshing = true,
                    finishedLoading = false,
                    error = null,
                    nextPage = 0
                )
            )
            loadNextPage()
        }
    }
}

data class HomeState(
    val isLoadingNextPage: Boolean = false,
    val isRefreshing: Boolean = false,
    val finishedLoading: Boolean = false,
    val error: Throwable? = null,
    val nextPage: Int = 0
)
