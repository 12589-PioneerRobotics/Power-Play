package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.apriltag.AprilTagDetection
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation


@TeleOp(name = "CV Test")
class CVTest : OpMode() {
    val FEET_PER_METER = 3.28084

    // Lens intrinsics
    // UNITS ARE PIXELS
    // NOTE: this calibration is for the C920 webcam at 800x448.
    // You will need to do your own calibration for other configurations!
    var fx = 578.272
    var fy = 578.272
    var cx = 402.145
    var cy = 221.506

    // UNITS ARE METERS
    private var tagsize = 0.166

    var numFramesWithoutDetection = 0

    private val DECIMATION_HIGH = 3f
    private val DECIMATION_LOW = 2f
    private val THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f
    private val THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4

    private val pipeline: AprilTagDetectionPipeline = AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy)
    private val cameraMonitorViewId : Int = hardwareMap.appContext.resources.getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.packageName)
    val webcam : OpenCvCamera = OpenCvCameraFactory.getInstance().createWebcam(
        hardwareMap.get("Webcam 1") as WebcamName?, cameraMonitorViewId
    )

    override fun init() {
        webcam.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {
                //camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
                webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {}
        })
    }

    override fun loop() {
        val detections: ArrayList<AprilTagDetection>? = pipeline.getDetectionsUpdate()

        // If there's been a new frame...

        // If there's been a new frame...
        if (detections != null) {
            telemetry.addData("FPS", webcam.getFps())
            telemetry.addData("Overhead ms", webcam.getOverheadTimeMs())
            telemetry.addData("Pipeline ms", webcam.getPipelineTimeMs())

            // If we don't see any tags
            if (detections.size === 0) {
                numFramesWithoutDetection++

                // If we haven't seen a tag for a few frames, lower the decimation
                // so we can hopefully pick one up if we're e.g. far back
                if (numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION) {
                    pipeline.setDecimation(DECIMATION_LOW)
                }
            } else {
                numFramesWithoutDetection = 0

                // If the target is within 1 meter, turn on high decimation to
                // increase the frame rate
                if (detections[0].pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS) {
                    pipeline.setDecimation(DECIMATION_HIGH)
                }
                for (detection in detections) {
                    telemetry.addLine(String.format("\nDetected tag ID=%d", detection.id))
                    telemetry.addLine(
                        String.format(
                            "Translation X: %.2f feet",
                            detection.pose.x * FEET_PER_METER
                        )
                    )
                    telemetry.addLine(
                        String.format(
                            "Translation Y: %.2f feet",
                            detection.pose.y * FEET_PER_METER
                        )
                    )
                    telemetry.addLine(
                        String.format(
                            "Translation Z: %.2f feet",
                            detection.pose.z * FEET_PER_METER
                        )
                    )
                    telemetry.addLine(
                        String.format(
                            "Rotation Yaw: %.2f degrees",
                            Math.toDegrees(detection.pose.yaw)
                        )
                    )
                    telemetry.addLine(
                        String.format(
                            "Rotation Pitch: %.2f degrees",
                            Math.toDegrees(detection.pose.pitch)
                        )
                    )
                    telemetry.addLine(
                        String.format(
                            "Rotation Roll: %.2f degrees",
                            Math.toDegrees(detection.pose.roll)
                        )
                    )
                }
            }
            telemetry.update()
        }
    }
}