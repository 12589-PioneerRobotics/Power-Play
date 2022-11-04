package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.drive.GamepadEventPS

@TeleOp(name = "Arm Test")
class ArmTest : OpMode() {
    private lateinit var rightArm: DcMotorEx
    private lateinit var leftArm: DcMotorEx
    private lateinit var claw: Servo
    private lateinit var wrist: CRServo

    private lateinit var gamepadEvent: GamepadEventPS

    override fun init() {
        rightArm = hardwareMap.get(DcMotorEx::class.java, "rightArm")
        leftArm = hardwareMap.get(DcMotorEx::class.java, "leftArm")
        claw = hardwareMap.get(Servo::class.java, "claw")
        wrist = hardwareMap.get(CRServo::class.java, "wrist")

        leftArm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        rightArm.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        gamepadEvent = GamepadEventPS(gamepad1)

        telemetry.addLine("Initialized")
    }

    override fun loop() {
        if (gamepad1.dpad_up) {
            Thread { rightArm.power = -0.5 }.start()
            Thread { leftArm.power = -0.5 }.start()
        } else if (gamepad1.dpad_down) {
            Thread { rightArm.power = 0.25 }.start()
            Thread { leftArm.power = 0.25 }.start()
        } else {
            Thread { rightArm.power = 0.0 }.start()
            Thread { leftArm.power = 0.0 }.start()
        }

        if (gamepad1.dpad_right) { claw.position -= 0.05 }

        else if (gamepad1.dpad_left) { claw.position += 0.05 }


        if (gamepad1.circle)
            wrist.power = 0.2
        else if (gamepad1.square)
            wrist.power = -0.2
        else
            wrist.power = 0.0

        telemetry.addData("rightArm Current Position: ", rightArm.currentPosition)
        telemetry.addData("rightArm Target Position: ", rightArm.targetPosition)
        telemetry.addData("leftArm Current Position: ", leftArm.currentPosition)
        telemetry.addData("leftArm Target Position: ", leftArm.targetPosition)
        telemetry.addData("Claw Position: ", claw.position)
        telemetry.update()
    }
}