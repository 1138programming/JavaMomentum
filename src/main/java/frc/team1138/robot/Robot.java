
package frc.team1138.robot;

import edu.wpi.first.wpilibj.DriverStation;
//import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team1138.robot.commands.AutonCommandGroup;
import frc.team1138.robot.commands.DogTrack;
import frc.team1138.robot.commands.ExampleCommand;
import frc.team1138.robot.commands.TestAutonomousCommand;
import frc.team1138.robot.commands.TurnWithGyro;
import frc.team1138.robot.subsystems.ExampleSubsystem;
import frc.team1138.robot.subsystems.SubDriveBase;
import frc.team1138.robot.commands.DriveForward;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * @author Zheyuan Hu
 * @version 1.0.0
 */
public class Robot extends IterativeRobot {
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static SubDriveBase SUB_DRIVE_BASE;
	private static OI oi;
	private Command autonomousCommand;
	private SendableChooser<Command> chooser = new SendableChooser<>();
	//private SendableChooser chooser;
	private TurnWithGyro turn;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		SUB_DRIVE_BASE = new SubDriveBase();
		oi = new OI();
		//chooser.addDefault("AutonCommandGroup", new AutonCommandGroup());
		//chooser.addDefault("DriveForward", new DriveForward());
		//chooser.addDefault("Turn With Gyro", new TurnWithGyro());
		SmartDashboard.putData("Autonomous Mode Chooser", chooser);
		SmartDashboard.putData("Test Auton", new TestAutonomousCommand());
//		SmartDashboard.putData("PID TURN", new TurnWithGyro(0));
			//chooser.addObject("My Auto", new MyAutoCommand());
//        Robot.SUB_DRIVE_BASE.resetGyro(); // reset Gyro at the start of the Robot
    }

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		//autonomousCommand1 = chooser.getSelected();
		//autonomousCommand2 = chooser.getSelected();
		String gameData;
		gameData = DriverStation.getInstance().getGameSpecificMessage();
		SmartDashboard.putString("Game String", gameData);
		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */
		// schedule the autonomous command (example)
		if(gameData.charAt(0) == 'L') //TODO fix the FMS loops
		{
			autonomousCommand = new DriveForward();
		}
		else
		{
			autonomousCommand = new TurnWithGyro();
		}
		
		if (autonomousCommand != null)
		{
			Robot.SUB_DRIVE_BASE.resetEncoders();
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
//		if (autonomousCommand1 != null)
//		{
//			autonomousCommand1.cancel();
//		}
//		if(autonomousCommand2 != null)
//		{
//			autonomousCommand2.cancel();
//		}
		SmartDashboard.putNumber("setAngle", 0);
		Robot.SUB_DRIVE_BASE.resetGyro();
		Robot.SUB_DRIVE_BASE.resetEncoders();
//		Command dogTrack = new DogTrack();
//		dogTrack.start();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
//		SmartDashboard.putNumber("angle", Robot.SUB_DRIVE_BASE.getAngle());
//		SmartDashboard.putBoolean("Running",true);
		SmartDashboard.putNumber("Left", Robot.SUB_DRIVE_BASE.getLeftEncoderValue());
		SmartDashboard.putNumber("Right", Robot.SUB_DRIVE_BASE.getRightEncoderValue());
	}

	/**
	 * This function is called periodically during test mode
	 */
	
	@Override
	public void testInit() {
		Robot.SUB_DRIVE_BASE.resetGyro();
	}
	
	@Override
	public void testPeriodic() {
	}
}
