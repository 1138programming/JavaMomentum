package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.Robot;

/**
 * @author Christopher Herrera
 * @version 1.0.0
 */
public class TestAutonomousCommand extends Command
{

	public TestAutonomousCommand()
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

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute()
	{
		final double DistFromTarget = Robot.SUB_DRIVE_BASE.UpdateTurnSpeed(90, 90);
		SmartDashboard.putNumber("Distance From Target", DistFromTarget);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		return true;
	}

	// Called once after isFinished returns true
	@Override
	protected void end()
	{
		Robot.SUB_DRIVE_BASE.resetGyro();
		Robot.SUB_DRIVE_BASE.tankDrive(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
	}
}
