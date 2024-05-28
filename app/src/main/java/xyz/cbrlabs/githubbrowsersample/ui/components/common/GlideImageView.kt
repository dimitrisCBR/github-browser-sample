package xyz.cbrlabs.githubbrowsersample.ui.components.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GlideImageView(url: String, contentDescription: String, modifier: Modifier = Modifier) {
    GlideImage(
        model = url,
        contentDescription = contentDescription,
        loading = placeholder(ColorPainter(MaterialTheme.colorScheme.secondary)),
        modifier = modifier,
    )

}