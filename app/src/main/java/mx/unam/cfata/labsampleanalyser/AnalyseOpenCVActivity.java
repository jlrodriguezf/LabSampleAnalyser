package mx.unam.cfata.labsampleanalyser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

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

    //JAVA CAMERA INITIALIZATION AND VARIABLE SETTING
    private JavaCameraView mOpenCvCameraView;
    Mat InputFrame;

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
        mOpenCvCameraView = findViewById(R.id.OpenCvView);
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
    }

    @Override
    public void onCameraViewStopped() {
        //FILE WRITING
        File filepath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"LabSampleAnalyser");
        if (!filepath.exists()) {
            if (!filepath.mkdirs()) {
                Log.e(TAG, "Failed to create directory");
            }
        }
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        File imagename = new File(filepath.getPath() + File.separator + "LSA_PNG_" + timeStamp + ".png");
        String image = imagename.toString();
        Boolean writeStatus = Imgcodecs.imwrite(image, InputFrame);
        if (writeStatus)
            Log.i(TAG, "SUCCESS writing image to external storage...");
        else
            Log.i(TAG, "FAILED writing image to external storage...");
        
        //MAT RELEASE
        InputFrame.release();
        finish();
    }

    @Override
    //CAMERA FRAME INITIALIZATION
    public Mat onCameraFrame(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //PRE-PROCESSING
        final Mat circles = new Mat();
        Mat InputFrame = inputFrame.gray();

        //Imgproc.GaussianBlur(InputFrame, InputFrame, new Size(9, 9), 2, 2);

        //ACTUAL ALGORITHM
        Imgproc.HoughCircles(InputFrame, circles, Imgproc.CV_HOUGH_GRADIENT, sharedPreferences.getInt("pref_hough_dp", 1), sharedPreferences.getInt("pref_hough_minDist", 75), sharedPreferences.getInt("pref_hough_oedt", 50), sharedPreferences.getInt("pref_hough_ofdt", 20), sharedPreferences.getInt("pref_hough_minRad", 0), sharedPreferences.getInt("pref_hough_maxRad", 0));

        //ORGANISM COUNT TEXT
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                final TextView textView = findViewById(R.id.sample_data);
                textView.setText("Number of Organisms: " + circles.cols());
                Log.i(TAG, String.valueOf("Number of Organisms: " + circles.cols()));
            }
        });

        //CONTOUR DRAWING
        if (circles.cols() > 0) {
            for (int x=0; x < Math.min(circles.cols(), 10); x++ ) {
                double circleVec[] = circles.get(0, x);
                if (circleVec == null) {
                    break;
                }
                Point center = new Point((int) circleVec[0], (int) circleVec[1]);
                int radius = (int) circleVec[2];
                //CENTER OF CIRCLE
                Imgproc.circle(InputFrame, center, 3, new Scalar(255, 0, 0), 5);
                //EXTERNAL RING
                Imgproc.circle(InputFrame, center, radius, new Scalar(0, 0, 255), 5);
            }
        }

        //CAM RELEASE AND RESULT
        circles.release();
        InputFrame.release();
        //RETURN RESULT AND MAKE IT AVAILABLE TO OTHER METHODS IN RGB COLOR
        return inputFrame.rgba();
    }
}
