package com.aayush.runningappaayush.db

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class Converters {

    //to get bitmap from a byteArray
    @TypeConverter
    fun toBitmap(byteArray: ByteArray):Bitmap{
       return BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
    }

    //to store a bitmap in room database
    @TypeConverter
    fun fromBitMap(bmp:Bitmap):ByteArray{
        val outputStream =  ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }

}