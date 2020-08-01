package com.akarshsingh.starmakersimpledownloader

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.lang.Exception

class DownloadSongManager (private val SongURL : String, private val SongName : String)
{

    private fun isStoragePermissionGranted(context: Context) : Boolean
    {
        val checkReadStorageSelfPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        val checkWriteStorageSelfPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return (checkReadStorageSelfPermission and checkWriteStorageSelfPermission) == PackageManager.PERMISSION_GRANTED
    }

    fun getValidURL(context: Context) : String
    {
        var staticURL = ""
        try {
            if (SongURL.isEmpty() or SongURL.isBlank())
                Toast.makeText(context,"Please copy link from Star maker",Toast.LENGTH_SHORT).show()
            else
            {
                val downloadUri: Uri = Uri.parse(SongURL)
                val recordingId = downloadUri.getQueryParameter("recordingId")
                staticURL = "https://static.starmakerstudios.com/production/uploading/recordings/$recordingId/master.mp4"
            }
        }
        catch (e : Exception)
        {
            Toast.makeText(context,"URL not valid, exception: ",Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        return staticURL
    }

    fun downloadFile(context: Context,staticURL : String, isMP3Checked : Boolean)
    {
        if (isStoragePermissionGranted(context))
        {
            if (SongName.isEmpty() or SongName.isBlank())
            {
                Toast.makeText(context,"Please write Song Name",Toast.LENGTH_LONG).show()
                return
            }
            if (staticURL.isEmpty() or staticURL.isBlank())
                return
            else
            {
                val baseFolderForSongDownload: String = Environment.DIRECTORY_DOWNLOADS.toString()

                val songName = SongName
                val downloadUri: Uri = Uri.parse(staticURL.trim())
                val songFile : String = if (isMP3Checked) "$songName.mp3" else "$songName.mp4"

                //download process
                val req = DownloadManager.Request(downloadUri)
                req.setDestinationInExternalPublicDir(baseFolderForSongDownload, songFile)
                req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                req.setAllowedOverRoaming(true)
                req.setAllowedOverRoaming(true)
                val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue((req))
            }
        }
        else
            Toast.makeText(context,"Please allow Storage access",Toast.LENGTH_LONG).show()
    }
/*
    suspend fun connectURL(context: Context,strURL : String): URLConnection? {
        var connect : URLConnection? = null
        try {
            withContext(Dispatchers.IO) {
                val inputURL: URL = URL(strURL)
                connect = inputURL.openConnection()
            }
        }
        catch (e : MalformedURLException)
        {
            Toast.makeText(context,"Please provide valid link",Toast.LENGTH_SHORT).show()
        }
        catch (e : IOException)
        {
            Toast.makeText(context,"Cannot Connect to the provided Link",Toast.LENGTH_SHORT).show()
        }

        return connect
    }
*/

}