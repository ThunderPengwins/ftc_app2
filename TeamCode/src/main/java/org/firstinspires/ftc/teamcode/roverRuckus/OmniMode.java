package org.firstinspires.ftc.teamcode.roverRuckus;

import android.hardware.Sensor;

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
    public void configureMotors(){
        right.setDirection(DcMotorSimple.Direction.REVERSE);
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
        left.setPower(input);
        right.setPower(input);
    }
    //
    public void turn(double input){
        left.setPower(input);
        right.setPower(-input);
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
}
