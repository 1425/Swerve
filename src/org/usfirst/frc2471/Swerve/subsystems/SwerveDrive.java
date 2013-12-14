// RobotBuilder Version: 0.0.2
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in th future.
package org.usfirst.frc2471.Swerve.subsystems;
import com.sun.squawk.util.MathUtils;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc2471.Swerve.*;
import org.usfirst.frc2471.Swerve.RobotMap;
import org.usfirst.frc2471.Swerve.commands.*;
/**
 *
 */
class Filter {
    double samples[];
    int pos;
    int count;
    int m_numSamples;
    double sum;
    
    public Filter( int numSamples ) {
        m_numSamples = numSamples;
        samples = new double[numSamples];
        pos = 0;
        count = 0;
        sum = 0;
    }
    
    public void AddSample( double sample ) {
        pos = pos + 1;
        if (pos == m_numSamples) {
            pos = 0;
        }
        
        count = count + 1;
        if (count>m_numSamples) {
            count = m_numSamples;
            sum = sum - samples[pos];
        }
            
        sum = sum + sample;
        samples[pos] = sample;
    }
    
    public double GetAverage() {
        return sum / count;
    }
}

public class SwerveDrive extends PIDSubsystem  {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    SwerveVector rfVect, lfVect, rrVect, lrVect;
    double turnPower;
    double saveGyroAngle;
    double accelerometerAngle;
    double turnJoystickAngle;
    Filter accelFilterX;
    Filter accelFilterY;
    DashboardPID steerDashboardPID;
    boolean autoSteer;
    boolean fieldSteer, fieldMove;
    double prevXVelocity, prevYVelocity;
    
    public DashboardPID getSteerDashboardPID() {
        return steerDashboardPID;
    }
    
    public SwerveDrive() {
        super("Steer PID", -1.5, -0.0, -6.0);
        steerDashboardPID = new DashboardPID( "Steer", getPIDController() );
        //SmartDashboard.putData(this);  // I wish this worked, but it didn't seem to, so we rolled our own. (above)
        
        setInputRange( -Math.PI, Math.PI );
        getPIDController().setContinuous( true );
        setAbsoluteTolerance( Math.PI/180.0*5.0 );
        getPIDController().setOutputRange(-1.0, 1.0);
        enable();
        accelerometerAngle = 0.0;
        autoSteer = false;
        fieldSteer = false;
        fieldMove = true;
        SmartDashboard.putBoolean("FieldSteer", fieldSteer);
        SmartDashboard.putBoolean("FieldMove", fieldMove);
        prevXVelocity = 0;
        prevYVelocity = 0;
        
        lrVect= new SwerveVector(RobotMap.leftRearSwerve, -16.0,-11.0, -Math.PI/4.0); 
        lfVect= new SwerveVector(RobotMap.leftFrontSwerve, -16.0,11.0, Math.PI/4.0);  
        rrVect= new SwerveVector(RobotMap.rightRearSwerve, 16.0,-11.0, Math.PI/4.0); 
        rfVect= new SwerveVector(RobotMap.rightFrontSwerve, 16.0,11.0, -Math.PI/4.0);  
    }
    
    public void drive(double x, double y, double r, double s, double gyroAngle, double accelX, double accelY)
    {
        steerDashboardPID.update();
        fieldSteer = SmartDashboard.getBoolean("FieldSteer");
        fieldMove = SmartDashboard.getBoolean("FieldMove");

        autoSteer = Robot.oi.autoSteerButton.get();
        saveGyroAngle = gyroAngle;
        SmartDashboard.putNumber("gyroAngle", -gyroAngle);
        GetAccelerationFromJoyStick(x, y);
                 
        double magnitude = Math.sqrt( x*x + y*y );
        double turnMag = Math.sqrt( r*r + s*s );          
        if (magnitude < 0.1 && turnMag < 0.05 && !autoSteer) {
            lrVect.HandsOff();
            lfVect.HandsOff();
            rrVect.HandsOff();
            rfVect.HandsOff();
            disable();
            return;
        }
        else
        {
            if (turnMag > 0.05) {
                turnJoystickAngle = MathUtils.atan2( -r, s );  // convert the right stick to a goal angle for robot orientation
                SmartDashboard.putNumber("joyStickAngle", -turnJoystickAngle);
                if (!autoSteer) {
                    setSetpoint( turnJoystickAngle );
                    enable();
                }
            }
            if (autoSteer) {
                setSetpoint( accelerometerAngle );
                enable();
            }
        }

        if (!fieldSteer && !autoSteer) {
            turnPower = r;  // joystick direct turning
        }
        
        double tempGyro;
        if (fieldMove) {
            tempGyro = gyroAngle;
        }
        else {
            tempGyro = 0;
        }
        
        double lrPower = lrVect.drive(x, y, turnPower, tempGyro);
        double lfPower = lfVect.drive(x, y, turnPower, tempGyro);
        double rrPower = rrVect.drive(x, y, turnPower, tempGyro);
        double rfPower = rfVect.drive(x, y, turnPower, tempGyro);
        
        double maxPower = Math.max( 1.0, Math.max( lrPower, Math.max( lfPower, Math.max( rrPower, rfPower) ) ) );
        
        lrVect.SetMaxPower( maxPower );
        lfVect.SetMaxPower( maxPower );
        rrVect.SetMaxPower( maxPower );
        rfVect.SetMaxPower( maxPower );
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new DriveLoop());
    }
    
    protected double returnPIDInput() {
        return saveGyroAngle;
    }
    
    protected void usePIDOutput(double output) {
        //double error = getPIDController().getError();
        SmartDashboard.putNumber("TurnPower", output);
        if (fieldSteer || autoSteer) {
            turnPower = output;
        }
    }

    private void GetAccelerationFromJoyStick(double x, double y) {
        // use particle simulation to estimate acceleration from left joystick input
        double xVelocity = prevXVelocity + x;  // update velocity with accel from joystick
        double yVelocity = prevYVelocity + y;
        xVelocity = xVelocity * 0.85;  // viscous drag
        yVelocity = yVelocity * 0.85;
        double xAccel = xVelocity - prevXVelocity;  // compute new accel
        double yAccel = yVelocity - prevYVelocity;
        if (Math.abs(xAccel)>0.05 || Math.abs(yAccel)>0.05) {
           accelerometerAngle = MathUtils.atan2(-xAccel, yAccel);
           SmartDashboard.putNumber("accel angle", -accelerometerAngle);
        }
        prevXVelocity = xVelocity;
        prevYVelocity = yVelocity;
    }
}
