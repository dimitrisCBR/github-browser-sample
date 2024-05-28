package xyz.cbrlabs.githubbrowsersample.ui.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.distinctUntilChanged
import xyz.cbrlabs.githubbrowsersample.ui.components.common.ErrorScreen
import xyz.cbrlabs.githubbrowsersample.ui.components.common.Loader
import xyz.cbrlabs.githubbrowsersample.ui.components.common.PullToRefreshLazyColumn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel(),
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()

    val searchQuery by homeViewModel.searchQuery.collectAsState()
    val repos by homeViewModel.reposFlow.collectAsState()

    val uiState by homeViewModel.stateFlow.collectAsState()
    Scaffold(topBar = {
        Surface(
            modifier = Modifier, shadowElevation = 4.dp
        ) {
            androidx.compose.material3.SearchBar(query = searchQuery, onQueryChange = {
                homeViewModel.onSearchTermChanged(it)
            }, onSearch = {
                homeViewModel.loadNextPage()
            }, active = false, onActiveChange = {

            }, placeholder = { Text("Search Github Repos") }, shape = RoundedCornerShape(8.dp), leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = "Search"
                )
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {

            }
        }
    }, bottomBar = {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoadingNextPage) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter) // Align the loader to the bottom
                        .height(4.dp) // Adjust height of the loader as needed
                )
            }
        }
    }, snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentPadding.calculateTopPadding() - 8.dp)
        ) {
            if (uiState.isRefreshing && repos.isEmpty()) {
                Loader()
            } else if (uiState.error != null && repos.isEmpty()) {
                ErrorScreen("Something went wrong", uiState.error?.message ?: "Unknown Error", onRetryClick = {
                    searchQuery.takeIf { it.isNotEmpty() }?.let {
                        homeViewModel.loadNextPage()
                    }
                })
            } else {
                PullToRefreshLazyColumn(
                    items = repos,
                    content = { repo ->
                        RepoItem(repo = repo, onItemClicked = {
                            navController.navigate(
                                "detail/${it.owner.name}/${it.name}"
                            )
                        })
                    },
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { homeViewModel.refresh() },
                    lazyListState = lazyListState
                )
            }
            if (uiState.error != null) {
                LaunchedEffect(uiState.error) {
                    val result = snackbarHostState.showSnackbar(
                        message = uiState.error?.message ?: "Something went wrong",
                        actionLabel = "Retry",
                        duration = SnackbarDuration.Long
                    )
                    when (result) {
                        SnackbarResult.ActionPerformed -> {
                            searchQuery.takeIf { it.isNotEmpty() }?.let {
                                homeViewModel.loadNextPage()
                            }

                        }

                        SnackbarResult.Dismissed -> {/* Handle snackbar dismissed */
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset }.distinctUntilChanged().collect {
            if (lazyListState.layoutInfo.visibleItemsInfo.any { it.index == repos.size - 1 }) {
                // Load more items if scrolled to the bottom
                if (!uiState.finishedLoading && !uiState.isLoadingNextPage && !uiState.isRefreshing) {
                    homeViewModel.loadNextPage()
                }
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}