package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.Robot;

public class DriveWithEncoders extends PIDCommand
{
	private static double P = 0.7, I = 0.0, D = 0.0;
	private final PIDController driveController;

	public DriveWithEncoders()
	{
		super("Drive Distance", P, I, D);
		requires(Robot.SUB_DRIVE_BASE);
		driveController = this.getPIDController();
		driveController.setInputRange(-4095, 4095); // TODO find this out
		driveController.setOutputRange(-1, 1);
		driveController.setAbsoluteTolerance(10); // TODO find this out too
		driveController.setContinuous(true);
		Robot.SUB_DRIVE_BASE.resetEncoders();
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
	}

	@Override
	protected double returnPIDInput()
	{
		// TODO Auto-generated method stub
		// SmartDashboard.putNumber("Error",
		// (Robot.SUB_DRIVE_BASE.getAngle()-initialAngle));
		return (Robot.SUB_DRIVE_BASE.getLeftEncoderValue());
	}

	@Override
	protected void usePIDOutput(double output)
	{
		// The side with the GEAR IS THE FRONT!
		if (!driveController.onTarget())
		{
			if ((this.returnPIDInput() - this.getSetpoint()) > 0)
			{ // need to move forward
				System.out.println("Move Forward");
				System.out.println("Left Motor: " + (output) + "Right Motor: " + (output));
				Robot.SUB_DRIVE_BASE.tankDrive(output, output);
			}
			else if ((this.returnPIDInput() - this.getSetpoint()) < 0)
			{ // need to move backward
				System.out.println("Move Backward");
				System.out.println("Left Motor: " + (-output) + "Right Motor: " + (-output));
				Robot.SUB_DRIVE_BASE.tankDrive(-output, -output);
			}
			System.out.println("set point: " + this.getSetpoint());
			System.out.println("Current Encoder Value: " + Robot.SUB_DRIVE_BASE.getLeftEncoderValue());
			System.out.println("Error: " + (Robot.SUB_DRIVE_BASE.getLeftEncoderValue() - this.getSetpoint()));
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
		final double setEncoder = SmartDashboard.getNumber("setEncoder", 0);
		// double kP = SmartDashboard.getNumber("kP", 1.0);

		setTarget(setEncoder);
		// driveController.setPID(kP,0,0);
		SmartDashboard.putBoolean("tracking", true);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		System.out.println("On Target: " + driveController.onTarget());
		return true;
		// return driveController.onTarget();
	}

	public void setTarget(double ticks)
	{
		this.driveController.setSetpoint(ticks);
	}

	// Called once after isFinished returns true
	@Override
	protected void end()
	{
		Robot.SUB_DRIVE_BASE.tankDrive(0, 0);
		Robot.SUB_DRIVE_BASE.resetEncoders();
		SmartDashboard.putNumber("setEncoder", 0);
		SmartDashboard.putBoolean("tracking", false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
	}
}
