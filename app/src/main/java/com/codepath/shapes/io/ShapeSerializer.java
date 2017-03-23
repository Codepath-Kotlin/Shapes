package com.codepath.shapes.io;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.codepath.shapes.shape.Circle;
import com.codepath.shapes.shape.Rectangle;
import com.codepath.shapes.shape.Shape;
import com.codepath.shapes.shape.Text;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShapeSerializer {
    private static final String TAG = "ShapeSerializer";

    private static final String SHAPE_X = "x";
    private static final String SHAPE_Y = "y";
    private static final String SHAPE_COLOR = "color";
    private static final String SHAPE_TYPE = "type";
    private static final String CIRCLE_RADIUS = "radius";
    private static final String RECT_WIDTH = "width";
    private static final String RECT_HEIGHT = "height";
    private static final String TEXT_TEXT = "text";
    private static final String TEXT_FONT_SIZE = "fontSize";
    private static final String TYPE_CIRCLE = "circle";
    private static final String TYPE_RECTANGLE = "rectangle";
    private static final String TYPE_TEXT = "text";

    private static final String FILENAME = "shapes.json";


    @NonNull
    private Context mContext;


    public ShapeSerializer(@NonNull Context context) {
        mContext = context;
    }

    @MainThread
    public void saveShapes(@NonNull List<Shape> shapeList, @NonNull final OnSaveFinishedCallback callback) {
        final AsyncTask<List<Shape>, Void, Boolean> saveTask = new AsyncTask<List<Shape>, Void, Boolean>() {

            @SafeVarargs
            @Override
            protected final Boolean doInBackground(List<Shape>... params) {
                final List<Shape> shapeList = params[0];
                internalSaveShapes(shapeList);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                callback.onSaveFinished();
            }
        };

        saveTask.execute(shapeList);
    }


    @MainThread
    public void restoreShapes(@NonNull final OnRestoreFinishedCallback callback) {
        final AsyncTask<Void, Void, List<Shape>> restoreTask = new AsyncTask<Void, Void, List<Shape>>() {

            @Override
            protected List<Shape> doInBackground(Void... params) {
                return internalRestoreShapes();
            }

            @Override
            protected void onPostExecute(List<Shape> shapeList) {
                callback.onRestoreFinished(shapeList);
            }
        };

        restoreTask.execute();
    }

    @WorkerThread
    private void internalSaveShapes(@NonNull List<Shape> shapeList) {
        final File file = new File(mContext.getFilesDir(), FILENAME);
        final String json = toJsonString(shapeList);
        saveString(file, json);
    }

    @WorkerThread
    @NonNull
    private List<Shape> internalRestoreShapes() {
        final File file = new File(mContext.getFilesDir(), FILENAME);
        if (file.exists() && file.canRead()) {
            final String json = readString(file);
            return fromJsonString(json);
        } else {
            return Collections.emptyList();
        }
    }

    @NonNull
    private String toJsonString(@NonNull List<Shape> shapeList) {
        final JSONArray result;
        try {
            result = new JSONArray();
            for (Shape shape : shapeList) {
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put(SHAPE_X, shape.getX());
                jsonObject.put(SHAPE_Y, shape.getY());
                jsonObject.put(SHAPE_COLOR, shape.getColor());

                if (shape instanceof Circle) {
                    jsonObject.put(SHAPE_TYPE, TYPE_CIRCLE);
                    final Circle circle = (Circle) shape;
                    jsonObject.put(CIRCLE_RADIUS, circle.getRadius());
                } else if (shape instanceof Rectangle) {
                    jsonObject.put(SHAPE_TYPE, TYPE_RECTANGLE);
                    final Rectangle rectangle = (Rectangle) shape;
                    jsonObject.put(RECT_WIDTH, rectangle.getWidth());
                    jsonObject.put(RECT_HEIGHT, rectangle.getHeight());
                } else if (shape instanceof Text) {
                    jsonObject.put(SHAPE_TYPE, TYPE_TEXT);
                    final Text text = (Text) shape;
                    jsonObject.put(TEXT_TEXT, text.getText());
                    jsonObject.put(TEXT_FONT_SIZE, text.getFontSize());
                }

                result.put(jsonObject);
            }
            return result.toString();
        } catch (JSONException e) {
            return "[]";
        }
    }

    @NonNull
    private List<Shape> fromJsonString(@NonNull String jsonArrayString) {
        final List<Shape> shapeList = new ArrayList<>();

        try {
            final JSONArray jsonArray = new JSONArray(jsonArrayString);
            final int shapeCount = jsonArray.length();
            for (int i = 0; i < shapeCount; i++) {
                final JSONObject shape = jsonArray.getJSONObject(i);
                final float x = (float) shape.getDouble(SHAPE_X);
                final float y = (float) shape.getDouble(SHAPE_Y);
                final int color = shape.getInt(SHAPE_COLOR);
                final String type = shape.getString(SHAPE_TYPE);

                switch (type) {
                    case TYPE_CIRCLE:
                        final float radius = (float) shape.getDouble(CIRCLE_RADIUS);
                        shapeList.add(new Circle(x, y, color, radius));
                        break;

                    case TYPE_RECTANGLE:
                        final float width = (float) shape.getDouble(RECT_WIDTH);
                        final float height = (float) shape.getDouble(RECT_HEIGHT);
                        shapeList.add(new Rectangle(x, y, color, width, height));
                        break;

                    case TYPE_TEXT:
                        final String text = shape.getString(TEXT_TEXT);
                        final float fontSize = (float) shape.getDouble(TEXT_FONT_SIZE);
                        shapeList.add(new Text(x, y, color, fontSize, text));
                        break;
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Failed to serialize shape list", e);
        }

        return shapeList;
    }

    @WorkerThread
    private void saveString(@NonNull File file, @NonNull String string) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            out.write(string.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Failed to save file", e);
        }
    }

    @WorkerThread
    @NonNull
    private String readString(@NonNull File file) {
        try (FileInputStream in = new FileInputStream(file)) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.toString();
        } catch (IOException e) {
            Log.e(TAG, "Failed to read file", e);
            return "[]";
        }
    }

    public interface OnSaveFinishedCallback {
        void onSaveFinished();
    }

    public interface OnRestoreFinishedCallback {
        void onRestoreFinished(@NonNull List<Shape> shapeList);
    }
}
