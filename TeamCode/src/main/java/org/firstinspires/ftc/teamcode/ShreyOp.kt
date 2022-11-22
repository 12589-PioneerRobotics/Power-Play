package org.firstinspires.ftc.teamcode

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.outoftheboxrobotics.photoncore.PhotonCore
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive

@TeleOp(name = "ShreyOp")
class ShreyOp : OpMode() {
    var drive: SampleMecanumDrive? = null
    override fun init() {
        telemetry.addLine("Initialized!")
        telemetry.update()
        drive = SampleMecanumDrive(hardwareMap)
    }

    override fun loop() {
        PhotonCore.enable()
        drive?.setWeightedDrivePower(Pose2d(
            -gamepad1.left_stick_y.toDouble(),
            -gamepad1.left_stick_x.toDouble(),
            -gamepad1.right_stick_x.toDouble()
        ))
    }
}