package org.firstinspires.ftc.teamcode.roverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public abstract class OmniMode extends LinearOpMode {
    //
    public abstract void runOpMode();
    //
    DcMotor left;
    DcMotor right;
    //
    public void configureMotors(){//remember to switch this back
        left.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    //
    public boolean likeToMoveIt(){
        return left.isBusy() &&
        right.isBusy();
    }
    //
    public void waitForStartify(){
        waitForStart();
    }
    //
    public void drive(double input){
        withoutEncoder();
        left.setPower(input);
        right.setPower(input);
    }
    //
    public void turn(double input){
        withoutEncoder();
        left.setPower(input);
        right.setPower(-input);
    }
    //
    public void turnWithEncoder(double input){
        withEncoder();
        left.setPower(input);
        right.setPower(-input);
    }
    //
    public void shimmyWithEncoder(double input, int motor){
        withEncoder();
        //
        if (motor == 0) {
            left.setPower(input);
            right.setPower(input / 3);
        }else {
            right.setPower(input);
            left.setPower(input / 3);
        }
    }
    //
    public void resetEncoder(){
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    //
    public void withEncoder(){
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //
    public void withoutEncoder(){
        left.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    //
    public void toPosition(){
        left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        right.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    //
    public void telInit(String message){
        telemetry.addData("Initializing", message);
        telemetry.update();
    }
    //
    public void telMove(String message){
        telemetry.addData("Move", message);
        telemetry.update();
    }
    //
    public void countdown (Integer seconds){
        for (int i = 0; i < seconds; i++){
            telemetry.addData("T-minus", seconds - i);
            telemetry.update();
            sleep(1000);
        }
    }
    //
}