package com.example.test

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.speedmatch.databinding.ProgressBarBinding
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.StandardCopyOption


object Common {

    private const val BASE_IMAGE_PATH = ""
    private var dialog: Dialog? = null

    fun showProgressDialog(context: Context) {

        if (dialog == null) {
            dialog = Dialog(context)
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.setDimAmount(0.6F)
            dialog?.setCancelable(false)
            dialog?.setContentView(ProgressBarBinding.inflate(LayoutInflater.from(context)).root)
            dialog?.window?.setElevation(4F)
        }
        try {
            dialog?.show()
        } catch (e: WindowManager.BadTokenException) {
            dialog?.dismiss()
            dialog = null
//            showProgressDialog(context)
        } catch (e: Exception) {
            Log.d("Exception", "showProgressDialog: ${e.message}")
        }

    }

    fun dismissProgressDialog() {
        try {
            dialog?.dismiss()
            dialog = null
        } catch (ex: Exception) {
        }

    }

    fun String.showToast(context: Context) {
        Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
    }


    var mediaJSON = """{ "categories" : [ { "name" : "Movies",
        "videos" : [
        { "description" : "HBO GO now works with Chromecast -- the easiest way to enjoy online video on your TV. For when you want to settle into your Iron Throne to watch the latest episodes. For $35.\nLearn how to use Chromecast with HBO GO and more at google.com/chromecast.",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4" ],
            "subtitle" : "By Google",
            "thumb" : "images/ForBiggerBlazes.jpg",
            "title" : "For Bigger Blazes"
        },
        { "description" : "Introducing Chromecast. The easiest way to enjoy online video and music on your TV—for when Batman's escapes aren't quite big enough. For $35. Learn how to use Chromecast with Google Play Movies and more at google.com/chromecast.",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4" ],
            "subtitle" : "By Google",
            "thumb" : "images/ForBiggerEscapes.jpg",
            "title" : "For Bigger Escape"
        },
        { "description" : "Introducing Chromecast. The easiest way to enjoy online video and music on your TV. For $35.  Find out more at google.com/chromecast.",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4" ],
            "subtitle" : "By Google",
            "thumb" : "images/ForBiggerFun.jpg",
            "title" : "For Bigger Fun"
        },
        { "description" : "Introducing Chromecast. The easiest way to enjoy online video and music on your TV—for the times that call for bigger joyrides. For $35. Learn how to use Chromecast with YouTube and more at google.com/chromecast.",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4" ],
            "subtitle" : "By Google",
            "thumb" : "images/ForBiggerJoyrides.jpg",
            "title" : "For Bigger Joyrides"
        },
        { "description" :"Introducing Chromecast. The easiest way to enjoy online video and music on your TV—for when you want to make Buster's big meltdowns even bigger. For $35. Learn how to use Chromecast with Netflix and more at google.com/chromecast.",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4" ],
            "subtitle" : "By Google",
            "thumb" : "images/ForBiggerMeltdowns.jpg",
            "title" : "For Bigger Meltdowns"
        },
        { "description" : "Sintel is an independently produced short film, initiated by the Blender Foundation as a means to further improve and validate the free/open source 3D creation suite Blender. With initial funding provided by 1000s of donations via the internet community, it has again proven to be a viable development model for both open 3D technology as for independent animation film.\nThis 15 minute film has been realized in the studio of the Amsterdam Blender Institute, by an international team of artists and developers. In addition to that, several crucial technical and creative targets have been realized online, by developers and artists and teams all over the world.\nwww.sintel.org",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4" ],
            "subtitle" : "By Blender Foundation",
            "thumb" : "images/Sintel.jpg",
            "title" : "Sintel"
        },
        { "description" : "Smoking Tire takes the all-new Subaru Outback to the highest point we can find in hopes our customer-appreciation Balloon Launch will get some free T-shirts into the hands of our viewers.",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/SubaruOutbackOnStreetAndDirt.mp4" ],
            "subtitle" : "By Garage419",
            "thumb" : "images/SubaruOutbackOnStreetAndDirt.jpg",
            "title" : "Subaru Outback On Street And Dirt"
        },
        { "description" : "Tears of Steel was realized with crowd-funding by users of the open source 3D creation tool Blender. Target was to improve and test a complete open and free pipeline for visual effects in film - and to make a compelling sci-fi film in Amsterdam, the Netherlands.  The film itself, and all raw material used for making it, have been released under the Creatieve Commons 3.0 Attribution license. Visit the tearsofsteel.org website to find out more about this, or to purchase the 4-DVD box with a lot of extras.  (CC) Blender Foundation - http://www.tearsofsteel.org",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4" ],
            "subtitle" : "By Blender Foundation",
            "thumb" : "images/TearsOfSteel.jpg",
            "title" : "Tears of Steel"
        },
        { "description" : "The Smoking Tire heads out to Adams Motorsports Park in Riverside, CA to test the most requested car of 2010, the Volkswagen GTI. Will it beat the Mazdaspeed3's standard-setting lap time? Watch and see...",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4" ],
            "subtitle" : "By Garage419",
            "thumb" : "images/VolkswagenGTIReview.jpg",
            "title" : "Volkswagen GTI Review"
        },
        { "description" : "The Smoking Tire is going on the 2010 Bullrun Live Rally in a 2011 Shelby GT500, and posting a video from the road every single day! The only place to watch them is by subscribing to The Smoking Tire or watching at BlackMagicShine.com",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WeAreGoingOnBullrun.mp4" ],
            "subtitle" : "By Garage419",
            "thumb" : "images/WeAreGoingOnBullrun.jpg",
            "title" : "We Are Going On Bullrun"
        },
        { "description" : "The Smoking Tire meets up with Chris and Jorge from CarsForAGrand.com to see just how far $1,000 can go when looking for a car.The Smoking Tire meets up with Chris and Jorge from CarsForAGrand.com to see just how far $1,000 can go when looking for a car.",
            "sources" : [ "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/WhatCarCanYouGetForAGrand.mp4" ],
            "subtitle" : "By Garage419",
            "thumb" : "images/WhatCarCanYouGetForAGrand.jpg",
            "title" : "What care can you get for a grand?"
        }
        ]}]}"""


    fun ImageView.load(url: String) {
        if (url.startsWith("http")) Glide.with(this).load(url).into(this)
        else Glide.with(this).load(BASE_IMAGE_PATH + url).into(this)
    }

    fun ImageView.loadCircleCrop(url: String) {
        if (url.startsWith("http")) Glide.with(this).load(url).circleCrop().into(this)
        else Glide.with(this).load(BASE_IMAGE_PATH + url).circleCrop().into(this)
    }

    fun ImageView.loadRoundCorners(url: String, corners: Int = 12) {
        if (url.startsWith("http")) Glide.with(this).load(url).transform(RoundedCorners(corners))
            .into(this)
        else Glide.with(this).load(BASE_IMAGE_PATH + url).transform(RoundedCorners(corners))
            .into(this)
    }

    fun ImageView.loadRoundCorners(url: File, corners: Int = 12) {
        Glide.with(this).load(url).transform(RoundedCorners(corners)).into(this)
    }


    /**
     * File Handling
     * */


    val savePath get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/testApp")
    val hidePath get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/testApp/.hide")


    private val okHttpClient = OkHttpClient.Builder().build()

    fun getUsers(callback: (users: List<User>) -> Unit) {
        val request = Request.Builder().url("http://3.237.12.38:5000/api/v1/auth/getbyid")
            .method("POST", RequestBody.create(null,"")).build()
        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("downloadImage", "downloadImage: onFailure ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.d("downloadImage", "downloadImage: !response.isSuccessful")
                    return
                }
                response.body?.let { responseBody ->
                    val userResponse: UserResponse? =
                        Gson().fromJson(responseBody.string(), UserResponse::class.java)
                    userResponse?.data?.let { callback.invoke(it) }
                }
            }

        })
    }

    fun downloadImage(fileUrl: String, context: Context) {
        "downloading...".showToast(context)
        val request = Request.Builder().url(fileUrl).build()

        okHttpClient.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("downloadImage", "downloadImage: onFailure ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (!response.isSuccessful) {
                    Log.d("downloadImage", "downloadImage: !response.isSuccessful")
                    return
                }
                response.body?.let { responseBody ->
                    try {
                        val byteArray = responseBody.byteStream().readBytes()
                        val words = fileUrl.split("/").toTypedArray()
                        val fileName = words.last()
                        saveFile(context, byteArray, fileName)
                    } catch (e: IOException) {
                    }
                }
            }

        })

    }


    fun saveFile(context: Context, byteArray: ByteArray, fileName: String) {
        try {
            savePath.mkdirs()
            val file = File(savePath, fileName)
            file.writeBytes(byteArray)
            MediaScannerConnection.scanFile(
                context, arrayOf(file.toString()), null
            ) { path, uri ->
                "file saved successfully".showToast(context)
            }
        } catch (e: IOException) {
        }
    }


    fun hideFile(fileName: String, context: Context, callback: (file: File) -> Unit) {
        val file = File(savePath, fileName)
        val dest = getHidePath(fileName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.move(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } else file.renameTo(
            dest
        )
        MediaScannerConnection.scanFile(context, arrayOf(file.toString()), null) { _, _ ->
            callback(file)
        }
    }

    /*fun hideFile(fileName: String, context: Context, callback: (file: File) -> Unit) {

    }*/

    fun showFile(fileName: String, context: Context, callback: (file: File) -> Unit) {
        val file = File(hidePath, fileName)
        val dest = getShowPath(fileName)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Files.move(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } else file.renameTo(
            dest
        )
        MediaScannerConnection.scanFile(context, arrayOf(file.absolutePath), null) { _, _ ->
            callback(file)
        }
    }

    private fun getHidePath(fileName: String): File {
        val file =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/testApp/.hide/$fileName")
        if (!file.exists())
            file.mkdirs()
        return file
    }

    private fun getShowPath(fileName: String): File {
        val file =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/testApp/$fileName")
        if (!file.exists())
            file.mkdirs()
        return file
    }


}