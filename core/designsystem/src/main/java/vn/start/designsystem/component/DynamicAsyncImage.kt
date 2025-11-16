//package vn.start.designsystem.component
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.size
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.graphics.painter.Painter
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalInspectionMode
//import androidx.compose.ui.unit.dp
//import coil3.compose.AsyncImage
//import coil3.compose.AsyncImagePainter
//import coil3.request.ImageRequest
//import core.networking.getSharedImageLoader
//import vn.start.designsystem.theme.LocalTintTheme
//
//@Composable
//fun DynamicAsyncImage(
//    imageUrl: String,
//    contentDescription: String?,
//    modifier: Modifier = Modifier,
//    placeholder: Painter? = null,
//    contentScale: ContentScale = ContentScale.Crop
//) {
//    val iconTint = LocalTintTheme.current.iconTint
//    var isLoading by remember { mutableStateOf(true) }
//    var isError by remember { mutableStateOf(false) }
//    val isPreview = LocalInspectionMode.current
//
//    val imageLoader = getSharedImageLoader()
//
//    Box(
//        modifier = modifier,
//        contentAlignment = Alignment.Center
//    ) {
//        AsyncImage(
//            model = ImageRequest.Builder()
//                .data(imageUrl)
//                .build(),
//            contentDescription = contentDescription,
//            imageLoader = imageLoader,
//            contentScale = contentScale,
//            onState = { state ->
//                isLoading = state is AsyncImagePainter.State.Loading
//                isError = state is AsyncImagePainter.State.Error
//            },
//            modifier = modifier
//        )
//
//        when {
//            isLoading && !isPreview -> {
//                CircularProgressIndicator(
//                    modifier = Modifier.size(40.dp),
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//
//            isError && placeholder != null -> {
//                Image(
//                    painter = placeholder,
//                    contentDescription = contentDescription,
//                    contentScale = contentScale,
//                    colorFilter = if (iconTint != Color.Unspecified)
//                        ColorFilter.tint(iconTint)
//                    else null,
//                )
//            }
//        }
//    }
//}
