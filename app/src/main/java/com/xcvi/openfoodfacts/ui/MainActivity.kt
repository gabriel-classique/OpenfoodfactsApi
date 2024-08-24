package com.xcvi.openfoodfacts.ui

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.xcvi.openfoodfacts.ui.theme.OpenFoodFactsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenFoodFactsTheme {
                val viewModel: FoodViewModel = hiltViewModel()
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            MainScreen(
                                onSuccess = {
                                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT)
                                        .show()
                                    viewModel.catch(it,this@MainActivity)
                                    navController.navigate("food_screen")

                                },
                                onText = {
                                    navController.navigate("search_screen")
                                }
                            )
                        }
                        composable("food_screen") {
                            FoodScreen(viewModel)
                        }
                        composable("search_screen") {
                            SearchScreen(viewModel = viewModel, this@MainActivity)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(viewModel: FoodViewModel, context: Context) {
    var foodName by remember {
        mutableStateOf("")
    }
    val state = viewModel.state.collectAsState().value
    var page by remember {
        mutableStateOf(1)
    }
    LazyColumn {
        item {
            Row {
                TextField(
                    modifier = Modifier.weight(1f),
                    value = foodName, onValueChange = {
                        foodName = it
                    }
                )
                Button(
                    onClick = {
                        page = 1
                        viewModel.search(foodName, page = page, context)
                    }
                ) {
                    Text(text = "Search")
                }
            }
        }

        if(state.isLoading){
            item {
                Box(modifier = Modifier.fillMaxSize()){
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
            }
        } else {
            state.searchResult.forEach {
                item {
                    Column {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(4.dp)) {
                                Text(
                                    text = it.name,
                                    fontSize = MaterialTheme.typography.headlineLarge.fontSize
                                )
                                Text(
                                    text = "Calories per 100g: " + it.calories,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                                Text(
                                    text = "Carbohydrates: " + it.carbs,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                                Text(
                                    text = "Fat: " + it.fats,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                                Text(
                                    text = "Protein: " + it.protein,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        if(state.searchResult.isNotEmpty() && !state.isLoading){
            item {
                Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = {
                            page++
                            viewModel.search(foodName, page = page, context)
                        }
                    ) {
                        Text(text = "Next Page")
                    }
                }
            }
        }
    }


}

@Composable
fun FoodScreen(viewModel: FoodViewModel) {
    val state = viewModel.state.collectAsState().value
    state.currentFood?.let {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = it.name, fontSize = MaterialTheme.typography.headlineLarge.fontSize)
            Text(
                text = "Calories per 100g: " + it.calories,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Text(
                text = "Carbohydrates: " + it.carbs,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
            Text(text = "Fat: " + it.fats, fontSize = MaterialTheme.typography.bodyLarge.fontSize)
            Text(
                text = "Protein: " + it.protein,
                fontSize = MaterialTheme.typography.bodyLarge.fontSize
            )
        }
    }
}

@ExperimentalPermissionsApi
@Composable
fun MainScreen(onSuccess: (String) -> Unit, onText: () -> Unit) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var analyzerType by remember { mutableStateOf(AnalyzerType.UNDEFINED) }

    if (cameraPermissionState.status.isGranted) {
        if (analyzerType == AnalyzerType.UNDEFINED) {
            Column {
                Button(onClick = { analyzerType = AnalyzerType.BARCODE }) {
                    Text(text = "BARCODE")
                }
                Button(
                    onClick = {
                        onText()
                        //analyzerType = AnalyzerType.TEXT
                    }
                ) {
                    Text(text = "TEXT")
                }
            }
        } else {
            CameraScreen(analyzerType, onSuccess)
        }
    } else if (cameraPermissionState.status.shouldShowRationale) {
        Text("Camera Permission permanently denied")
    } else {
        SideEffect {
            cameraPermissionState.run { launchPermissionRequest() }
        }
        Text("No Camera Permission")
    }
}

@Composable
fun CameraScreen(analyzerType: AnalyzerType, onSuccess: (String) -> Unit) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val previewView = PreviewView(context)
            val preview = Preview.Builder().build()
            val selector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()

            preview.setSurfaceProvider(previewView.surfaceProvider)

            val imageAnalysis = ImageAnalysis.Builder().build()
            imageAnalysis.setAnalyzer(
                ContextCompat.getMainExecutor(context),
                if (analyzerType == AnalyzerType.BARCODE) {
                    BarcodeAnalyzer {
                        onSuccess(it)
                    }
                } else {
                    TextAnalyzer(context)
                }
            )

            runCatching {
                cameraProviderFuture.get().bindToLifecycle(
                    lifecycleOwner,
                    selector,
                    preview,
                    imageAnalysis
                )
            }.onFailure {
                Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
            }
            previewView
        }
    )
}