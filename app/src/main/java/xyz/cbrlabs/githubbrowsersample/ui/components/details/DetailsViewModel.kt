package xyz.cbrlabs.githubbrowsersample.ui.components.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xyz.cbrlabs.githubbrowsersample.data.GithubRepository
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubRepoItem
import xyz.cbrlabs.githubbrowsersample.ui.Result
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val githubRepository: GithubRepository
) : ViewModel() {

    private val _mutableStateFlow: MutableStateFlow<DetailState> = MutableStateFlow(DetailState())
    val stateFlow: StateFlow<DetailState> = _mutableStateFlow

    fun loadGithubRepo(repoOwner: String, repoName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _mutableStateFlow.value = _mutableStateFlow.value.copy(isLoading = true)
            try {
                val result = githubRepository.loadRepo(repoOwner, repoName)
                _mutableStateFlow.emit(
                    _mutableStateFlow.value.copy(isLoading = false, repo = result)
                )
            } catch (t : Throwable) {
                _mutableStateFlow.emit(
                    _mutableStateFlow.value.copy(isLoading = false, error = t)
                )
            }
        }
    }

}

data class DetailState(
    val isLoading: Boolean = false,
    val repo: GithubRepoItem? = null,
    val error: Throwable? = null,
)
