package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.CVTests.AprilTagDetectionPipeline
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence
import org.firstinspires.ftc.teamcode.trajectorysequence.sequencesegment.TrajectorySegment
import org.openftc.apriltag.AprilTagDetection
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@Autonomous(name = "Autonomous")
class RedAutonomous: LinearOpMode() {
    private val fx = 578.272
    private val fy = 578.272
    private val cx = 402.145
    private val cy = 221.506
    private val tagsize = 0.166
    private var tagId = 2

    private lateinit var park: TrajectorySequence
    private lateinit var drive: SampleMecanumDrive

    override fun runOpMode() {
        drive = SampleMecanumDrive(hardwareMap)
        drive.poseEstimate = Pose2d(15.0, -58.0, Math.toRadians(90.0))

        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardwareMap.appContext.packageName
        )

        val camera = OpenCvCameraFactory.getInstance().createWebcam(
            hardwareMap.get(
                WebcamName::class.java, "Webcam 1"
            ), cameraMonitorViewId
        )

        val pipeline = AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy)

        camera.setPipeline(pipeline)

        camera.openCameraDeviceAsync(object : OpenCvCamera.AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {}
        })

        FtcDashboard.getInstance().startCameraStream(camera, 0.0)

        while (!isStarted && !isStopRequested) {
            val currentDetections: ArrayList<AprilTagDetection> = pipeline.latestDetections

            if (currentDetections.size == 1) {
                when (currentDetections[0].id) {
                    11 -> tagId = 1
                    15 -> tagId = 3
                }
            }

            telemetry.addData("Tag ID", tagId)
            telemetry.update()
            sleep(20)
        }



        when (tagId) {
            1 -> park = drive.trajectorySequenceBuilder(drive.poseEstimate)
                    .strafeLeft(30.0)
                    .strafeRight(5.0)
                    .forward(20.0)
                    .build()
            2 -> park = drive.trajectorySequenceBuilder(drive.poseEstimate)
                    .strafeLeft(30.0)
                    .strafeRight(30.0)
                    .forward(20.0)
                    .build()
            3 -> park = drive.trajectorySequenceBuilder(drive.poseEstimate)
                    .strafeLeft(30.0)
                    .strafeRight(55.0)
                    .forward(20.0)
                    .build()
        }


        drive.followTrajectorySequence(park)
    }
}