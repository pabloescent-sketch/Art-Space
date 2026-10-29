package com.example.artspace

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ArtSpaceApp()
                }
            }
        }
    }
}

@Composable
fun ArtSpaceApp() {
    // 1. Colección de obras de arte
    val artworks = listOf(
        Artwork(
            id = 1,
            imageRes = R.drawable.artwork_1,
            title = "Noche estrellada",
            artist = "Vincent van Gogh",
            year = "1889"
        ),
        Artwork(
            id = 2,
            imageRes = R.drawable.artwork_2,
            title = "Mona Lisa",
            artist = "Leonardo da Vinci",
            year = "1506"
        ),
        Artwork(
            id = 3,
            imageRes = R.drawable.artwork_3,
            title = "Guernica",
            artist = "Pablo Picasso",
            year = "1973"
        )
    )

    // 2. Estado para la obra actual (índice de la lista)
    var currentIndex by remember { mutableIntStateOf(0) }
    val currentArtwork = artworks[currentIndex]

    // 3. Funciones para la lógica de navegación
    val onPreviousClick: () -> Unit = {
        currentIndex = if (currentIndex == 0) artworks.size - 1 else currentIndex - 1
    }

    val onNextClick: () -> Unit = {
        currentIndex = if (currentIndex == artworks.size - 1) 0 else currentIndex + 1
    }

    // 4. Detección de la orientación de pantalla
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // 5. Layout condicional según orientación
    if (isLandscape) {
        ArtSpaceLandscapeLayout(
            artwork = currentArtwork,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick
        )
    } else {
        ArtSpacePortraitLayout(
            artwork = currentArtwork,
            onPreviousClick = onPreviousClick,
            onNextClick = onNextClick
        )
    }
}

@Composable
fun ArtSpacePortraitLayout(
    artwork: Artwork,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Sección A: Cuadro de la imagen
        ArtworkWall(
            imageRes = artwork.imageRes,
            contentDescription = artwork.title,
            modifier = Modifier
                .weight(1f, fill = false)
                .padding(bottom = 24.dp)
        )

        // Contenedor inferior para información y botones
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Sección B: Título, Artista y Año
            ArtworkDescriptor(
                title = artwork.title,
                artist = artwork.artist,
                year = artwork.year
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Sección C: Controles de navegación
            ArtworkController(
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick
            )
        }
    }
}

@Composable
fun ArtSpaceLandscapeLayout(
    artwork: Artwork,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Lado Izquierdo: Imagen
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            ArtworkWall(
                imageRes = artwork.imageRes,
                contentDescription = artwork.title
            )
        }

        // Lado Derecho: Descripción y Botones
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ArtworkDescriptor(
                title = artwork.title,
                artist = artwork.artist,
                year = artwork.year
            )

            Spacer(modifier = Modifier.height(32.dp))

            ArtworkController(
                onPreviousClick = onPreviousClick,
                onNextClick = onNextClick
            )
        }
    }
}

@Composable
fun ArtworkWall(
    imageRes: Int,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .shadow(12.dp, shape = RoundedCornerShape(4.dp)),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            modifier = Modifier
                .padding(32.dp)
                .wrapContentSize()
        )
    }
}

@Composable
fun ArtworkDescriptor(
    title: String,
    artist: String,
    year: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(0.9f),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Text(
                    text = artist,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = " ($year)",
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ArtworkController(
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(0.9f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = onPreviousClick,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(text = "Previous")
        }

        Button(
            onClick = onNextClick,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            Text(text = "Next")
        }
    }
}

// Previsualizaciones para el editor
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ArtSpacePortraitPreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=1280dp,height=800dp,dpi=240"
)
@Composable
fun ArtSpaceLandscapePreview() {
    ArtSpaceTheme {
        ArtSpaceApp()
    }
}