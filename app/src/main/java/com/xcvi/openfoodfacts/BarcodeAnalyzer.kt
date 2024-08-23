package com.xcvi.openfoodfacts

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

enum class AnalyzerType {
    UNDEFINED, BARCODE, TEXT
}

class BarcodeAnalyzer(private val onSuccess: (String)-> Unit) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_EAN_13 )
        .build()

    private val scanner = BarcodeScanning.getClient(options)



    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { image ->
            scanner.process(
                InputImage.fromMediaImage(
                    image, imageProxy.imageInfo.rotationDegrees
                )
            ).addOnSuccessListener { barcode ->
                barcode?.takeIf { it.isNotEmpty() }
                    ?.mapNotNull { it.rawValue }
                    ?.joinToString(",")
                    ?.let {
                        onSuccess(it)
                    }
                imageProxy.close()
            }.addOnCompleteListener {
                imageProxy.close()
            }
        }
    }
}

class TextAnalyzer(private val context: Context) : ImageAnalysis.Analyzer {

    private val options = TextRecognizerOptions.DEFAULT_OPTIONS

    private val scanner = TextRecognition.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image
            ?.let { image ->
                scanner.process(
                    InputImage.fromMediaImage(
                        image, imageProxy.imageInfo.rotationDegrees
                    )
                ).addOnSuccessListener { results ->
                    results.textBlocks
                        .takeIf { it.isNotEmpty() }
                        ?.joinToString(",") { it.text }
                        ?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
                }.addOnCompleteListener {
                    imageProxy.close()
                }
            }
    }
}