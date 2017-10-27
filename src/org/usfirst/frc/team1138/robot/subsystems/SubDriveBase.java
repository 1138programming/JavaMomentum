package org.usfirst.frc.team1138.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team1138.robot.RobotMap;
import org.usfirst.frc.team1138.robot.commands.DriveWithJoy;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class SubDriveBase extends Subsystem{
	
	private CANTalon leftFrontBaseMotor, rightFrontBaseMotor, leftRearBaseMotor, rightRearBaseMotor;
	private DoubleSolenoid shifterSolenoid, liftSolenoid; 
	private AHRS gyroAccel;
	public SubDriveBase() {
		// Motors
		// master motors
		leftFrontBaseMotor = new CANTalon(RobotMap.KLeftFrontBaseTalon);
		rightFrontBaseMotor = new CANTalon(RobotMap.KRightFrontBaseTalon);
		//slave motors 
		leftRearBaseMotor = new CANTalon(RobotMap.KLeftRearBaseTalon);
		rightRearBaseMotor = new CANTalon(RobotMap.KRightRearBaseTalon);
		// Config the masters 
		leftFrontBaseMotor.setInverted(true);
		initSafeMotor();
		leftFrontBaseMotor.enableControl();
		rightFrontBaseMotor.enableControl();
		// Config the slaves
		leftRearBaseMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		rightRearBaseMotor.changeControlMode(CANTalon.TalonControlMode.Follower);
		leftRearBaseMotor.set(leftFrontBaseMotor.getDeviceID());
		rightRearBaseMotor.set(rightFrontBaseMotor.getDeviceID());
		
		// Solenoids 
		shifterSolenoid = new DoubleSolenoid(RobotMap.KShifterSolenoid1, RobotMap.KShifterSolenoid2);
		liftSolenoid = new DoubleSolenoid(RobotMap.KLiftSolenoid1, RobotMap.KLiftSolenoid2); 
		
		//Gyro & Accel
		gyroAccel = new AHRS(Port.kMXP);
		SmartDashboard.putBoolean("Navx Connection: ", gyroAccel.isConnected());
		gyroAccel.zeroYaw();
		
		//Encoders 
		leftFrontBaseMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		rightFrontBaseMotor.setFeedbackDevice(CANTalon.FeedbackDevice.CtreMagEncoder_Relative);
		leftFrontBaseMotor.configEncoderCodesPerRev(4095);
		rightFrontBaseMotor.configEncoderCodesPerRev(4095);
		leftFrontBaseMotor.setEncPosition(0);
		rightFrontBaseMotor.setEncPosition(0);
		
		// LiveWindow
		LiveWindow.addSensor("SubDriveBase", "Gyro", gyroAccel);
//		LiveWindow.addActuator("SubDriveBase", "Left Front Motor", leftFrontBaseMotor);
//		LiveWindow.addActuator("SubDriveBase", "Right Front Motor", rightFrontBaseMotor);
//		LiveWindow.addActuator("SubDriveBase", "Left Rear Motor", leftRearBaseMotor);
//		LiveWindow.addActuator("SubDriveBase", "Right Rear Motor", rightRearBaseMotor);
	}
	
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DriveWithJoy());
	}
	
	private void initSafeMotor() {
		leftFrontBaseMotor.setSafetyEnabled(true);
		rightFrontBaseMotor.setSafetyEnabled(true);
		leftRearBaseMotor.setSafetyEnabled(true);
		rightRearBaseMotor.setSafetyEnabled(true);
	}
	
	public void tankDrive(double left, double right) { // The side with the GEAR IS THE FRONT!
		if(left > RobotMap.KDeadZoneLimit || left < -RobotMap.KDeadZoneLimit) leftFrontBaseMotor.set(left);
		else leftFrontBaseMotor.set(0);
		if(right > RobotMap.KDeadZoneLimit || right < -RobotMap.KDeadZoneLimit) rightFrontBaseMotor.set(right);
		else rightFrontBaseMotor.set(0);
	}
	
	public void highShiftBase() {
		shifterSolenoid.set(DoubleSolenoid.Value.kReverse);
	}
	
	public void lowShiftBase() {
		shifterSolenoid.set(DoubleSolenoid.Value.kForward);
	}
	
	public void toggleShift() {
		if (shifterSolenoid.get() == DoubleSolenoid.Value.kForward) {
			highShiftBase();
		} else {
			lowShiftBase();
		}
	}
	
	public void engageLift()
	{
		if(liftSolenoid.get() == DoubleSolenoid.Value.kForward) //is the lift engaged?
			liftSolenoid.set((DoubleSolenoid.Value.kReverse));	//disengage lift
		else liftSolenoid.set((DoubleSolenoid.Value.kForward));	//engage lift
	}
	
	public void resetGyro() {
		gyroAccel.zeroYaw();
	}
	
	public double getAngle() {
		return gyroAccel.getAngle();
	}
	
	public Value getLiftState() {
		return liftSolenoid.get(); 
	}
}
