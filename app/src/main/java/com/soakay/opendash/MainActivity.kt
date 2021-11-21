package com.soakay.opendash

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.soakay.opendash.databinding.ActivityMainBinding
import android.widget.Toast

import android.content.Intent
import android.view.View

import android.app.Activity
import android.util.Log
import android.widget.TextView
import org.w3c.dom.Text
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {

    private val FILE_SELECT_CODE = 0;


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var textFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun showFileChooser(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "text/plain"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a File to Upload"),
                FILE_SELECT_CODE
            )
        } catch (ex: ActivityNotFoundException) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(
                this, "Please install a File Manager.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun makeCall(view: View) {
        var nameBox = findViewById<TextView>(R.id.nameBox);
        var contextBox = findViewById<TextView>(R.id.contextBox)
        if(nameBox.length() == 0 || contextBox.length() == 0) {
            Toast.makeText(
                this,
                "Please fill in both boxes with valid information!",
                Toast.LENGTH_SHORT
            ).show()
            return;
        }

        Toast.makeText(
            this,
            "Making a Call!",
            Toast.LENGTH_SHORT
        ).show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("MainActivity", "PRINTING")
        if (requestCode == FILE_SELECT_CODE) { // Step 1: When a result has been received, check if it is the result for READ_IN_FILE
            if (resultCode == Activity.RESULT_OK) { // Step 2: Check if the operation to retrieve thea ctivity's result is successful
                var builder: StringBuilder = StringBuilder();
                try {
                    data?.data?.let {
                        contentResolver.openInputStream(it)
                    }?.let {
                        val r = BufferedReader(InputStreamReader(it))
                        while (true) {
                            val line: String? = r.readLine() ?: break
                            builder.append(line).append("\n")
                        }
                    }
                    findViewById<TextView>(R.id.contextBox).text = builder.toString();

                } catch (e: Exception) { // If the app failed to attempt to retrieve the error file, throw an error alert
                    Toast.makeText(
                        this,
                        "Sorry, but there was an error reading in the file",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}