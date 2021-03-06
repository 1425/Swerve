// RobotBuilder Version: 0.0.2
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.
package org.usfirst.frc2471.Swerve;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc2471.Swerve.commands.*;
import org.usfirst.frc2471.Swerve.subsystems.EightBallGrabber;
import org.usfirst.frc2471.Swerve.subsystems.ShooterFan;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    Command autonomousCommand;
    SendableChooser autoChooser;
    
    public static ShooterFan shooterFan;
    public static EightBallGrabber eightBallGrabber;
    public static OI oi;
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public static SwerveModule swerveModule;
    public Preferences prefs;
    
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        
	RobotMap.init();
        autoChooser = new SendableChooser();
        autoChooser.addDefault("Simple Autonomous", new SimpleAutonomous());
        //autoChooser.addObject("name", );
        //autoChooser.addObject("name", );
        SmartDashboard.putData("Autonomous Chooser", autoChooser);
        shooterFan = new ShooterFan();
        eightBallGrabber = new EightBallGrabber();
        prefs = Preferences.getInstance();

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();
        
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=AUTONOMOUS
        
        // Setup compass to stream data
    }
    public void autonomousInit() {
        // schedule the autonomous command (example)
        autonomousCommand = (Command)autoChooser.getSelected();
        if (autonomousCommand != null) {
            autonomousCommand.start();
        }
    }
    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }
    public void teleopInit() {
	// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        
        if (autonomousCommand != null) autonomousCommand.cancel();
        RobotMap.leftRearTwistEnc.setDistancePerPulse(Math.PI * 2.0 / 1250.0);
        RobotMap.leftRearTwistEnc.start();
        RobotMap.gyro.reset();
    }
    
    public void disabledInit() {
//        System.out.println("saved");
        //RobotMap.swerve.getSteerDashboardPID().save();
        RobotMap.rightRearSwerve.getTwistDashboardPID().save();
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        Scheduler.getInstance().run();
//        RobotMap.leftRearTwist.set(oi.getJoystick1().getX());
//        System.out.println("Encoder: " + RobotMap.leftRearTwistEnc.getDistance());

        //getCompass();
        
//        if (Robot.oi.shootButton.get()) { 
//            Robot.shooterFan.Shoot(-100.0);
//        }
//        else {
//            Robot.shooterFan.Shoot(0.0);
//        }
        

    }
    
//    public void getCompass() {
//        byte[] outBuff = new byte[] {0x11,0x10};
//        byte[] inBuff = new byte[] {65,0x00};
//        int heading;
//        boolean rtn;
//        try {
//            rtn = RobotMap.compassI2C.addressOnly();
//            if(rtn) {
////                System.out.println("addressOnly rtn: " + rtn);
//            }
//            inBuff = new byte[] {'A'};
//            rtn = RobotMap.compassI2C.transaction(inBuff, 1, outBuff, 0);
//            if(rtn) {
//                System.out.println("write rtn: " + rtn);
//            }
//            Thread.sleep(10);
//            rtn = RobotMap.compassI2C.transaction(inBuff, 0, outBuff, 2);
//            if(rtn) {
//                System.out.println("read rtn: " + rtn);
//            }
//            heading = (outBuff[0] << 8);
//            heading = heading + outBuff[1];
//            heading = heading / 10;
//            System.out.println("Heading: " + heading);
//        }
//        catch (Exception e) {
////            System.out.println("I2C execption");
//        }
//        
//    }
    
    /**
     * This function called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
//        getCompass();
    }
    
    public void disabledPeriodic() {
//        getCompass();
        //System.out.println("Accel: " + RobotMap.accel.getAcceleration(ADXL345_I2C.Axes.kX));
//        System.out.println("leftFront: " + RobotMap.leftFrontSpeedEnc.get());
//        System.out.println("RightFront: " + RobotMap.rightFrontSpeedEnc.get());
//        System.out.println("leftRear: " + RobotMap.leftRearSpeedEnc.get());
//        System.out.println("rightRear: " + RobotMap.rightRearSpeedEnc.get());
    }
}
