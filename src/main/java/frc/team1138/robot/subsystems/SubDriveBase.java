package frc.team1138.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
//import edu.wpi.first.wpilibj.PWMTalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.RobotMap;
import frc.team1138.robot.commands.DriveWithJoy;

/**
 * @author Zheyuan Hu
 * @version 1.0.0
 */
public class SubDriveBase extends Subsystem
{
	private final TalonSRX leftFrontBaseMotor, rightFrontBaseMotor, rightRearBaseMotor, gyroTalon;
	private final TalonSRX leftRearBaseMotor;
	private final PigeonIMU PigeonIMU;
	private final DoubleSolenoid shifterSolenoid, liftSolenoid;
	// private AHRS gyroAccel; deprecated

	public SubDriveBase()
	{
		// Motors
		// master motors
		leftFrontBaseMotor = new TalonSRX(RobotMap.KLeftFrontBaseTalon);
		rightFrontBaseMotor = new TalonSRX(RobotMap.KRightFrontBaseTalon);
		// slave motors
		leftRearBaseMotor = new TalonSRX(RobotMap.KLeftRearBaseTalon);
		rightRearBaseMotor = new TalonSRX(RobotMap.KRightRearBaseTalon);
		// Config the masters
		leftFrontBaseMotor.setInverted(true);
		// leftRearBaseMotor.setInverted(true);
		// initSafeMotor();
		// Config the slaves
		leftRearBaseMotor.set(ControlMode.Follower, leftFrontBaseMotor.getDeviceID());
		rightRearBaseMotor.set(ControlMode.Follower, rightFrontBaseMotor.getDeviceID());

		// Solenoids
		shifterSolenoid = new DoubleSolenoid(RobotMap.KShifterSolenoid1, RobotMap.KShifterSolenoid2);
		liftSolenoid = new DoubleSolenoid(RobotMap.KLiftSolenoid1, RobotMap.KLiftSolenoid2);

		// Gyro & Accel
		// gyroAccel = new AHRS(Port.kMXP);
		gyroTalon = new TalonSRX(10);
		PigeonIMU = new PigeonIMU(gyroTalon);
		SmartDashboard.putNumber("Navx Connection: ", PigeonIMU.getFirmwareVersion());
		PigeonIMU.setYaw(0, 0);
		// gyroAccel.zeroYaw();

		// Encoders
		leftFrontBaseMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		rightFrontBaseMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
		// leftFrontBaseMotor.configEncoderCodesPerRev (4095);
		// rightFrontBaseMotor.configEncoderCodesPerRev(4095);
		leftFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);
		rightFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);

		// LiveWindow
		// LiveWindow.addSensor("SubDriveBase", "Pigeon", (LiveWindowSendable)
		// PigeonIMU);
		// LiveWindow.addSensor("SubDriveBase", "Gyro", gyroAccel);
		// LiveWindow.addActuator("SubDriveBase", "Left Front Motor",
		// leftFrontBaseMotor);
		// LiveWindow.addActuator("SubDriveBase", "Right Front Motor",
		// rightFrontBaseMotor);
		// LiveWindow.addActuator("SubDriveBase", "Left Rear Motor", leftRearBaseMotor);
		// LiveWindow.addActuator("SubDriveBase", "Right Rear Motor",
		// rightRearBaseMotor);
		// LiveWindow.add(leftRearBaseMotor.init);
	}

	@Override
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

		if ((left > RobotMap.KDeadZoneLimit) || (left < -RobotMap.KDeadZoneLimit))
		{
			leftRearBaseMotor.set(ControlMode.Follower, leftFrontBaseMotor.getDeviceID());
			leftFrontBaseMotor.set(ControlMode.PercentOutput, left);
			leftRearBaseMotor.set(ControlMode.Follower, leftFrontBaseMotor.getDeviceID());

			SmartDashboard.putNumber("leftFrontBaseMotor", leftFrontBaseMotor.getMotorOutputPercent());
			SmartDashboard.putNumber("leftRearBaseMotor", leftRearBaseMotor.getMotorOutputPercent());
			SmartDashboard.putNumber("leftFrontBaseMotor ID", leftFrontBaseMotor.getDeviceID());
			SmartDashboard.putString("leftRearBaseMotor mode", leftRearBaseMotor.getControlMode().toString());
		}
		else
		{
			leftFrontBaseMotor.set(ControlMode.PercentOutput, 0);
		}

		if ((right > RobotMap.KDeadZoneLimit) || (right < -RobotMap.KDeadZoneLimit))
		{
			rightFrontBaseMotor.set(ControlMode.PercentOutput, right);
			SmartDashboard.putNumber("rightFrontBaseMotor", rightFrontBaseMotor.getMotorOutputPercent());
			SmartDashboard.putNumber("rightRearBaseMotor", rightRearBaseMotor.getMotorOutputPercent());
		}
		else
		{
			rightFrontBaseMotor.set(ControlMode.PercentOutput, 0);
		}
	}

	// TODO FIX THIS!
	public double UpdateTurnSpeed(double TargetAngle, double LogValue)
	{
		double ModifiedAngle = this.getAngle();

		while (ModifiedAngle < 0)
		{
			ModifiedAngle += 360;
		}
		while (TargetAngle < 0)
		{
			TargetAngle += 360;
		}

		final double CounterClockwiseAngle = TargetAngle > ModifiedAngle ? TargetAngle - ModifiedAngle
				: (360 - ModifiedAngle) + TargetAngle;
		final double ClockwiseAngle = TargetAngle > ModifiedAngle ? (360 - TargetAngle) + ModifiedAngle
				: ModifiedAngle - TargetAngle;
		// Determines the angle of the right and left turns needed to reach the target
		// angle in order to later decide the shortest turn

		final double Speed = Math.log(Math.abs(ModifiedAngle - TargetAngle)) / Math.log(LogValue + 1);
		// NumberFormat defaultFormat = NumberFormat.getPercentInstance();
		// defaultFormat.setMinimumFractionDigits(2);
		// double PercentSpeed = defaultFormat.format(Speed);
		// double NegativePercentSpeed = defaultFormat.format(-1 * Speed);
		SmartDashboard.putNumber("Speed", Speed);
		// As the difference between the target angle and the robot's angle gets
		// smaller, the robot turns more slowly in order to minimize overshoot

		if (CounterClockwiseAngle < ClockwiseAngle)
		{
			this.tankDrive(Speed, -Speed); // Turn counter clockwise
		}
		else
		{
			this.tankDrive(-Speed, Speed); // Turn clockwise
		}

		return Math.abs(TargetAngle - ModifiedAngle); // Return the distance from the target angle
	}

	// public void drive(double right, double curve) { //TODO fix here!
	// final double leftOutput;
	// final double rightOutput;
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
		if (liftSolenoid.get() == DoubleSolenoid.Value.kForward)
		{
			liftSolenoid.set((DoubleSolenoid.Value.kReverse)); // disengage lift
		}
		else
		{
			liftSolenoid.set((DoubleSolenoid.Value.kForward)); // engage lift
		}
	}

	/**
	 * public method to reset Gyro value
	 */
	public void resetGyro()
	{
		PigeonIMU.setYaw(0, 0);
	}

	public void resetEncoders()
	{
		leftFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);
		rightFrontBaseMotor.getSensorCollection().setQuadraturePosition(0, 0);
	}

	// TODO FIX THIS
	public void DriveForward(float distance, float speed)
	{
		final float encoderReference = leftFrontBaseMotor.getSensorCollection().getQuadraturePosition(); // Alex Harris
																											// fixed it
		final float encoderReference2 = rightFrontBaseMotor.getSensorCollection().getQuadraturePosition();
		float encoder = leftFrontBaseMotor.getSensorCollection().getQuadraturePosition();
		float encoder2 = rightFrontBaseMotor.getSensorCollection().getQuadraturePosition();
		while (((encoder - encoderReference) < distance) && ((encoder2 - encoderReference2) < distance))
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
		final double[] ypr = new double[3];
		PigeonIMU.getYawPitchRoll(ypr);
		return (-ypr[0]);
	}

	public double getLeftEncoderValue()
	{
		return leftFrontBaseMotor.getSensorCollection().getQuadraturePosition();
	}

	public double getRightEncoderValue()
	{
		return rightFrontBaseMotor.getSensorCollection().getQuadraturePosition();
	}

	/**
	 * @return the state of the lift solenoid
	 */
	public Value getLiftState()
	{
		return liftSolenoid.get();
	}
}
