package com.codepath.shapes

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
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

    private val rectColor by lazyColor(R.color.colorRectangle)
    private val circleColor by lazyColor(R.color.colorCircle)
    private val textColor by lazyColor(R.color.colorText)

    private val rectangleSide by lazyDimension(R.dimen.rectangleSide)
    private val circleRadius by lazyDimension(R.dimen.circleRadius)
    private val textSize by lazyFontSize(R.dimen.textFontSize)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                Snackbar.make(binding.root, "Restored", Snackbar.LENGTH_SHORT).show()
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private enum class SelectShape {
        RECTANGLE, CIRCLE, TEXT
    }

    private fun lazyColor(@ColorRes color: Int) = lazy({ resources.getColor(color) })
    private fun lazyDimension(@DimenRes dimenRes: Int) = lazy({ resources.getDimensionPixelOffset(dimenRes).toFloat() })
    private fun lazyFontSize(@DimenRes dimenRes: Int) = lazy({ resources.getDimension(dimenRes) })
}
