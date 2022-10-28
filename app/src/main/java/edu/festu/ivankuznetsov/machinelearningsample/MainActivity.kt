package edu.festu.ivankuznetsov.machinelearningsample

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import edu.festu.ivankuznetsov.machinelearningsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var current: Boolean = true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.switchButton.setOnClickListener {
            current = !current
        }
        val cameraContract = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ activityResult ->
            if(activityResult.resultCode == RESULT_OK) {
                val image = activityResult?.data?.extras?.get("data") as Bitmap
                val labeler:ImageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
                val inputImage = InputImage.fromBitmap(image,0)
                val recognizer:TextRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                binding.imageView.setImageBitmap(image)

                 if (current) {
                    val task = recognizer.process(inputImage)
                     task.addOnSuccessListener {
                         Toast.makeText(this,it.text,Toast.LENGTH_SHORT).show()
                     }
                }
                 else {
                     val task = labeler.process(inputImage)
                     task.addOnSuccessListener {

                         Toast.makeText(this, it.first().text, Toast.LENGTH_SHORT).show()
                     }
                 }


            }
        }
        binding.getImage.setOnClickListener {
            cameraContract.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        }
    }

    companion object{
        private val TAG  = MainActivity::class.java.simpleName
    }
}