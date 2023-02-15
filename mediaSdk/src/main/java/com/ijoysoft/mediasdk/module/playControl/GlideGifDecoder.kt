package com.ijoysoft.mediasdk.module.playControl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.gifdecoder.StandardGifDecoder
import com.bumptech.glide.load.resource.gif.GifBitmapProvider
import com.ijoysoft.mediasdk.module.playControl.GifDecoder.GifFrame
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.ByteBuffer

//Handler for read & extract Bitmap from *.gif
class GlideGifDecoder {


    companion object {


//        val gifStateClassField by lazy {
//            val stateClass = GifDrawable::class.java.declaredConstructors.let { constructors ->
//                constructors.forEach {
//                    if (it.parameterTypes.size == 1) {
//                        return@let it.parameterTypes[0]
//                    }
//                }
//                throw NoSuchMethodException()
//            }
//            GifDrawable::class.java.declaredFields.let { fields ->
//                fields.forEach {
//                    if (it.type == stateClass) {
//                        return@lazy it.apply { isAccessible = true }
//                    }
//                }
//            }
//            throw NoSuchElementException()
//        }
//
//        val gifFrameLoaderField by lazy {
//            gifStateClassField.type.declaredFields[0].apply { isAccessible = true }
//        }
//
//        val gifDecoderField by lazy {
//            gifFrameLoaderField.type.declaredFields.filter { field ->
//                field.type.let {
//                    it.isInterface && it.isAssignableFrom(StandardGifDecoder::class.java)
//                }
//            }.let {
//                if (it.isNotEmpty() && it.size == 1) {
//                    return@lazy it[0].apply { isAccessible = true }
//                } else {
//                    throw NoSuchElementException()
//                }
//            }
//        }


        fun getGif(context: Context, source: String): GlideGif? {
            var inputStream: InputStream? = null
            val data: ByteArray
            try {
                inputStream =
                    FileInputStream(source)
                data = inputStream.readBytes()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
                return null
            } finally {
                inputStream?.close()
            }
            return getGif(context, data)

        }

        fun getGif(context: Context, @RawRes source: Int): GlideGif {
            val data = context.resources.openRawResource(source).readBytes()
            return getGif(context, data)
        }

        fun getGif(context: Context, data: ByteArray): GlideGif {
            val byteBuffer = ByteBuffer.wrap(data)
            val glide = Glide.get(context)
            val gifBitmapProvider = GifBitmapProvider(glide.bitmapPool, glide.arrayPool)
            //val header = GifHeaderParser().setData(byteBuffer).parseHeader()
            //val standardGifDecoder = StandardGifDecoder(gifBitmapProvider, header, byteBuffer, 1)
            //alternative, without getting header and needing sample size:
            val standardGifDecoder = StandardGifDecoder(gifBitmapProvider)
            standardGifDecoder.read(data)
            val result = ArrayList<GifFrame>()
            val frameCount = standardGifDecoder.frameCount
            if (frameCount == 0) {
                return GlideGif(result, standardGifDecoder.width, standardGifDecoder.height)
            }
            standardGifDecoder.advance()
            for (i in 0 until frameCount) {
                val delay = standardGifDecoder.nextDelay
                val bitmap = standardGifDecoder.nextFrame
                result.add(GifFrame(bitmap, delay))
                //bitmap ready here
                standardGifDecoder.advance()
            }
            return GlideGif(result, standardGifDecoder.width, standardGifDecoder.height)
        }
    }

}