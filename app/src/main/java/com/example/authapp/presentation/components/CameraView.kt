package com.example.authapp.presentation.components

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun CameraXCaptureScreen(
    onImageCaptured: (Uri) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1. Manejo de Permisos
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
            if (!granted) {
                onError("Permiso de cámara denegado.")
            }
        }
    )
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Configuración de CameraX
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    val previewView = remember { PreviewView(context) }
    val imageCapture: ImageCapture =
        remember { ImageCapture.Builder().build() }

    // Uso del Executor estándar
    val cameraExecutor: ExecutorService =
        remember { Executors.newSingleThreadExecutor() }

    if (hasCameraPermission) {
        LaunchedEffect(lensFacing) { // Se re-bind cuando cambia la cámara
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                // Definición del cameraSelector
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing)
                    .build()

                try {
                    cameraProvider.unbindAll() // Desvincular casos de uso anteriores
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture // Vincula el caso de uso de captura
                    )
                } catch (exc: Exception) {
                    Log.e("CameraX", "Error al vincular casos de uso: $exc")
                    onError("Error al iniciar la cámara: ${exc.message}")
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }

    // 3. Interfaz de Usuario
    Box(modifier = Modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            AndroidView({ previewView }, modifier = Modifier.fillMaxSize())
        } else {
            Text("Se necesita permiso de cámara.")
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (!hasCameraPermission) {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                        return@Button
                    }
                    // Implementa la lógica para tomar la foto
                    val photoFile = File(
                        context.externalMediaDirs.firstOrNull(),
                        "JPEG_${System.currentTimeMillis()}.jpg"
                    )

                    // CORRECCIÓN 3: Manejo seguro de la creación de directorios
                    photoFile.parentFile?.let { parent ->
                        if (!parent.exists()) {
                            parent.mkdirs()
                        }
                    }

                    val outputOptions =
                        ImageCapture.OutputFileOptions.Builder(photoFile)
                            .build()

                    imageCapture.takePicture(
                        outputOptions,
                        cameraExecutor, // Ejecutor para la devolución de llamada
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onError(exc: ImageCaptureException) {
                                Log.e("CameraX", "Error al guardar foto: ${exc.message}", exc)
                                onError("Error al guardar foto: ${exc.message}")
                            }

                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                val savedUri = output.savedUri ?: Uri.fromFile(photoFile)
                                Log.d("CameraX", "Foto guardada: $savedUri")
                                onImageCaptured(savedUri)
                            }
                        }
                    )
                },
                enabled = hasCameraPermission
            ) {
                Text("Tomar Foto")
            }

            Button(onClick = { // Botón para cambiar de cámara
                lensFacing =
                    if (lensFacing == CameraSelector.LENS_FACING_BACK) {
                        CameraSelector.LENS_FACING_FRONT
                    } else {
                        CameraSelector.LENS_FACING_BACK
                    }
            }, enabled = hasCameraPermission) {
                Text("Cambiar Cámara")
            }
        }
    }
}