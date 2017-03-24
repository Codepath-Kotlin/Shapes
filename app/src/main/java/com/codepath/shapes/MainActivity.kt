package com.codepath.shapes

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.codepath.shapes.databinding.ActivityMainBinding
import com.codepath.shapes.io.ShapeSerializer
import com.codepath.shapes.shape.Circle
import com.codepath.shapes.shape.Rectangle
import com.codepath.shapes.shape.Text

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var selectedShape = SelectShape.RECTANGLE
    private val shapeSerializer = ShapeSerializer(this)

    @ColorInt
    private var rectColor: Int = 0

    @ColorInt
    private var circleColor: Int = 0

    @ColorInt
    private var textColor: Int = 0

    private var rectangleSide: Float = 0f
    private var circleRadius: Float = 0f
    private var textSize: Float = 0f

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(resources) {
            rectColor = getColor(R.color.colorRectangle)
            circleColor = getColor(R.color.colorCircle)
            textColor = getColor(R.color.colorText)
            rectangleSide = getDimensionPixelOffset(R.dimen.rectangleSide).toFloat()
            circleRadius = getDimensionPixelOffset(R.dimen.circleRadius).toFloat()
            textSize = getDimension(R.dimen.textFontSize)
        }

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            setSupportActionBar(toolbar)

            screen.tapListener = { x, y ->
                val shape = when (selectedShape) {
                    SelectShape.RECTANGLE -> Rectangle(x, y, rectColor, rectangleSide, rectangleSide)
                    SelectShape.CIRCLE -> Circle(x, y, circleColor, circleRadius)
                    SelectShape.TEXT -> Text(x, y, textColor, textSize, "Kotlin")
                }
                screen.addShape(shape)
            }

            bottomBar.setOnNavigationItemSelectedListener { item ->
                selectedShape = when (item.itemId) {
                    R.id.action_circle -> SelectShape.CIRCLE
                    R.id.action_rectangle -> SelectShape.RECTANGLE
                    R.id.action_text -> SelectShape.TEXT
                    else -> throw IllegalArgumentException("Unexpected selected value ${item.itemId}")
                }
                true
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_erase -> {
            binding.screen.clear()
            true
        }
        R.id.action_save -> {
            shapeSerializer.saveShapes(binding.screen.shapeList) {
                Snackbar.make(binding.root, "Saved", Snackbar.LENGTH_SHORT).show()
            }
            true
        }
        R.id.action_restore -> {
            shapeSerializer.restoreShapes { shapeList ->
                binding.screen.shapeList = shapeList
                Snackbar.make(binding.root, "Restored", Snackbar.LENGTH_SHORT).show();
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private enum class SelectShape {
        RECTANGLE, CIRCLE, TEXT
    }
}
