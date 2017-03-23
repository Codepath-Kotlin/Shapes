package com.codepath.shapes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.codepath.shapes.databinding.ActivityMainBinding;
import com.codepath.shapes.io.ShapeSerializer;
import com.codepath.shapes.shape.Circle;
import com.codepath.shapes.shape.Rectangle;
import com.codepath.shapes.shape.Shape;
import com.codepath.shapes.shape.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private SelectShape mSelectedShape = SelectShape.RECTANGLE;

    @ColorInt
    private int mRectColor;

    @ColorInt
    private int mCircleColor;

    @ColorInt
    private int mTextColor;


    private float mRectangleSide;
    private float mCircleRadius;
    private float mTextSize;

    private ShapeSerializer mShapeSerializer;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRectColor = getResources().getColor(R.color.colorRectangle);
        mCircleColor = getResources().getColor(R.color.colorCircle);
        mTextColor = getResources().getColor(R.color.colorText);
        mRectangleSide = getResources().getDimensionPixelOffset(R.dimen.rectangleSide);
        mCircleRadius = getResources().getDimensionPixelOffset(R.dimen.circleRadius);
        mTextSize = getResources().getDimension(R.dimen.textFontSize);

        mShapeSerializer = new ShapeSerializer(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        mBinding.screen.init();
        mBinding.screen.setOnTapListener(new Screen.OnTapListener() {
            @Override
            public void onTap(float x, float y) {
                final Shape shape;
                switch (mSelectedShape) {
                    case RECTANGLE:
                        shape = new Rectangle(x, y, mRectColor, mRectangleSide, mRectangleSide);
                        break;

                    case CIRCLE:
                        shape = new Circle(x, y, mCircleColor, mCircleRadius);
                        break;

                    case TEXT:
                        shape = new Text(x, y, mTextColor, mTextSize, "Kotlin");
                        break;

                    default:
                        //Hey Java, this shouldn't happen
                        shape = new Rectangle(0, 0, 0, 0, 0);
                }

                mBinding.screen.addShape(shape);
            }
        });

        mBinding.bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_circle:
                        mSelectedShape = SelectShape.CIRCLE;
                        break;
                    case R.id.action_rectangle:
                        mSelectedShape = SelectShape.RECTANGLE;
                        break;
                    case R.id.action_text:
                        mSelectedShape = SelectShape.TEXT;
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_erase:
                mBinding.screen.clear();
                return true;

            case R.id.action_save:
                mShapeSerializer.saveShapes(mBinding.screen.getShapeList(), new ShapeSerializer.OnSaveFinishedCallback() {
                    @Override
                    public void onSaveFinished() {
                        Snackbar.make(mBinding.getRoot(), "Saved", Snackbar.LENGTH_SHORT).show();
                    }
                });
                return true;

            case R.id.action_restore:
                mShapeSerializer.restoreShapes(new ShapeSerializer.OnRestoreFinishedCallback() {
                    @Override
                    public void onRestoreFinished(@NonNull List<Shape> shapeList) {
                        Snackbar.make(mBinding.getRoot(), "Restored", Snackbar.LENGTH_SHORT).show();
                        mBinding.screen.setShapeList(shapeList);
                    }
                });
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private enum SelectShape {
        RECTANGLE, CIRCLE, TEXT
    }
}