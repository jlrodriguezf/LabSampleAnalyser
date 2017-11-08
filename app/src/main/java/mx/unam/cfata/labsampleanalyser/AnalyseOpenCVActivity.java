package mx.unam.cfata.labsampleanalyser;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AnalyseOpenCVActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "AnalyseOpenCVActivity";
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Error loading OpenCV...");
        }
    }

    private JavaCameraView mOpenCvCameraView;
    Mat mGRAY;
//    Mat mRGBAF;
//    Mat mRGBAT;

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
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.analyse_layout);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.OpenCvView);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
                mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
                mOpenCvCameraView.disableView();
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent return2MainActivity = new Intent(AnalyseOpenCVActivity.this, MainActivity.class);
        return2MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(return2MainActivity);
        finish();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
//        mGRAY = new Mat(height, width, CvType.CV_16U);
//        mRGBAF = new Mat(height, width, CvType.CV_16U);
//        mRGBAT = new Mat(width, width, CvType.CV_16U);
    }

    @Override
    public void onCameraViewStopped() {
        mGRAY.release();
        finish();
    }
//TODO: Implement imwrite to save analysed photo to external storage
    @Override
    public Mat onCameraFrame(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        Mat mGRAY = inputFrame.gray();
//        Core.transpose(mGRAY, mRGBAT);
//        Imgproc.resize(mRGBAT, mRGBAF, mRGBAF.size(), 1, 1, 0);
//        Core.flip(mRGBAF, mGRAY, 1);
        Mat circles = new Mat();
        Imgproc.blur(mGRAY, mGRAY, new Size(7, 7), new Point(2, 2));
        Imgproc.HoughCircles(mGRAY, circles, Imgproc.CV_HOUGH_GRADIENT, 1.2, 200, 100, 100, 0, 0);

        //TODO: TextView Number of Organisms without crashing
        Log.i(TAG, String.valueOf("Number of Organisms: " + circles.cols()));

        Imgproc.putText(mGRAY, String.valueOf("Number of Organisms: " + circles.cols()), new Point(10, 50), Core.FONT_HERSHEY_COMPLEX, 1, new Scalar(0, 0, 0), 4);

        if (circles.cols() > 0) {
            for (int x=0; x < Math.min(circles.cols(), 5); x++ ) {
                double circleVec[] = circles.get(0, x);
                if (circleVec == null) {
                    break;
                }
                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];
                Imgproc.circle(mGRAY, center, 3, new Scalar(255, 255, 255), 5);
                Imgproc.circle(mGRAY, center, radius, new Scalar(255, 255, 255), 2);
            }
        }
//        Mat mInter = new Mat();
//        Imgproc.cvtColor(mGRAY, mInter, Imgproc.COLOR_RGBA2BGR, 3);
        File filepath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"LabSampleAnalyser");
        if (!filepath.exists()) {
            if (!filepath.mkdirs()) {
                Log.e(TAG, "Failed to create directory");
            }
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        File imagename = new File(filepath.getPath() + File.separator + "LBA_PNG_" + timeStamp + ".png");
        String image = imagename.toString();
        Boolean writeStatus = Imgcodecs.imwrite(image, mGRAY);
        if (writeStatus)
            Log.i(TAG, "SUCCESS writing image to external storage...");
        else
            Log.i(TAG, "FAILED writing image to external storage...");
        circles.release();
        mGRAY.release();
        return inputFrame.rgba();
    }
}
