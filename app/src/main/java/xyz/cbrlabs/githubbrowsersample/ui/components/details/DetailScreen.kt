package xyz.cbrlabs.githubbrowsersample.ui.components.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import xyz.cbrlabs.githubbrowsersample.domain.api.model.GithubOwner
import xyz.cbrlabs.githubbrowsersample.ui.components.common.ErrorScreen
import xyz.cbrlabs.githubbrowsersample.ui.components.common.GlideImageView
import xyz.cbrlabs.githubbrowsersample.ui.components.common.Loader


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavHostController,
    repoOwner: String,
    repoName: String,
    detailsViewModel: DetailsViewModel = hiltViewModel(),
) {
    val uiState by detailsViewModel.stateFlow.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                text = repoName,
                style = MaterialTheme.typography.headlineMedium,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    }) { contentPadding ->
        Surface(
            color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = contentPadding.calculateTopPadding(), horizontal = 8.dp)
                    .fillMaxSize()
            ) {
                if (uiState.isLoading) {
                    Loader()
                } else if (uiState.error != null) {
                    ErrorScreen(
                        bigText = "Something went wrong", smallText = uiState.error?.message ?: "Unknown Error"
                    ) {

                    }
                } else if (uiState.repo != null) {
                    val repo = uiState.repo!!

                    HorizontalDivider(modifier = Modifier.padding(bottom = 16.dp))
                    // License Section
                    repo.license?.let { license ->
                        SectionTitle("License")
                        Text(
                            text = license.name,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                    }

                    // Owner Info Section
                    SectionTitle("Owner Info")
                    OwnerInfo(owner = repo.owner)

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    // Description Section
                    SectionTitle("Description")
                    Text(
                        text = repo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Details Section
                    SectionTitle("Details")
                    repo.language?.let {
                        DetailsItem("Language", it, Icons.Default.Info)
                    }

                    DetailsItem("Stars", repo.stargazersCount.toString(), Icons.Default.Star)
                    DetailsItem("Watchers", repo.watchersCount.toString(), Icons.Default.Person)
                    DetailsItem("Forks", repo.forksCount.toString(), Icons.Default.Share)
                    DetailsItem("Open Issues", repo.openIssuesCount.toString(), Icons.Default.Info)
                }


                // Other details can be added similarly
            }
        }

    }
    LaunchedEffect(Unit) {
        detailsViewModel.loadGithubRepo(repoOwner, repoName)
    }
}

@Composable
fun OwnerInfo(owner: GithubOwner) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp,
            modifier = Modifier.size(48.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            GlideImageView(
                url = owner.avatarUrl, contentDescription = "User avatar", modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = owner.name, style = MaterialTheme.typography.bodyLarge, fontSize = 16.sp
            )
            Text(
                text = owner.url, style = MaterialTheme.typography.bodyMedium, fontSize = 12.sp
            )
        }
    }
}


@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun DetailsItem(title: String, value: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            Text(
                text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold
            )
        }
    }
}