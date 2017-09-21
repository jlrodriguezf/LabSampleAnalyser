package mx.unam.cfata.labsampleanalyser;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.WindowManager;

public class AnalyseOpenCVActivity extends AppCompatActivity implements CvCameraViewListener2 {

    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat mIntermediateMat;

    private static final String TAG = "AnalyseOpenCVActivity";
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Error loading OpenCV...");
        }
    }

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default: {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.analyse_layout);
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.OpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mIntermediateMat = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        if (mIntermediateMat != null)
            mIntermediateMat.release();

        mIntermediateMat = null;
    }

    @Override
    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        Size sizeRgba = rgba.size();

        Mat rgbaInnerWindow;

        int rows = (int) sizeRgba.height;
        int cols = (int) sizeRgba.width;

        int left = cols / 8;
        int top = rows / 8;

        int width = cols * 3 / 4;
        int height = rows * 3 / 4;

        rgbaInnerWindow = rgba
                .submat(top, top + height, left, left + width);
        Imgproc.cvtColor(rgbaInnerWindow, rgbaInnerWindow,
                Imgproc.COLOR_RGB2GRAY);
        Mat circles = rgbaInnerWindow.clone();
        rgbaInnerWindow = rgba
                .submat(top, top + height, left, left + width);
        Imgproc.GaussianBlur(rgbaInnerWindow, rgbaInnerWindow, new Size(5,
                5), 2, 2);
        Imgproc.Canny(rgbaInnerWindow, mIntermediateMat, 10, 90);
        Imgproc.HoughCircles(mIntermediateMat, circles,
                Imgproc.CV_HOUGH_GRADIENT, 1, 75, 50, 13, 35, 40);
        Imgproc.cvtColor(mIntermediateMat, rgbaInnerWindow,
                Imgproc.COLOR_GRAY2BGRA, 4);

        for (int x = 0; x < circles.cols(); x++) {
            double vCircle[] = circles.get(0, x);
            if (vCircle == null)
                break;
            Point pt = new Point(Math.round(vCircle[0]),
                    Math.round(vCircle[1]));
            int radius = (int) Math.round(vCircle[2]);
            Log.d("cv", pt + " radius " + radius);
            Imgproc.circle(rgbaInnerWindow, pt, 3, new Scalar(0, 0, 255), 5);
            Imgproc.circle(rgbaInnerWindow, pt, radius, new Scalar(255, 0, 0),
                    5);
        }
        rgbaInnerWindow.release();
        return rgba;
    }

}
