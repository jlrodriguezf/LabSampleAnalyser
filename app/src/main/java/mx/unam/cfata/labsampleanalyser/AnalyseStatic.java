package mx.unam.cfata.labsampleanalyser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.Objects;


public class AnalyseStatic extends Fragment {
    public AnalyseStatic() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analyse_static, container, false);
        ImageView imageView = view.findViewById(R.id.pickedImage);
        byte[] byteArray = Objects.requireNonNull(getArguments()).getByteArray("pickedImage");
        Bitmap pickedImage = BitmapFactory.decodeByteArray(byteArray, 0, Objects.requireNonNull(byteArray).length);

        final String TAG = "AnalyseOpenCVActivity";
        BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(Objects.requireNonNull(getActivity()).getApplicationContext()) {
            @Override
            public void onManagerConnected(int status) {
                switch (status) {
                    case LoaderCallbackInterface.SUCCESS: {
                        Log.i(TAG, "OpenCV loaded successfully");
                    } break;
                    default: {
                        super.onManagerConnected(status);
                    } break;
                }
            }
        };

        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, Objects.requireNonNull(getActivity()).getApplicationContext(), mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }

        imageView.setImageBitmap(pickedImage);
        return view;
    }
}