package com.codepath.shapes.io

import android.content.Context
import android.os.AsyncTask
import android.support.annotation.MainThread
import android.support.annotation.WorkerThread
import android.util.Log
import com.codepath.shapes.shape.Circle
import com.codepath.shapes.shape.Rectangle
import com.codepath.shapes.shape.Shape
import com.codepath.shapes.shape.Text
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*

typealias OnSaveShapesCallback = () -> Unit
typealias ShapeList = List<Shape>
typealias OnRestoreShapesCallBack = (ShapeList) -> Unit

class ShapeSerializer(private val mContext: Context) {
    private val TAG = "ShapeSerializer"

    private val SHAPE_X = "x"
    private val SHAPE_Y = "y"
    private val SHAPE_COLOR = "color"
    private val SHAPE_TYPE = "type"
    private val CIRCLE_RADIUS = "radius"
    private val RECT_WIDTH = "width"
    private val RECT_HEIGHT = "height"
    private val TEXT_TEXT = "text"
    private val TEXT_FONT_SIZE = "fontSize"
    private val TYPE_CIRCLE = "circle"
    private val TYPE_RECTANGLE = "rectangle"
    private val TYPE_TEXT = "text"

    private val FILENAME = "shapes.json"

    @MainThread
    fun saveShapes(shapeList: ShapeList, callback: OnSaveShapesCallback) {
        object : AsyncTask<ShapeList, Void, Boolean>() {
            @SafeVarargs
            override fun doInBackground(vararg params: ShapeList): Boolean {
                internalSaveShapes(params.first())
                return true
            }

            override fun onPostExecute(aBoolean: Boolean?) = callback()
        }.execute(shapeList)
    }

    @MainThread
    fun restoreShapes(callback: OnRestoreShapesCallBack) {
        object : AsyncTask<Void, Void, ShapeList>() {
            override fun doInBackground(vararg params: Void) = internalRestoreShapes()
            override fun onPostExecute(shapeList: ShapeList) = callback(shapeList)
        }.execute()
    }

    @WorkerThread
    private fun internalSaveShapes(shapeList: ShapeList) {
        val file = File(mContext.filesDir, FILENAME)
        saveString(file, shapeList.toJsonString())
    }

    @WorkerThread
    private fun internalRestoreShapes(): ShapeList {
        val file = File(mContext.filesDir, FILENAME)
        return if (file.exists() && file.canRead()) {
            val json = readString(file)
            fromJsonString(json)
        } else {
            emptyList()
        }
    }

    private fun ShapeList.toJsonString(): String = try {
        val result = JSONArray()
        map { it.toJsonObject() }.forEach { result.put(it) }
        result.toString()
    } catch (e: JSONException) {
        "[]"
    }

    private fun Shape.toJsonObject() = JSONObject().apply {
        put(SHAPE_X, x.toDouble())
        put(SHAPE_Y, y.toDouble())
        put(SHAPE_COLOR, color)

        when (this@toJsonObject) {
            is Circle -> {
                put(SHAPE_TYPE, TYPE_CIRCLE)
                put(CIRCLE_RADIUS, radius.toDouble())
            }

            is Rectangle -> {
                put(SHAPE_TYPE, TYPE_RECTANGLE)
                put(RECT_WIDTH, width.toDouble())
                put(RECT_HEIGHT, height.toDouble())
            }

            is Text -> {
                put(SHAPE_TYPE, TYPE_TEXT)
                put(TEXT_TEXT, text)
                put(TEXT_FONT_SIZE, fontSize.toDouble())
            }
        }
    }

    private fun fromJsonString(jsonArrayString: String): ShapeList {
        val shapeList = mutableListOf<Shape>()

        try {
            val jsonArray = JSONArray(jsonArrayString)
            for (i in 0 until jsonArray.length()) {
                with(jsonArray.getJSONObject(i)) {
                    val x = getDouble(SHAPE_X).toFloat()
                    val y = getDouble(SHAPE_Y).toFloat()
                    val color = getInt(SHAPE_COLOR)
                    val type = getString(SHAPE_TYPE)

                    val shape = when (type) {
                        TYPE_CIRCLE -> {
                            val radius = getDouble(CIRCLE_RADIUS).toFloat()
                            Circle(x, y, color, radius)
                        }

                        TYPE_RECTANGLE -> {
                            val width = getDouble(RECT_WIDTH).toFloat()
                            val height = getDouble(RECT_HEIGHT).toFloat()
                            Rectangle(x, y, color, width, height)
                        }

                        TYPE_TEXT -> {
                            val text = getString(TEXT_TEXT)
                            val fontSize = getDouble(TEXT_FONT_SIZE).toFloat()
                            Text(x, y, color, fontSize, text)
                        }

                        else -> null
                    }
                    shape?.let { shapeList.add(it) }
                }
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Failed to serialize shape list", e)
        }

        return shapeList
    }

    @WorkerThread
    private fun saveString(file: File, string: String) {
        try {
            FileOutputStream(file).use { out -> out.write(string.toByteArray()) }
        } catch (e: IOException) {
            Log.e(TAG, "Failed to save file", e)
        }
    }

    @WorkerThread
    private fun readString(file: File) = try {
        FileInputStream(file).use { `in` -> BufferedReader(InputStreamReader(`in`)).readText() }
    } catch (e: IOException) {
        Log.e(TAG, "Failed to read file", e)
        "[]"
    }
}
