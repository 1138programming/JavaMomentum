package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.OI;
import frc.team1138.robot.Robot;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

import frc.team1138.robot.Robot;

/**
 * @author Christopher Herrera
 * @version 1.0.0
 */
public class TestAutonomousCommand extends Command {
	double DistFromTarget = 0;
	
	public TestAutonomousCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.SUB_DRIVE_BASE);
		Robot.SUB_DRIVE_BASE.resetGyro();
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}
	
	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double TargetAngle = SmartDashboard.getNumber("setAngle", 0);
		DistFromTarget = Robot.SUB_DRIVE_BASE.UpdateTurnSpeed(TargetAngle, 180);
		SmartDashboard.putNumber("Distance From Target", DistFromTarget);
		SmartDashboard.putNumber("Gyro", Robot.SUB_DRIVE_BASE.getAngle());
		//flag++;
		//Robot.SUB_DRIVE_BASE.tankDrive(1.0, 1.0);
		//SmartDashboard.putNumber("Running", flag);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return DistFromTarget <= 1.5;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.SUB_DRIVE_BASE.resetGyro();
		Robot.SUB_DRIVE_BASE.tankDrive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
