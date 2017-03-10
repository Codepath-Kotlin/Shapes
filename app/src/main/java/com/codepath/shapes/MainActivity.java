package com.codepath.shapes;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.codepath.shapes.databinding.ActivityMainBinding;
import com.codepath.shapes.shape.Circle;
import com.codepath.shapes.shape.Shape;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        final List<Shape> shapeList = new LinkedList<>();

        binding.screen.init();
        binding.screen.setOnTapListener(new Screen.OnTapListener() {
            @Override
            public void onTap(float x, float y) {
                shapeList.add(new Circle(x, y, 60f));
                binding.screen.setShapeList(shapeList);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_options, menu);
        return true;
    }
}
