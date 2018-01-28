package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.Robot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;

public class DriveForward extends PIDCommand
{
	private static double P = 0.0002, I = 0.0, D = 0.0001;
	private PIDController driveController;
	double DistanceToTarget = 0;
	double ticksPerRotation = 4096;

	public DriveForward()
	{
		super("Drive Distance", P, I, D);
		SmartDashboard.putNumber("setEncoder", 0);
		requires(Robot.SUB_DRIVE_BASE);
		driveController = this.getPIDController();
		driveController.setInputRange(-20000000, 20000000);
		driveController.setOutputRange(-1, 1);
		driveController.setAbsoluteTolerance(100); // Error allowed
		driveController.setContinuous(true);
		Robot.SUB_DRIVE_BASE.resetEncoders();
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
		Robot.SUB_DRIVE_BASE.resetEncoders();
		setTarget(11 * ticksPerRotation);
	}

	@Override
	protected double returnPIDInput()
	{
		// TODO Auto-generated method stub
		// SmartDashboard.putNumber("Error",
		// (Robot.SUB_DRIVE_BASE.getAngle()-initialAngle));
		return (Robot.SUB_DRIVE_BASE.getRightEncoderValue());
	}

	public void setTarget(double ticks)
	{
		this.driveController.setSetpoint(ticks);
	}

	@Override
	protected void usePIDOutput(double output)
	{
		// The side with the GEAR IS THE FRONT!
		if (!driveController.onTarget())
		{
			if (this.returnPIDInput() - this.getSetpoint() < 0)
			{ // need to move forward
				System.out.println("Move Forward");
				System.out.println("Left Motor: " + (output) + "Right Motor: " + (output));
				SmartDashboard.putNumber("Distance From Target", this.returnPIDInput() - this.getSetpoint());
				Robot.SUB_DRIVE_BASE.tankDrive(-output, -output);
			}
			else if (this.returnPIDInput() - this.getSetpoint() > 0)
			{ // need to move backward
				System.out.println("Move Backward");
				System.out.println("Left Motor: " + (-output) + "Right Motor: " + (-output));
				Robot.SUB_DRIVE_BASE.tankDrive(-output, -output);
			}
			System.out.println("set point: " + this.getSetpoint());
			System.out.println("Current Encoder Value: " + Robot.SUB_DRIVE_BASE.getRightEncoderValue());
			System.out.println("Error: " + (Robot.SUB_DRIVE_BASE.getRightEncoderValue() - this.getSetpoint()));
			System.out.println("Input: " + this.returnPIDInput());
			System.out.println("On Target: " + driveController.onTarget());
		}
		else
		{
			Robot.SUB_DRIVE_BASE.tankDrive(0, 0);
			System.out.println("On Target: " + driveController.onTarget());
		}
	}

	// Called repeatedly when this Command is scheduled to run

	@Override
	protected void execute()
	{

		// double EncoderTarget = SmartDashboard.getNumber("setEncoder", 0);
		// DistanceToTarget = Robot.SUB_DRIVE_BASE.UpdateForwardSpeed(EncoderTarget,
		// 1000);
		SmartDashboard.putNumber("Left", Robot.SUB_DRIVE_BASE.getLeftEncoderValue());
		SmartDashboard.putNumber("Right", Robot.SUB_DRIVE_BASE.getRightEncoderValue());
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		// System.out.println("On Target: " + driveController.onTarget());
		return driveController.onTarget();
		// return DistanceToTarget < 20;
		// return driveController.onTarget();
	}

	// Called once after isFinished returns true
	@Override
	protected void end()
	{
		Robot.SUB_DRIVE_BASE.tankDrive(0, 0);
		Robot.SUB_DRIVE_BASE.resetEncoders();
		SmartDashboard.putNumber("setEncoder", 0);
		System.out.println("END!!!!!!!!!!!!!!!!!");

	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{

	}
}
