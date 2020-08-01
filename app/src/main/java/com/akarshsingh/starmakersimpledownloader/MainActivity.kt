package com.akarshsingh.starmakersimpledownloader

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTitle("SMSD")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize UI elements
        val songURL_editText : EditText = findViewById(R.id.SongURL_EditText)
        val songName_editText : EditText = findViewById(R.id.SongName_EditText)
        val isMP3_radioButton : RadioButton = findViewById(R.id.mp3_radioButton)
        val download_button : Button = findViewById(R.id.download_button)
        val fileExplorer_button : Button= findViewById(R.id.file_explorer_button)

        //handle URL intent from star maker app
        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    val starmaker_shareString = intent.getStringExtra(Intent.EXTRA_TEXT)
                    val url= "https"+starmaker_shareString.toString().substringAfter("https")
                    songURL_editText.setText(url)
                }
            }
            //else -> {
                // Handle other intents, such as being started from the home screen
        }


        //download button click
        download_button.setOnClickListener {
            val downloadSongManager = DownloadSongManager(songURL_editText.text.toString(),songName_editText.text.toString())
            downloadSongManager.downloadFile(this,downloadSongManager.getValidURL(this),isMP3_radioButton.isChecked)
        }

        //file explorer button logic
        val fileLocation = Environment.DIRECTORY_DOWNLOADS.toString()
        val uri = Uri.parse(fileLocation)
        fileExplorer_button.setOnClickListener {
            val fileExplorerIntent = Intent(Intent.ACTION_VIEW)
            //intent.addCategory(Intent.CATEGORY_OPENABLE)
            fileExplorerIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            fileExplorerIntent.setDataAndType(uri,"*/*")
            try {
                startActivity(fileExplorerIntent)
            }
            catch (e : ActivityNotFoundException)
            {
                Toast.makeText(this,"Please install a File Manager",Toast.LENGTH_LONG).show()
            }

        }
    }



}
