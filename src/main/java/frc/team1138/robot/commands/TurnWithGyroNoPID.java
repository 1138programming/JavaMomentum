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
public class TurnWithGyroNoPID extends Command
{
	public TurnWithGyroNoPID()
	{
		// Use requires() here to declare subsystem dependencies
		requires(Robot.SUB_DRIVE_BASE);
		Robot.SUB_DRIVE_BASE.resetGyro();
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
	}

	protected double UpdateTurnSpeed(double TargetAngle, double LogValue)
	{
		double ModifiedAngle = Robot.SUB_DRIVE_BASE.getAngle();

		while (ModifiedAngle < 0)
			ModifiedAngle += 360;
		while (TargetAngle < 0)
			TargetAngle += 360;

		double CounterClockwiseAngle = TargetAngle > ModifiedAngle ? TargetAngle - ModifiedAngle
				: (360 - ModifiedAngle) + TargetAngle;
		double ClockwiseAngle = TargetAngle > ModifiedAngle ? (360 - TargetAngle) + ModifiedAngle
				: ModifiedAngle - TargetAngle;
		// Determines the angle of the right and left turns needed to reach the target
		// angle in order to later decide the shortest turn

		double Speed = Math.log(Math.abs(ModifiedAngle - TargetAngle)) / Math.log(LogValue + 1);
		// As the difference between the target angle and the robot's angle gets
		// smaller, the robot turns more slowly in order to minimize overshoot

		if (CounterClockwiseAngle < ClockwiseAngle)
			Robot.SUB_DRIVE_BASE.tankDrive(Speed, -Speed); // Turn counter clockwise
		else
			Robot.SUB_DRIVE_BASE.tankDrive(-Speed, Speed); // Turn clockwise

		return Math.abs(TargetAngle - ModifiedAngle); // Return the distance from the target angle
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute()
	{
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end()
	{
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
	}
}
