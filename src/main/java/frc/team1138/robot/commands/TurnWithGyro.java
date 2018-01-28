package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.OI;
import frc.team1138.robot.Robot;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class TurnWithGyro extends PIDCommand
{
	private static double P = 0.1, I = 0.0, D = 0.0;
	private PIDController turnController;

	public TurnWithGyro()
	{
		super("Turn Angle", P, I, D);
		requires(Robot.SUB_DRIVE_BASE);
		turnController = this.getPIDController();
		turnController.setInputRange(-360, 360);
		turnController.setOutputRange(-1, 1);
		turnController.setAbsoluteTolerance(1.5);
		turnController.setContinuous(true);
		Robot.SUB_DRIVE_BASE.resetGyro();
		Robot.SUB_DRIVE_BASE.resetEncoders();
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
		Robot.SUB_DRIVE_BASE.resetGyro();
		Robot.SUB_DRIVE_BASE.resetEncoders();
		setTarget(65);
	}

	@Override
	protected double returnPIDInput()
	{
		// TODO Auto-generated method stub
		// SmartDashboard.putNumber("Error",
		// (Robot.SUB_DRIVE_BASE.getAngle()-initialAngle));
		return (Robot.SUB_DRIVE_BASE.getAngle());
	}

	@Override
	protected void usePIDOutput(double output)
	{
		// The side with the GEAR IS THE FRONT!
		if (!turnController.onTarget())
		{
			if (this.returnPIDInput() - this.getSetpoint() > 0)
			{ // need to turn left
				System.out.println("turn left");
				System.out.println("Left Motor: " + (-output) + "Right Motor: " + (output));
				Robot.SUB_DRIVE_BASE.tankDrive(-output, output);
			}
			else if (this.returnPIDInput() - this.getSetpoint() < 0)
			{ // need to turn right
				System.out.println("turn right");
				System.out.println("Left Motor: " + (-output) + "Right Motor: " + (output));
				Robot.SUB_DRIVE_BASE.tankDrive(-output, output);
			}
			System.out.println("set point: " + this.getSetpoint());
			System.out.println("Current Angle: " + Robot.SUB_DRIVE_BASE.getAngle());
			System.out.println("Error: " + (Robot.SUB_DRIVE_BASE.getAngle() - this.getSetpoint()));
			System.out.println("Input: " + this.returnPIDInput());
			System.out.println("On Target: " + turnController.onTarget());
		}
		else
		{
			Robot.SUB_DRIVE_BASE.tankDrive(0, 0);
			System.out.println("On Target: " + turnController.onTarget());
		}
	}

	// Called repeatedly when this Command is scheduled to run

	@Override
	protected void execute()
	{
		// setTarget(45);
		// double setAngle = SmartDashboard.getNumber("setAngle", 0);
		// //double kP = SmartDashboard.getNumber("kP", 1.0);
		//
		// setTarget(setAngle);
		// turnController.setPID(kP,0,0);
		// SmartDashboard.putBoolean("tracking",true);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		System.out.println("On Target: " + turnController.onTarget());
		// return false;
		return turnController.onTarget();
	}

	public void setTarget(double angle)
	{
		this.turnController.setSetpoint(angle);
	}

	// Called once after isFinished returns true
	@Override
	protected void end()
	{
		Robot.SUB_DRIVE_BASE.tankDrive(0, 0);
		Robot.SUB_DRIVE_BASE.resetGyro();
		SmartDashboard.putNumber("setAngle", 0);
		SmartDashboard.putBoolean("tracking", false);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
	}
}