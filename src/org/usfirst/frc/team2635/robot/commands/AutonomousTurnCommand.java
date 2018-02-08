package org.usfirst.frc.team2635.robot.commands;

import org.usfirst.frc.team2635.robot.Robot;
import org.usfirst.frc.team2635.robot.RobotMap;
import org.usfirst.frc.team2635.robot.model.MotionMagicLibrary;
import org.usfirst.frc.team2635.robot.model.MotionParameters;


import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class AutonomousTurnCommand extends Command {

	MotionParameters rotationParams;
	
	double rpm;
	double targetAngle;
	double acceleration;
	double intialHeading; 
    public AutonomousTurnCommand(double rpm, double targetAngle, double acceleration) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drive);
    	this.rpm = rpm;
    	this.targetAngle = targetAngle;
    	this.acceleration = acceleration;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	
    	Robot.drive.reset();
    	Robot.drive.navxReset();
    	intialHeading = Robot.drive.getNavxHeading();
	   	rotationParams = MotionMagicLibrary.getRotationParameters(targetAngle,
				RobotMap.WHEEL_RADIUS_INCHES, RobotMap.WHEEL_SEPARATION_INCHES, rpm, acceleration);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	Robot.drive.motionMagic(rotationParams);  
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	boolean isFinished = Robot.drive.motionMagicDone(rotationParams, RobotMap.ERRORTOLERANCE);
    	if(isFinished) {
    		double currentHeading = Robot.drive.getNavxHeading();
    		double navxHeading = Math.abs(intialHeading-currentHeading);
    		System.out.println("Navx turn heading: " + navxHeading);
    		System.out.println("Navx turn angle: " + Robot.drive.getNavxAngle());
    		System.out.println("Drive Turn Finished");
    		System.out.println("-----------");
    	}
    	return isFinished;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.motorControl(ControlMode.PercentOutput, 0.0, 0.0, false);
    	Robot.drive.reset();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	
    	end();
    }
}
