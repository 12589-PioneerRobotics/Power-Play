package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation

@TeleOp(name = "Bum.kt")
class KotlinCameraTest : LinearOpMode() {
    override fun runOpMode() {
        val cameraMonitorViewId : Int = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName())
        val webcam : OpenCvCamera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName::class.java, "Webcam 1"), cameraMonitorViewId)
//        webcam.openCameraDeviceAsync(object : AsyncCameraOpenListener {
//            override fun onOpened() {
//                //camera.setViewportRenderer(OpenCvCamera.ViewportRenderer.GPU_ACCELERATED);
//                webcam.startStreaming(640, 360, OpenCvCameraRotation.UPRIGHT)
//            }
//
//            override fun onError(errorCode: Int) {}
//        })

        telemetry.addLine("Waiting for start")
        telemetry.update()

        waitForStart()
    }
}