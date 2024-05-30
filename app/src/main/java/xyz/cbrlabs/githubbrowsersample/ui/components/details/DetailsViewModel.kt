package xyz.cbrlabs.githubbrowsersample.ui.components.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xyz.cbrlabs.githubbrowsersample.commons.Result
import xyz.cbrlabs.githubbrowsersample.domain.data.GithubRepository
import xyz.cbrlabs.githubbrowsersample.domain.model.Repo
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
            val result = githubRepository.loadRepo(repoOwner, repoName)
            if (result is Result.Success) {
                _mutableStateFlow.emit(
                    _mutableStateFlow.value.copy(isLoading = false, repo = result.data, error = null)
                )
            } else if (result is Result.Error) {
                _mutableStateFlow.emit(
                    _mutableStateFlow.value.copy(isLoading = false, repo = null, error = result.error)
                )
            }
        }
    }

}

data class DetailState(
    val isLoading: Boolean = false,
    val repo: Repo? = null,
    val error: Throwable? = null,
)
