package frc.team1138.robot.subsystems;

import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.RobotMap;
import frc.team1138.robot.commands.DriveWithJoy;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
//import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * @author Zheyuan Hu
 * @version 1.0.0
 */
public class SubDriveBase extends Subsystem
{
	private WPI_TalonSRX leftFrontBaseMotor, rightFrontBaseMotor;
	private TalonSRX rightRearBaseMotor, gyroTalon;
	private TalonSRX leftRearBaseMotor;
	// private PigeonIMU PigeonIMU;
	private DoubleSolenoid shifterSolenoid, liftSolenoid;
	// private AHRS gyroAccel; deprecated

	public SubDriveBase()
	{
		// Gyro & Accel *Note this declaration needs to come before the motor
		// declarations
		// gyroAccel = new AHRS(Port.kMXP);
		// gyroTalon = new TalonSRX(3);
		// Motors
		// master motors
		leftFrontBaseMotor = new WPI_TalonSRX(RobotMap.KLeftFrontBaseTalon);
		rightFrontBaseMotor = new WPI_TalonSRX(RobotMap.KRightFrontBaseTalon);
		// slave motors
		leftRearBaseMotor = new TalonSRX(RobotMap.KLeftRearBaseTalon);
		rightRearBaseMotor = new TalonSRX(RobotMap.KRightRearBaseTalon);
		// Config the masters
		
		// initSafeMotor();
		// Config the slaves
		leftRearBaseMotor.set(ControlMode.Follower, leftFrontBaseMotor.getDeviceID());
		rightRearBaseMotor.set(ControlMode.Follower, rightFrontBaseMotor.getDeviceID());

		// Solenoids
		shifterSolenoid = new DoubleSolenoid(RobotMap.KShifterSolenoid1, RobotMap.KShifterSolenoid2);
		liftSolenoid = new DoubleSolenoid(RobotMap.KLiftSolenoid1, RobotMap.KLiftSolenoid2);

		// PigeonIMU = new PigeonIMU(gyroTalon);
		// SmartDashboard.putNumber("Navx Connection: ", PigeonIMU.getFirmwareVersion());
		// PigeonIMU.setYaw(0, 0);
		// gyroAccel.zeroYaw();

		// Encoders
		leftFrontBaseMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightFrontBaseMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		// leftFrontBaseMotor.configEncoderCodesPerRev (4095);
		// rightFrontBaseMotor.configEncoderCodesPerRev(4095);
		leftFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);
		rightFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);
		
		leftFrontBaseMotor.setSensorPhase(true);
		rightFrontBaseMotor.setSensorPhase(true);

		leftFrontBaseMotor.setInverted(false);
		leftRearBaseMotor.setInverted(false);
		rightFrontBaseMotor.setInverted(true);
		rightRearBaseMotor.setInverted(true);

		leftFrontBaseMotor.setName("SubDriveBase", "Left Front Motor");
		rightFrontBaseMotor.setName("SubDriveBase", "Right Front Motor");
		LiveWindow.setEnabled(true);
	}

	public void initDefaultCommand()
	{
		// Set the default command for a subsystem here.
		setDefaultCommand(new DriveWithJoy());
	}

	/**
	 * Enable Safety for motors
	 */
	// private void initSafeMotor() {
	// leftFrontBaseMotor.setSafetyEnabled(true);
	// rightFrontBaseMotor.setSafetyEnabled(true);
	// leftRearBaseMotor.setSafetyEnabled(true);
	// rightRearBaseMotor.setSafetyEnabled(true);
	// }

	/**
	 * @apiNote The side with the GEAR IS THE FRONT!
	 * @param left
	 *            the motor input for left motors from -1.0 to 1.0
	 * @param right
	 *            the motor input for right motors from -1.0 to 1.0
	 */
	public void tankDrive(double left, double right)
	{

		if (left > RobotMap.KDeadZoneLimit || left < -RobotMap.KDeadZoneLimit)
		{
			leftFrontBaseMotor.set(ControlMode.PercentOutput, left);
		}
		else
		{
			leftFrontBaseMotor.set(ControlMode.PercentOutput, 0);
		}

		if (right > RobotMap.KDeadZoneLimit || right < -RobotMap.KDeadZoneLimit)
		{
			rightFrontBaseMotor.set(ControlMode.PercentOutput, right);
		}
		else
		{
			rightFrontBaseMotor.set(ControlMode.PercentOutput, 0);
		}
		SmartDashboard.putNumber("Right Encoder", rightFrontBaseMotor.getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("Left Encoder", leftFrontBaseMotor.getSelectedSensorPosition(0));
	}

	// TODO FIX THIS!
	public double UpdateTurnSpeed(double TargetAngle, double LogValue)
	{
		double ModifiedAngle = this.getAngle();

		while (ModifiedAngle < 0)
			ModifiedAngle += 360;
		while (TargetAngle < 0)
			TargetAngle += 360;

		double CounterClockwiseAngle = 0;
		if (TargetAngle > ModifiedAngle)
			CounterClockwiseAngle = TargetAngle - ModifiedAngle;
		else
			CounterClockwiseAngle = (360 - ModifiedAngle) + TargetAngle;

		double ClockwiseAngle = 0;
		if (TargetAngle > ModifiedAngle)
			ClockwiseAngle = (360 - TargetAngle) + ModifiedAngle;
		else
			ClockwiseAngle = ModifiedAngle - TargetAngle;
		// Determines the angle of the right and left turns needed to reach the target
		// angle in order to later decide the shortest turn

		double Speed = Math.log(Math.abs(ModifiedAngle - TargetAngle) + 1) / Math.log(LogValue + 1);
		Speed = Math.floor(Speed * 100) / 100;
		if (Speed > 1)
			Speed = 1;
		if (-0.05 < Speed && Speed < 0.05)
			Speed = 0;
		if (Speed < -1)
			Speed = -1;
		SmartDashboard.putNumber("Speed", Speed);
		// As the difference between the target angle and the robot's angle gets
		// smaller, the robot turns more slowly in order to minimize overshoot

		if (CounterClockwiseAngle < ClockwiseAngle)
			this.tankDrive(-Speed, Speed); // Turn counter clockwise
		else
			this.tankDrive(Speed, -Speed); // Turn clockwise

		return Math.abs(TargetAngle - ModifiedAngle); // Return the distance from the target angle
	}

	public double UpdateForwardSpeed(double Target, double LogValue)
	{
		double RightEncoder = Math.abs(rightFrontBaseMotor.getSensorCollection().getQuadraturePosition());
		double LeftEncoder = Math.abs(leftFrontBaseMotor.getSensorCollection().getQuadraturePosition());
		double EncoderAverage = (RightEncoder + LeftEncoder) / 2;
		double Speed = Math.log(Math.abs(Target - EncoderAverage) + 1) / Math.log(LogValue + 1);
		Speed = Math.floor(Speed * 100) / 100;
		SmartDashboard.putNumber("Right Encoder", RightEncoder);
		SmartDashboard.putNumber("Left Encoder", LeftEncoder);
		SmartDashboard.putNumber("Encoder Average", EncoderAverage);
		if (Speed > 1)
			Speed = 1;
		if (-0.05 < Speed && Speed < 0.05)
			Speed = 0;
		if (Speed < -1)
			Speed = -1;
		SmartDashboard.putNumber("Forward Speed", Speed);
		if (Target - EncoderAverage > 0)
			tankDrive(Speed, Speed); // Drive forward
		else
			tankDrive(-Speed, -Speed); // Drive backwards
		return Math.abs(Target - EncoderAverage);
	}
	// public void drive(double riouble curve) { //TODO fix here!
	// final double leftOutput;
	// final double rightOght, dutput;
	// double m_sensitivity = 1.0;
	// if (curve < 0) {
	// double value = Math.log(-curve);
	// double ratio = (value - m_sensitivity) / (value + m_sensitivity);
	// if (ratio == 0) {
	// ratio = .0000000001;
	// }
	// leftOutput = right / ratio;
	// rightOutput = right;
	// System.out.println("Turn Left: " + "lout: " + leftOutput + "rout: " +
	// rightOutput);
	// } else if (curve > 0) {
	// double value = Math.log(curve);
	// double ratio = (value - m_sensitivity) / (value + m_sensitivity);
	// if (ratio == 0) {
	// ratio = .0000000001;
	// }
	// leftOutput = right;
	// rightOutput = right / ratio;
	// System.out.println("Turn Right: " + "lout: " + leftOutput + "rout: " +
	// rightOutput);
	// } else {
	// leftOutput = right;
	// rightOutput = right;
	// System.out.println("Straight: " + "lout: " + leftOutput + "rout: " +
	// rightOutput);
	// }
	// setLeftRightMotorOutputs(leftOutput, rightOutput);
	// }

	/**
	 * Set the speed of the right and left motors. This is used once an appropriate
	 * drive setup function is called such as twoWheelDrive(). The motors are set to
	 * "leftSpeed" and "rightSpeed" and includes flipping the direction of one side
	 * for opposing motors.
	 *
	 * @param leftOutput
	 *            The speed to send to the left side of the robot.
	 * @param rightOutput
	 *            The speed to send to the right side of the robot.
	 */
	// private void setLeftRightMotorOutputs(double leftOutput, double rightOutput)
	// {
	// leftFrontBaseMotor.set(ControlMode.PercentOutput, limit(leftOutput));
	// leftRearBaseMotor.set(ControlMode.PercentOutput, limit(leftOutput));
	// rightFrontBaseMotor.set(ControlMode.PercentOutput, -limit(rightOutput));
	// rightRearBaseMotor.set(ControlMode.PercentOutput, -limit(rightOutput));
	// System.out.println("lmotor: " + limit(leftOutput) + "rmotor: " +
	// (-limit(rightOutput)));
	// }

	/**
	 * Limit motor values to the -1.0 to +1.0 range.
	 */
	// private static double limit(double number) {
	// if (number > 1.0) {
	// return 1.0;
	// }
	// if (number < -1.0) {
	// return -1.0;
	// }
	// return number;
	// }

	/**
	 * Shift the base to high position
	 */
	private void highShiftBase()
	{
		shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
	}

	/**
	 * Shift the base to low position
	 */
	private void lowShiftBase()
	{
		shifterSolenoid.set(DoubleSolenoid.Value.kForward);
	}

	/**
	 * public method to switch shifts base
	 */
	public void toggleShift()
	{
		if (shifterSolenoid.get() == DoubleSolenoid.Value.kForward)
		{
			highShiftBase();
		}
		else
		{
			lowShiftBase();
		}
	}

	/**
	 * public method to engage the lift
	 */
	public void engageLift()
	{
		if (liftSolenoid.get() == DoubleSolenoid.Value.kForward) // is the lift engaged?
			liftSolenoid.set((DoubleSolenoid.Value.kReverse)); // disengage lift
		else
			liftSolenoid.set((DoubleSolenoid.Value.kForward)); // engage lift
	}

	/**
	 * public method to reset Gyro value
	 */
	public void resetGyro()
	{
		// PigeonIMU.setYaw(0, 0);
		// gyroAccel.zeroYaw();
	}

	public void resetEncoders()
	{
		leftFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);
		rightFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);
	}

	// TODO FIX THIS LIKE RN
	public void DriveForward(float distance, float speed)
	{
		float encoderReference = leftFrontBaseMotor.getSensorCollection().getQuadraturePosition(); // Alex Harris fixed
																									// it
		float encoderReference2 = rightFrontBaseMotor.getSensorCollection().getQuadraturePosition();
		float encoder = leftFrontBaseMotor.getSensorCollection().getQuadraturePosition();
		float encoder2 = rightFrontBaseMotor.getSensorCollection().getQuadraturePosition();
		while ((encoder - encoderReference) < distance && (encoder2 - encoderReference2) < distance)
		{
			rightFrontBaseMotor.set(ControlMode.PercentOutput, speed);
			leftFrontBaseMotor.set(ControlMode.PercentOutput, speed);
			encoder = leftFrontBaseMotor.getSensorCollection().getQuadraturePosition();
			encoder2 = rightFrontBaseMotor.getSensorCollection().getQuadraturePosition();
		}
	}

	/**
	 * @return Current Gyro Value in degrees from 180.0 to -180.0
	 */
	public double getAngle()
	{
		double[] ypr = new double[3];
		// PigeonIMU.getYawPitchRoll(ypr);
		return (-ypr[0]);
		// return gyroAccel.getAngle();
	}

	public double getLeftEncoderValue()
	{
		return leftFrontBaseMotor.getSelectedSensorPosition(0);
	}

	public double getRightEncoderValue()
	{
		return rightFrontBaseMotor.getSelectedSensorPosition(0);
	}

	/**
	 * @return the state of the lift solenoid
	 */
	public Value getLiftState()
	{
		return liftSolenoid.get();
	}
	
	public void setLeftMotionControl(ControlMode mode, double value) {
		leftFrontBaseMotor.set(mode, value);
	}
	
	public void setRightMotionControl(ControlMode mode, double value) {
		rightFrontBaseMotor.set(mode, value);
	}
	
	public TalonSRX getRightMotor() {
		return this.rightFrontBaseMotor; 
	}
	
	public TalonSRX getLeftMotor() {
		return this.leftFrontBaseMotor; 
	}
}
