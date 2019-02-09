package frc.robot;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.TimedRobot;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/*
To impliment pixy code into a different project, you must install this library.
https://github.com/PseudoResonance/Pixy2JavaAPI
Follow the instructions to edit build.graddle, the build the project while connected to the internet.
I've had issues with installing the library, but was able to get it to work by running vscode as administrator
then following the instructions and building the project.
This may not be necissary however.
*/

public class Robot extends TimedRobot {
    private Joystick driveController = new Joystick(0);
    
    private Spark frontLeft = new Spark(0);
    private Spark backLeft = new Spark(1);
    private Spark frontRight = new Spark(3);
    private Spark backRight = new Spark(2);

    private SpeedControllerGroup leftSide = new SpeedControllerGroup(frontLeft, backLeft);
    private SpeedControllerGroup rightSide = new SpeedControllerGroup(frontRight, backRight);

    private DifferentialDrive drive = new DifferentialDrive(leftSide, rightSide);

    private double motorMod = 1;

    //Define the pixy over SPI. I tried I2C and wasn't able to get it to work
    private Pixy2 pixy = Pixy2.createInstance(Pixy2.LinkType.SPI);
    //Proportional gain (ammount to increase turn per pixel off of center)
    private float pGain = 2f/260f;

    @Override
    public void robotInit() {
        //Initialize the pixy
        pixy.init();
        //Turn on the lamp lights. First byte is for the top lights, second byte is to turn on the RBG light to white
        pixy.setLamp((byte)1, (byte)1);
    }

    @Override
    public void robotPeriodic() {
        
    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void autonomousPeriodic() {
        //Load blocks from pixy. Loading from sigmap one (FRC Balls) and only getting a maximum of one object (the biggest)
        int result = pixy.getCCC().getBlocks(false, 1, 1);
        //Check for a pixy error
        if(result != -0){
            //Get the blocks that were previously loaded
            ArrayList<Block> blocks = pixy.getCCC().getBlocks();
            //If we have any blocks...
            if(blocks.size() > 0){
                //Get the first block, which should be the ball
                Block ball = blocks.get(0);
                //Store the width + calculate the error from driving straight at the ball
                int width = 316;
                int error = ball.getX() - width/2;
                //Drive at full speed at a direction determined by the error * the proportional gain
                drive.arcadeDrive(1, error*pGain);
            }else{
                drive.arcadeDrive(0,0);
            }
        }else{
            drive.arcadeDrive(0,0);
        }
    }

    @Override
    public void teleopInit() {
    }

    @Override
    public void teleopPeriodic() {
		drive.tankDrive(driveController.getRawAxis(5)*motorMod, driveController.getRawAxis(1)*motorMod);
		
		if(driveController.getRawAxis(2)>0.5){
			motorMod = 0.7f;
		}else if(driveController.getRawAxis(3)>0.5){
			motorMod = 1f;
		}else{
			motorMod = 0.85f;
		}
    }

    @Override
    public void testPeriodic() {
    }
}