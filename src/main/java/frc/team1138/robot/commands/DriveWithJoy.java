package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.OI;
import frc.team1138.robot.Robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;

/**
 * @author Zheyuan Hu
 * @version 1.0.0 This Command requires Robot.SUB_DRIVE_BASE
 */
public class DriveWithJoy extends Command
{
	private OI oi;

	public DriveWithJoy()
	{
		// TODO Auto-generated constructor stub
		requires(Robot.SUB_DRIVE_BASE);
		oi = new OI();
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
		// two hand or one hand
		if (Robot.SUB_DRIVE_BASE.getLiftState() == DoubleSolenoid.Value.kForward)
			Robot.SUB_DRIVE_BASE.tankDrive(oi.getLeftControllerY(), oi.getRightControllerY());
		else
			Robot.SUB_DRIVE_BASE.tankDrive(oi.getLeftControllerY(), oi.getRightControllerY());
		double offset = SmartDashboard.getNumber("setAngle", 0) - Robot.SUB_DRIVE_BASE.getAngle();
		// if (Robot.SUB_DRIVE_BASE.getLiftState() == DoubleSolenoid.Value.kForward){
		// Robot.SUB_DRIVE_BASE.tankDrive(oi.getLeftControllerY(),
		// oi.getRightControllerY());
		// }
		// else {
		// Robot.SUB_DRIVE_BASE.drive(oi.getRightControllerY(), offset*0.7); // TODO
		// PLease Fix
		// }
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
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
	}
}
