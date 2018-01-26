package org.firstinspires.ftc.teamcode.relicrecoveryv2;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;

import java.util.Random;

/**
 * Created by TPR on 12/14/17.
 */
@TeleOp(name="RelicTelyOp",group="Jeff" )
public class RelicTelyMode  extends MeccyMode{
    //
    //PengwinFin pengwinFin;
    PengwinWing pengwinWing;
    //
    //<editor-fold desc="Startify">
    VuforiaLocalizer vuforia;
    OpenGLMatrix lastLocation = null;
    //
    ElapsedTime time = new ElapsedTime();
    //
    private Random randy = new Random();
    //
    double degreeOfRobotPower = 1;
    DrivingAction drivingAction = DrivingAction.Driving;
    GrabberUp grabberUp = GrabberUp.push;
    GrabberDown grabberDown = GrabberDown.push;
    //
    //<editor-fold desc="Controls"
    double leftX;
    double leftY;
    double rightX;
    boolean halfPower;
    boolean quarterPower;
    //
    boolean liftup;
    boolean liftdown;
    boolean retract;
    boolean extend;
    boolean halfSLPower;
    boolean left;
    boolean right;
    double pushAbove;
    double pushBelow;
    double down;
    double up;
    double Rup;
    double Rdown;
    //</editor-fold>
    //
    //</editor-fold>
    //
    public void runOpMode() {
        //<editor-fold desc="Initialize">
        //pengwinFin = new PengwinFin(hardwareMap);
        pengwinWing = new PengwinWing(hardwareMap);
        //<editor-fold desc="Vuforia">
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        //
        parameters.vuforiaLicenseKey = "AbxR5+T/////AAAAGR1YlvU/6EDzrJvG5EfPnXSFutoBr1aCusr0K3pKqPuWTBQsUb0mv5irjoX2Xf/GFvAvHyw8v1GBYgHwE+hNTcNj05kw3juX+Ur4l3HNnp5SfXV/8fave0xB7yVYZ/LBDraNnYXiuT+D/5iGfQ99PVVao3LI4uGUOvL9+3vbPqtTXLowqFJX5uE7R/W4iLmNqHgTCSzWcm/J1CzwWuOPD252FDE9lutdDVRri17DBX0C/D4mt6BdI5CpxhG6ZR0tm6Zh2uvljnCK6N42V5x/kXd+UrBgyP43CBAACQqgP6MEvQylUD58U4PeTUWe9Q4o6Xrx9QEwlr8v+pmi9nevKnmE2CrPPwQePkDUqradHHnU";
        //
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;//set camera (front)
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        //
        //Load ciphers:
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate");
        //</editor-fold>
        //
        leftBackMotor = hardwareMap.dcMotor.get("lback"); //left back
        rightBackMotor = hardwareMap.dcMotor.get("rback"); //right back
        leftFrontMotor = hardwareMap.dcMotor.get("lfront"); //left front
        rightFrontMotor = hardwareMap.dcMotor.get("rfront"); //right front
        configureMotors();
        //
        BNO055IMU imu;
        Orientation angles;
        Acceleration gravity;
        //</editor-fold>
        //
        waitForStartify();
        //
        telemetry.log().clear();
        //
        relicTrackables.activate();
        //
        time.reset();
        //
        while (opModeIsActive()) {
            //<editor-fold desc="Update">
            //Joey's controller
            leftX = gamepad1.left_stick_x;
            leftY = gamepad1.left_stick_y;
            rightX = gamepad1.right_stick_x;
            halfPower = gamepad1.right_bumper;
            quarterPower = gamepad1.left_bumper;
            //Meg's controller
            /*liftup = gamepad2.dpad_up;
            liftdown = gamepad2.dpad_down;
            retract = gamepad2.x;
            extend = gamepad2.y;
            halfSLPower = gamepad2.b;*/
            up = gamepad2.left_stick_y;
            down = gamepad2.right_stick_y;
            Rup = gamepad2.left_stick_x;
            Rdown = gamepad2.right_stick_x;

            left = !(gamepad2.left_trigger == 0);
            right = !(gamepad2.right_trigger == 0);
            //
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            //</editor-fold>
            //
            setDegreePower();
            //
            switchs();
            //
            moveTheRobot();
            //
            telemetryJazz(vuMark);
            //
        }
    }
    //
    //<editor-fold desc="Functions">
    private void setDegreePower() {
        if (halfPower) {
            degreeOfRobotPower = 0.5;
        }
        else if(quarterPower){
            degreeOfRobotPower = 0.25;
        }else{
            degreeOfRobotPower = 1.0;
        }
    }
    //
    private void switchs(){
        if (!(rightX == 0)){
            drivingAction = DrivingAction.Turning;
        }else if(Math.abs(leftX) > .15){
            drivingAction = DrivingAction.Strafing;
        }else{
            drivingAction = DrivingAction.Driving;
        }
        //
        if (Math.abs(up) > Math.abs(Rup)){
            grabberUp = GrabberUp.push;
        }else {
            grabberUp = GrabberUp.rotate;
        }
        //
        if (Math.abs(down) > Math.abs(Rdown)){
            grabberDown = GrabberDown.push;
        }else{
            grabberDown = GrabberDown.rotate;
        }
    }
    //
    private void moveTheRobot() {
        switch (drivingAction){
            case Driving:
                    drive(-leftY * degreeOfRobotPower);
                break;
            case Turning:
                    turn(rightX * degreeOfRobotPower);
                break;
            case Strafing:
                    strafe(leftX * degreeOfRobotPower, -leftY * degreeOfRobotPower);
                break;
        }
        //
        if (left){
            pengwinWing.upLeft(up);
            pengwinWing.upRight(0);
            //pengwinWing.downLeft(down);
        }else if (right){
            pengwinWing.upLeft(0);
            pengwinWing.upRight(up);
            //pengwinWing.downRight(down);
        }else{
            switch (grabberUp){
                case push:
                    pengwinWing.upLeft(up);
                    pengwinWing.upRight(up);
                    break;
                case rotate:
                    pengwinWing.upLeft(-Rup);
                    pengwinWing.upRight(Rup);
                    break;
            }
            switch (grabberDown){
                case push:
                    /*pengwinWing.downLeft(down);
                    pengwinWing.downRight(down);*/
                    break;
                case rotate:
                    /*pengwinWing.downLeft(-Rdown);
                    pengwinWing.downRight(Rdown);*/
            }
        }
    }
    //
    private void telemetryJazz(RelicRecoveryVuMark vuMark) {
        telemetry.addData("Unicorn Crossing", time.milliseconds());
        if (vuMark != RelicRecoveryVuMark.UNKNOWN) {
            //
            telemetry.addData("VuMark", vuMark);
            //
        }else {
            telemetry.addData("VuMark", "None");
        }
        telemetry.update();
    }
    //</editor-fold>
}