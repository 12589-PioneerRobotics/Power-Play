package org.firstinspires.ftc.teamcode.hardwaretests

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.drive.GamepadEventPS


@TeleOp(name = "Lift Test")
class LiftTest : OpMode() {
    private lateinit var lift: DcMotorEx
    private lateinit var wrist: Servo
    private lateinit var claw: Servo

    private lateinit var gamepadEvent: GamepadEventPS

    override fun init() {
        lift = hardwareMap.dcMotor.get("lift") as DcMotorEx
        claw = hardwareMap.servo.get("claw")
        wrist = hardwareMap.servo.get("wrist")

        lift.targetPosition = 0
        lift.power = 1.0
        lift.mode = DcMotor.RunMode.RUN_TO_POSITION
        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        claw.position = 0.0
        wrist.position = 0.0

        gamepadEvent = GamepadEventPS(gamepad1)

        telemetry.addLine("Initialized")
        telemetry.addData("Claw Port: ", claw.portNumber)
        telemetry.addData("Wrist Port: ", wrist.portNumber)
    }

    override fun loop() {
        if (gamepadEvent.dPadUp())
            lift.targetPosition += 500
        if (gamepadEvent.dPadDown())
            lift.targetPosition -= 500

        if (gamepadEvent.dPadRight())
            claw.position += 0.05
        if (gamepadEvent.dPadLeft())
            claw.position -= 0.05

        if (gamepadEvent.triangle())
            wrist.position += 0.05
        if (gamepadEvent.cross())
            wrist.position -= 0.05

        telemetry.addData("Lift Current Position: ", lift.currentPosition)
        telemetry.addData("Lift Target Position: ", lift.targetPosition)
        telemetry.addData("Claw Position: ", claw.position)
        telemetry.addData("Wrist Position: ", wrist.position)
        telemetry.update()
   }
}