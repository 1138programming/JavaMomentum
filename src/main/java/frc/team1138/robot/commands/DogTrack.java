package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DogTrack extends Command
{
	private TurnWithGyro turn = null;

	public DogTrack()
	{
		// Use requires() here to declare subsystem dependencies
		// requires(Robot.SUB_DRIVE_BASE);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
		double targetAngle = SmartDashboard.getNumber("setAngle", 0);
		turn = new TurnWithGyro();
		turn.start();
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
		if (turn != null && !turn.isFinished())
		{
			turn.end();
		}
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
	}
}
