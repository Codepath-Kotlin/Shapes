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
import java.io.File
import java.io.IOException

typealias ShapeList = List<Shape>
typealias OnSaveShapesCallback = () -> Unit
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
        object : AsyncTask<ShapeList, Nothing, Unit>() {
            @SafeVarargs
            override fun doInBackground(vararg params: ShapeList) = internalSaveShapes(params.first())

            override fun onPostExecute(result: Unit?) = callback()
        }.execute(shapeList)
    }

    @MainThread
    fun restoreShapes(callback: OnRestoreShapesCallBack) {
        object : AsyncTask<Void, Nothing, ShapeList>() {
            override fun doInBackground(vararg params: Void) = internalRestoreShapes()
            override fun onPostExecute(shapeList: ShapeList) = callback(shapeList)
        }.execute()
    }

    @WorkerThread
    private fun internalSaveShapes(shapeList: ShapeList) = File(mContext.filesDir, FILENAME).writeText(shapeList.toJsonString())

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
        JSONArray().apply {
            map { shape -> shape.toJsonObject() }.forEach { put(it) }
        }.toString()
    } catch (e: JSONException) {
        "[]"
    }

    private fun Shape.toJsonObject() = JSONObject().apply {
        put(SHAPE_X to x,
                SHAPE_Y to y,
                SHAPE_COLOR to color)

        when (this@toJsonObject) {
            is Circle -> {
                put(SHAPE_TYPE to TYPE_CIRCLE,
                        CIRCLE_RADIUS to radius)
            }

            is Rectangle -> {
                put(SHAPE_TYPE to TYPE_RECTANGLE,
                        RECT_WIDTH to width,
                        RECT_HEIGHT to height)
            }

            is Text -> {
                put(SHAPE_TYPE to TYPE_TEXT,
                        TEXT_TEXT to text,
                        TEXT_FONT_SIZE to fontSize)
            }
        }
    }

    private fun fromJsonString(jsonArrayString: String): ShapeList {
        val shapeList = mutableListOf<Shape>()

        try {
            val jsonArray = JSONArray(jsonArrayString)
            for (i in 0 until jsonArray.length()) {
                with(jsonArray.getJSONObject(i)) {
                    val x = getFloat(SHAPE_X)
                    val y = getFloat(SHAPE_Y)
                    val color = getInt(SHAPE_COLOR)
                    val type = getString(SHAPE_TYPE)

                    val shape = when (type) {
                        TYPE_CIRCLE -> {
                            val radius = getFloat(CIRCLE_RADIUS)
                            Circle(x, y, color, radius)
                        }

                        TYPE_RECTANGLE -> {
                            val width = getFloat(RECT_WIDTH)
                            val height = getFloat(RECT_HEIGHT)
                            Rectangle(x, y, color, width, height)
                        }

                        TYPE_TEXT -> {
                            val text = getString(TEXT_TEXT)
                            val fontSize = getFloat(TEXT_FONT_SIZE)
                            Text(x, y, color, fontSize, text)
                        }

                        else -> null
                    }
                    shape?.let { shapeList += it }
                }
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Failed to serialize shape list", e)
        }

        return shapeList
    }

    @WorkerThread
    private fun readString(file: File) = try {
        file.readText()
    } catch (e: IOException) {
        Log.e(TAG, "Failed to read file", e)
        "[]"
    }

    private fun JSONObject.getFloat(name: String) = getDouble(name).toFloat()
    private fun JSONObject.put(vararg values: Pair<String, Any>) {
        values.forEach { put(it.first, it.second) }
    }
}
