package id.zelory.compressor.sample

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import id.zelory.compressor.sample.MainActivity.actualImage
import id.zelory.compressor.sample.MainActivity.actualImageView
import id.zelory.compressor.sample.common.ScalingLogic
import id.zelory.compressor.sample.common.calculateSampleSize
import id.zelory.compressor.sample.common.compressImageFile
import id.zelory.compressor.sample.common.getBitmapFromUri
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.compressor.imagecompress.R
import java.io.File
import java.io.IOException
import java.text.DecimalFormat
import java.util.*

public class ChooseIntent(context: MainActivity, activity: MainActivity) {

    val context:Context? = context;
    val activity = activity

    private var imgPath: String = ""
    private var imageUri: Uri? = null
    private var queryImageUrl: String = ""



   public fun getPickImageIntent(): Intent? {
        var chooserIntent: Intent? = null

        var intentList: MutableList<Intent> = ArrayList()

        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri())

        intentList = addIntentsToList(context!!, intentList, pickIntent)
        intentList = addIntentsToList(context!!, intentList, takePhotoIntent)

        if (intentList.size > 0) {
            chooserIntent = Intent.createChooser(
                    intentList.removeAt(intentList.size - 1),
                    context!!.getString(R.string.select_capture_image)
            )
            chooserIntent!!.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    intentList.toTypedArray<Parcelable>()
            )
        }

        return chooserIntent
    }
     fun addIntentsToList(
             context: Context,
             list: MutableList<Intent>,
             intent: Intent
     ): MutableList<Intent> {
        val resInfo = context.packageManager.queryIntentActivities(intent, 0)
        for (resolveInfo in resInfo) {
            val packageName = resolveInfo.activityInfo.packageName
            val targetedIntent = Intent(intent)
            targetedIntent.setPackage(packageName)
            list.add(targetedIntent)
        }
        return list
    }

     fun setImageUri(): Uri {
        val folder = File("${context!!.getExternalFilesDir(Environment.DIRECTORY_DCIM)}")
        folder.mkdirs()

        val file = File(folder, "Image_Tmp.jpg")
        if (file.exists())
            file.delete()
        file.createNewFile()
        imageUri = FileProvider.getUriForFile(
                context!!,
                context!!.getString(R.string.content_provide_path),
                file
        )
        imgPath = file.absolutePath
        return imageUri!!
    }

    public fun handleImageRequest(data: Intent?) : Uri? {
        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            t.printStackTrace()
            //progressBar.visibility = View.GONE
            Toast.makeText(
                    context,
                    t.localizedMessage ?: context!!.getString(R.string.some_err),
                    Toast.LENGTH_SHORT
            ).show()
        }

        GlobalScope.launch(Dispatchers.Main + exceptionHandler) {
           // progressBar.visibility = View.VISIBLE

            if (data?.data != null) {     //Photo from gallery
                imageUri = data.data
                queryImageUrl = imageUri?.path!!

                queryImageUrl = activity.compressImageFile(queryImageUrl, false, imageUri!!)
            } else {
                queryImageUrl = imgPath ?: ""
                activity.compressImageFile(queryImageUrl, uri = imageUri!!)
            }
            imageUri = Uri.fromFile(File(queryImageUrl))

            if (queryImageUrl.isNotEmpty()) {

                /*Toast.makeText(
                        context,
                        queryImageUrl,
                        Toast.LENGTH_SHORT
                ).show()*/

                try {
                    actualImage = FileUtil.from(context, imageUri)
                    actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage.absolutePath))
                    MainActivity.actualSizeTextView.text = String.format("Size : %s", getReadableFileSize(actualImage.length()))
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                Glide.with(activity)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .load(queryImageUrl)
                        .into(actualImageView)
            }
            //progressBar.visibility = View.GONE
        }
        return imageUri;
    }
    fun getReadableFileSize(size: Long): String? {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        val value = (size / Math.pow(1024.0, digitGroups.toDouble())).toInt()
        return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
    }

    fun getFileSize(size: Long): Int {
        if (size <= 0) {
            return 0
        }
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        //return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
        return (size / Math.pow(1024.0, digitGroups.toDouble())).toInt()
    }
   public fun decodeFileData(
           context: Context,
           uri: Uri,
           dstWidth: Int,
           dstHeight: Int,
           scalingLogic: ScalingLogic
   ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        context.getBitmapFromUri(uri, options)
        options.inJustDecodeBounds = false

        options.inSampleSize = calculateSampleSize(
                options.outWidth,
                options.outHeight,
                dstWidth,
                dstHeight,
                scalingLogic
        )

        return context.getBitmapFromUri(uri, options)
    }
    @Throws(IOException::class)
    fun getBitmapFromUri(uri: Uri, options: BitmapFactory.Options? = null): Bitmap? {
        val parcelFileDescriptor = context!!.contentResolver.openFileDescriptor(uri, "r")
        val fileDescriptor = parcelFileDescriptor?.fileDescriptor
        val image: Bitmap? = if (options != null)
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
        else
            BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor?.close()
        return image
    }


}