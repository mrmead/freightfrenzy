/*
Code by Brison

--------------------------------
DO NOT EDIT!!!!! MAKE COPY!!!!!!
--------------------------------

made 4th quarter 2021

********************DRIVER INSTRUCTIONS********************

GAMEPAD I :
Left Joystick: Directional movement (Straffing included)
Right Joystick: Rotational movement
Left Bumper: Counter-clockwise Duck wheel rotation
Right Bumper: Clockwise Duck wheel rotation


GAMEPAD II :
B: Reset to starting/ready position
Right trigger: Input Star wheel rotation
Left trigger: Reverse Input Star wheel rotation
DPad Left: 
    1. Raises Intake block inward
    2. Lowers Claw to grabbing position
    3. Sets Claw to OPEN position
DPad Right:
    1. Raises Claw to lifting/defauly position
    2. Lowers Intake block outward (Ready to intake freight)
DPad Up:
    1. Sets Claw to CLOSED postion (Grabs)
    2. Raises Linear Slide Mechanism
    3. Raises Claw to OUTPUT position (Ready to deposit to XMas Tree)
DPad Down:
    1. Sets Claw to OPEN position (Releases freight)
    2. Waits
    3. Sets Claw to CLOSED position
    4. Lowers Claw to default position
    5. Lowers Linear Slide Mechanism
    
********************THANK YOU FOR READING INSTRUCTIONS********************

--------------------------------
       DO NOT EDIT!!!!!!!!!!!
--------------------------------

*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOpFF InMoment", group="")

public class TeleOpFF_Copy extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    private DcMotor duck;
    private DcMotor intake;
    private DcMotor slide;
    private Servo pivot;
    private Servo claw;
    private Servo rightIntake;
    private Servo leftIntake;
    private double angle;
    private double magnitude;
    private double magnitudeA;
    private double magnitudeB;
    private double direction;
    private double posChange;
    private double prePos = 0;
    private double postPos = 0;
    private double frontRightPower = 0;
    private double backLeftPower = 0;
    private double frontLeftPower = 0;
    private double backRightPower = 0;
    private double duckPower = 0;
    private float intakePower = 0;
    private double linearPower = 0;
    private int intakeToggle = 0;
    private double sprint = 1.5;
    private int tickCount = 1120;
    private boolean manualToggle = false;
    //1120 ticks == 40 mm
    //5684 ticks to top
    
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        frontLeft  = hardwareMap.get(DcMotorEx.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        intake = hardwareMap.get(DcMotor.class, "intake");
        duck = hardwareMap.get(DcMotor.class, "duck");
        slide = hardwareMap.get(DcMotor.class, "slide");
        pivot = hardwareMap.get(Servo.class, "pivot");
        claw = hardwareMap.get(Servo.class, "claw");
        leftIntake = hardwareMap.get(Servo.class, "leftIntake");
        rightIntake = hardwareMap.get(Servo.class, "rightIntake");
        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        
        waitForStart();
        runtime.reset();
        
        
        
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        duck.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        
        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        duck.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
        
        claw.setPosition(0);
                    leftIntake.setPosition(1);
                    rightIntake.setPosition(0);
                    pivot.setPosition(0.1);
                    slide.setTargetPosition(0);
                    slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx)slide).setVelocity(1120);
                    if(slide.getCurrentPosition() == slide.getTargetPosition()) {
                        ((DcMotorEx)slide).setVelocity(0);
                        slide.setPower(0);
                    }
        
        
        while (opModeIsActive()) {
             
            if (gamepad2.back) {
                if (manualToggle == false) {
                    manualToggle = true;
                } else
                    manualToggle = false;
            }
            
            magnitudeB = gamepad1.right_stick_x;
            
            magnitude = Math.hypot(Math.abs(gamepad1.left_stick_x), Math.abs(gamepad1.left_stick_y));
            
            if (gamepad1.right_bumper) {
                duckPower = 1;
            } else if (gamepad1.left_bumper) {
                duckPower = -1;
            } else
                duckPower = 0;
            
            if (magnitude > 1) {
                magnitude = 1;
            }
            /*
            
            --------------------------------
            DO NOT EDIT!!!!! MAKE COPY!!!!!!
            --------------------------------
            
            */
            angle = Math.toDegrees(Math.atan2(gamepad1.left_stick_x, -gamepad1.left_stick_y));
            
            if (gamepad1.left_stick_y == 0 && gamepad1.left_stick_x == 0) {
                angle = 0;
            }
            
            if (angle == 0) {
                frontRightPower = magnitude - magnitudeB;
                backRightPower = magnitude - magnitudeB;
                frontLeftPower = magnitude + magnitudeB;
                backLeftPower = magnitude + magnitudeB;
            }
            
            if (angle == 180) {
                frontRightPower = -magnitude - magnitudeB;
                backRightPower = -magnitude - magnitudeB;
                frontLeftPower = -magnitude + magnitudeB;
                backLeftPower = -magnitude + magnitudeB;
            }
            /*
            
            --------------------------------
            DO NOT EDIT!!!!! MAKE COPY!!!!!!
            --------------------------------
            
            */
            if (angle == 90) {
                frontRightPower = -magnitude - magnitudeB;
                backRightPower = magnitude - magnitudeB;
                frontLeftPower = magnitude + magnitudeB;
                backLeftPower = -magnitude + magnitudeB;
            }
            
            if (angle == -90) {
                frontRightPower = magnitude - magnitudeB;
                backRightPower = -magnitude - magnitudeB;
                frontLeftPower = -magnitude + magnitudeB;
                backLeftPower = magnitude + magnitudeB;
            }
            
            if (angle > -90 && angle < 0) {
                frontRightPower = magnitude - magnitudeB;
                backLeftPower = magnitude + magnitudeB;
                frontLeftPower = (0.0222 * angle + 1) * magnitude + magnitudeB;
                backRightPower = (0.0222 * angle + 1) * magnitude - magnitudeB;
            }
            
            if (angle > 0 && angle < 90) {
                frontLeftPower = magnitude + magnitudeB;
                backRightPower = magnitude - magnitudeB;
                frontRightPower = (-0.0222 * angle + 1) * magnitude - magnitudeB;
                backLeftPower = (-0.0222 * angle + 1) * magnitude + magnitudeB;
            }
            
            if (angle > 90 && angle < 180) {
                frontRightPower = -magnitude - magnitudeB;
                backLeftPower = -magnitude + magnitudeB;
                frontLeftPower = (-0.0222 * angle + 3) * magnitude + magnitudeB;
                backRightPower = (-0.0222 * angle + 3) * magnitude - magnitudeB;
            }
            
            if (angle > -180 && angle < -90) {
                frontLeftPower = -magnitude + magnitudeB;
                backRightPower = -magnitude - magnitudeB;
                frontRightPower = (0.0222 * angle + 3) * magnitude - magnitudeB;
                backLeftPower = (0.0222 * angle + 3) * magnitude + magnitudeB;
            }
            /*
            
            --------------------------------
            DO NOT EDIT!!!!! MAKE COPY!!!!!!
            --------------------------------
            
            */
            
            if (gamepad1.right_stick_button) {
                if (sprint == 1.5) {
                    sprint = 0.8;
                } else if (sprint == 0.8) {
                    sprint = 1.5;
                }
            }
            
            frontRight.setPower(frontRightPower * 0.6);
            backRight.setPower(backRightPower * 0.6);
            frontLeft.setPower(frontLeftPower * 0.6);
            backLeft.setPower(backLeftPower * 0.6);

            if (gamepad1.right_bumper == false && gamepad1.left_bumper == false) {
                duckPower = 0;
                duck.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            } else if(gamepad1.right_bumper == true && gamepad1.left_bumper == false){
                duckPower = 0.46;
            } else if(gamepad1.right_bumper == false && gamepad1.left_bumper == true){
                duckPower = -.46;
            }
            
            duck.setPower(duckPower);
            
            intakePower = gamepad2.right_trigger - gamepad2.left_trigger;
            intake.setPower(intakePower * 1.5);
            
            //gamepad 2 algs
            //if (manualToggle == false) {
            
                if (gamepad2.b) {
                    claw.setPosition(0);
                    leftIntake.setPosition(1);
                    rightIntake.setPosition(0);
                    pivot.setPosition(0.1);
                    slide.setTargetPosition(0);
                    slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx)slide).setVelocity(1120);
                    if(slide.getCurrentPosition() == slide.getTargetPosition()) {
                        ((DcMotorEx)slide).setVelocity(0);
                        slide.setPower(0);
                    }
                }
                /*
            
            --------------------------------
            DO NOT EDIT!!!!! MAKE COPY!!!!!!
            --------------------------------
            */
            
                if (gamepad2.dpad_left) {
                    leftIntake.setPosition(1);
                    rightIntake.setPosition(0);
                    pivot.setPosition(0.1);
                    sleep(500);
                    claw.setPosition(0.1);
                    
                }
                
                if (gamepad2.dpad_right) {
                    pivot.setPosition(0.8);
                    leftIntake.setPosition(0.45);
                    rightIntake.setPosition(0.55);
                }
                
                if (gamepad2.dpad_up) {
                    claw.setPosition(0);
                    sleep(250);
                    if (slide.getCurrentPosition() == 0) {
                        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    }
                    slide.setTargetPosition(3000);
                    slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx)slide).setVelocity(1120);
    
                    if(slide.getCurrentPosition() == slide.getTargetPosition()) {
                        ((DcMotorEx)slide).setVelocity(0);
                        slide.setPower(0);
                        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    }
                    sleep(250);
                    pivot.setPosition(0.8);
                }
                
                if (gamepad2.dpad_down) {
                    leftIntake.setPosition(0.45);
                    rightIntake.setPosition(0.55);
                    claw.setPosition(0.1);
                    sleep(250);
                    claw.setPosition(0);
                    pivot.setPosition(0.5);
                    slide.setTargetPosition(0);
                    slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx)slide).setVelocity(1120);
                    if(slide.getCurrentPosition() == slide.getTargetPosition()) {
                        ((DcMotorEx)slide).setVelocity(0);
                        slide.setPower(0);
                    }
                } 
                
                /*
            
            --------------------------------
            DO NOT EDIT!!!!! MAKE COPY!!!!!!
            --------------------------------
            
            */
            
            //manual
                /*
                if (gamepad2.dpad_up) {
                    if (slide.getCurrentPosition() == 0) {
                        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    }
                    slide.setTargetPosition(2500);
                    slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    ((DcMotorEx)slide).setVelocity(1120);
    
                    if(slide.getCurrentPosition() == slide.getTargetPosition()) {
                        ((DcMotorEx)slide).setVelocity(0);
                        slide.setPower(0);
                        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                    }
                }
                
                if (gamepad2.dpad_down) {
                    slide.setTargetPosition(0);
                    ((DcMotorEx)slide).setVelocity(1120);
                    if(slide.getCurrentPosition() == slide.getTargetPosition()) {
                        ((DcMotorEx)slide).setVelocity(0);
                        slide.setPower(0);
                    }
                }
                
                if (gamepad2.dpad_left) {
                    pivot.setPosition(-0.8);
                }
    
                if (gamepad2.dpad_right) {
                    pivot.setPosition(0.8);
                }
                
                if (gamepad2.right_bumper) {
                    claw.setPosition(0.1);
                }
                
                if (gamepad2.left_bumper) {
                    claw.setPosition(0);
                }
                
                if (gamepad2.x) {
                    leftIntake.setPosition(1);
                    rightIntake.setPosition(0);
                }
                
                if (gamepad2.y) {
                    leftIntake.setPosition(0.45);
                    rightIntake.setPosition(0.55);
                } 
            //} toggle else
            */
            if (magnitude == 0 && magnitudeB == 0) {
                frontRight.setPower(0);
                frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backRight.setPower(0);
                backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                frontLeft.setPower(0);
                frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backLeft.setPower(0);
                backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            }
            
            telemetry.addData("Positions", "frontLeft (%.2f), frontRight (%.2f)", frontLeftPower, frontRightPower);
            telemetry.addData("Positions", "backLeft (%.2f), backRight (%.2f)", backLeftPower, backRightPower);
            telemetry.addData("magnitude", magnitude);
            telemetry.addData("angle", angle);
            telemetry.addData("StickY", gamepad1.left_stick_y);
            telemetry.addData("StickX", gamepad1.left_stick_x);
            telemetry.addData("ManualToggle", manualToggle);
            telemetry.update();
        }
    }
}
            /*
            
            --------------------------------
            DO NOT EDIT!!!!! MAKE COPY!!!!!!
            --------------------------------
            
            */
