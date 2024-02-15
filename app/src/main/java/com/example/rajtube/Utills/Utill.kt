package com.example.rajtube.Utills

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun uploadImage(uri : Uri, folderName : String ,progressDialog: ProgressDialog, callback :(String?) -> Unit) {

    var imageUrl : String?  = null
    FirebaseStorage.getInstance()
        .getReference(folderName)
        .child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
                callback(imageUrl)
            }
        }
        .addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()

            progressDialog.setMessage("Uploaded $progress %")
            progressDialog.setCanceledOnTouchOutside(false)
        }

}

fun uploadVideo(context: Context ,uri : Uri, folderName : String ,progressDialog: ProgressDialog ,callback :(String?) -> Unit) {

    var imageUrl : String?  = null

        progressDialog.setTitle("Uploading . . .")
    progressDialog.show()

    FirebaseStorage.getInstance()
        .getReference(folderName)
        .child(UUID.randomUUID().toString())
        .putFile(uri)
        .addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl = it.toString()
               Toast.makeText(context ,
                   "The video is uploaded please add Video title and thumbnail " ,
                   Toast.LENGTH_LONG).show()
                progressDialog.dismiss()
                callback(imageUrl)
            }
        }
        .addOnProgressListener {

            val progress = (100.0 * it.bytesTransferred / it.totalByteCount).toInt()

            progressDialog.setMessage("Uploaded $progress %")
            progressDialog.setCanceledOnTouchOutside(false)
        }

}