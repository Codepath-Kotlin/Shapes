package com.codepath.shapes

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.annotation.ColorInt
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.codepath.shapes.databinding.ActivityMainBinding
import com.codepath.shapes.shape.Circle
import com.codepath.shapes.shape.Rectangle
import com.codepath.shapes.shape.Text

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private var mSelectedShape = SelectShape.RECTANGLE

    @ColorInt
    private var mRectColor: Int = 0

    @ColorInt
    private var mCircleColor: Int = 0

    @ColorInt
    private var mTextColor: Int = 0

    private var mRectangleSide: Float = 0f
    private var mCircleRadius: Float = 0f
    private var mTextSize: Float = 0f

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(resources) {
            mRectColor = getColor(R.color.colorRectangle)
            mCircleColor = getColor(R.color.colorCircle)
            mTextColor = getColor(R.color.colorText)
            mRectangleSide = getDimensionPixelOffset(R.dimen.rectangleSide).toFloat()
            mCircleRadius = getDimensionPixelOffset(R.dimen.circleRadius).toFloat()
            mTextSize = getDimension(R.dimen.textFontSize)
        }

        mBinding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            setSupportActionBar(toolbar)

            screen.tapListener = { x, y ->
                val shape = when (mSelectedShape) {
                    SelectShape.RECTANGLE -> Rectangle(x, y, mRectColor, mRectangleSide, mRectangleSide)
                    SelectShape.CIRCLE -> Circle(x, y, mCircleColor, mCircleRadius)
                    SelectShape.TEXT -> Text(x, y, mTextColor, mTextSize, "Kotlin")
                }
                screen.addShape(shape)
            }

            bottomBar.setOnNavigationItemSelectedListener { item ->
                mSelectedShape = when (item.itemId) {
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
            mBinding.screen.clear()
            true
        }
        R.id.action_save, R.id.action_restore -> true
        else -> super.onOptionsItemSelected(item)
    }

    private enum class SelectShape {
        RECTANGLE, CIRCLE, TEXT
    }
}
