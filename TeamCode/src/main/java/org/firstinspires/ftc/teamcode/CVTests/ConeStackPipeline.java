package org.firstinspires.ftc.teamcode.CVTests;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import static org.opencv.imgproc.Imgproc.boundingRect;
import static org.opencv.imgproc.Imgproc.contourArea;

import android.os.Build;
import android.provider.ContactsContract;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Stream;

public class ConeStackPipeline extends OpenCvPipeline {
    final Scalar GREEN = new Scalar(0, 255, 0);
    final Scalar RED_LB = new Scalar(108, 0, 0);
    final Scalar RED_UB = new Scalar(255, 93, 255);

    Mat mask = new Mat();
    Mat hierarchy = new Mat();
    ArrayList<MatOfPoint> contours;
    Rect boundingRect;
    MatOfPoint largestContour;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2GRAY);
        Imgproc.GaussianBlur(input, input, new Size(5, 5), 0);

        Core.inRange(input, RED_LB, RED_UB, mask);
        contours = new ArrayList<>();

        Imgproc.findContours(input, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        if (!contours.isEmpty()) {
//            contours.removeIf(a -> contourArea(a) < 20);
//            contours.removeIf(a -> {
//                Rect bb = boundingRect(a);
//                double aspectRatio = bb.width / bb.height;
//                return aspectRatio > 0.4 && aspectRatio < 0.8;
//            });
            largestContour = contours.stream()
                    .max(Comparator.comparing(Imgproc::contourArea)).get();

            boundingRect = boundingRect(largestContour);
            Imgproc.rectangle(input, boundingRect.br(), boundingRect.tl(), GREEN, 2);
        }

        return input;
    }
}
