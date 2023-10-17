package com.CameraX.camerax.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.CameraX.camerax.R
import java.io.File
import java.io.FileOutputStream

class PDFActivity : AppCompatActivity() {
    private var imageView: ImageView? = null
    var pageHeight = 1120
    var pageWidth = 792
    var bitmap :Bitmap?=null
    var scaledbmp :Bitmap?=null
    private var isActivityDestroyed = false

    var PERMISSION_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var generatePDFBtn: Button
        isActivityDestroyed = false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfactivity)
        imageView = findViewById(R.id.imageviewPDf)

        val filepath = intent.getStringExtra("FilePath")
        if(!filepath.isNullOrBlank())
        {
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.horizontaldummypic)
           // bitmap = BitmapFactory.decodeFile(filepath)//converts filepath back to bitmap
            imageView?.setImageBitmap(bitmap)
            bitmap?.let {
                scaledbmp = Bitmap.createScaledBitmap(it, 140, 140, false)
            }        }
        generatePDFBtn = findViewById(R.id.idBtnGeneratePdf)
        if (checkPermissions()) {
            // if permission is granted we are displaying a toast message.
            Toast.makeText(this, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            // if the permission is not granted
            // we are calling request permission method.
            requestPermission()
        }
        generatePDFBtn.setOnClickListener {

                generatePDF()

        }

    }
    override fun onDestroy() {
        super.onDestroy()
        isActivityDestroyed = true
    }
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun generatePDF(){
        var pdfPictureDocument: PdfDocument = PdfDocument()
        var paint: Paint = Paint()
        var title: Paint = Paint()

        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        var myPage: PdfDocument.Page = pdfPictureDocument.startPage(myPageInfo)
        var canvas: Canvas = myPage.canvas

        scaledbmp?.let { canvas.drawBitmap(it, 56F, 40F, paint) }

        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        title.textSize = 15F
        title.setColor(ContextCompat.getColor(this, R.color.purple_200))

        // and then we are passing our variable of paint which is title.
        canvas.drawText("Harbour master barcode scan report", 209F, 100F, title)
        canvas.drawText("Cargo Track", 209F, 80F, title)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(this, R.color.purple_200))
        title.textSize = 15F
        title.textAlign = Paint.Align.CENTER
        canvas.drawText("Harbour master report.", 396F, 560F, title)
        // PDF file we will be finishing our page.
        pdfPictureDocument.finishPage(myPage)
        val resolver = contentResolver
       /* val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "GFG.pdf")
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
*/
        // PDF file and its path.
        val file: File = File(getExternalFilesDir(null), "GFG.pdf")
        try {

            pdfPictureDocument.writeTo(FileOutputStream(file))


            if (!isActivityDestroyed) {
                Toast.makeText(applicationContext, "PDF file generated..", Toast.LENGTH_SHORT).show()
            }        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Fail to generate PDF file: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        pdfPictureDocument.close()
    }
    fun checkPermissions(): Boolean {

        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), PERMISSION_CODE
        )
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_CODE) {

            if (grantResults.size > 0) {

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {


                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

}