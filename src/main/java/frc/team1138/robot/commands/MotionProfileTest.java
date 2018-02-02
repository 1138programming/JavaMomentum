package frc.team1138.robot.commands;

import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;

import MotionProfile.LeftAutonProfile;
import MotionProfile.MotionProfile;
import MotionProfile.ProfileExecutor;
import MotionProfile.RightAutonProfile;
import edu.wpi.first.wpilibj.command.Command;

import frc.team1138.robot.Robot;

/**
 * @author Zheyuan Hu
 * @version 1.0.0
 */
public class MotionProfileTest extends Command
{
	private ProfileExecutor leftExecutor, rightExecutor; 
	private MotionProfile leftProfile, rightProfile; 
	public MotionProfileTest()
	{
		// Use requires() here to declare subsystem dependencies
		requires(Robot.SUB_DRIVE_BASE);
		leftProfile = new LeftAutonProfile();
		rightProfile = new RightAutonProfile(); 
		
		leftExecutor = new ProfileExecutor(Robot.SUB_DRIVE_BASE.getLeftMotor(), leftProfile);
		rightExecutor = new ProfileExecutor(Robot.SUB_DRIVE_BASE.getRightMotor(), rightProfile);
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
		leftExecutor.startMotionProfile();
		rightExecutor.startMotionProfile();
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute()
	{
		leftExecutor.controlMotionProfile();
		rightExecutor.controlMotionProfile();
		Robot.SUB_DRIVE_BASE.setLeftMotionControl(ControlMode.MotionProfile, leftExecutor.getSetValue().value);
		Robot.SUB_DRIVE_BASE.setRightMotionControl(ControlMode.MotionProfile, rightExecutor.getSetValue().value);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		return (leftExecutor.getSetValue() == SetValueMotionProfile.Hold && rightExecutor.getSetValue() == SetValueMotionProfile.Hold);
	}

	// Called once after isFinished returns true
	@Override
	protected void end()
	{
		leftExecutor.resetMotionProfile();
		rightExecutor.resetMotionProfile();
		Robot.SUB_DRIVE_BASE.setLeftMotionControl(ControlMode.PercentOutput, 0);
		Robot.SUB_DRIVE_BASE.setRightMotionControl(ControlMode.PercentOutput, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
		super.interrupted();
		end();
	}
}
