package frc.team1138.robot;

import frc.team1138.robot.commands.LiftBase;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 * @author Zheyuan Hu
 * @version 1.0.0
 */

public class OI {
	public static final int KLogitechController = 3; //Base driver
	public static final double KXboxDeadZoneLimit = 0.2;
	
	private Joystick leftController, rightController, xBoxController, logitechController; 
	private JoystickButton shiftBtn, liftBtn; // Logitech Buttons 
	private JoystickButton btnA, btnB, btnX, btnY, btnLB, btnRB;
	
	public OI() {
		leftController = new Joystick(RobotMap.KLeftJoystick);
		rightController = new Joystick(RobotMap.KRightJoystick);
		xBoxController = new Joystick(RobotMap.KXBoxController);
		logitechController = new Joystick(KLogitechController);

		//Logitech Buttons
		shiftBtn = new JoystickButton(leftController, 1); //Shifts the Base from Low Gear to High Gear and vice versa
		liftBtn = new JoystickButton(leftController, 7);   //Shifts the Base to Lift the robot and vice versa

		//XBox Definitions
		btnA	= new JoystickButton(xBoxController, RobotMap.KButtonA) ;	//Toggle Vision
		btnB = new JoystickButton(xBoxController, RobotMap.KButtonB) ;	//Toggle Esophagus
		btnX = new JoystickButton(xBoxController, RobotMap.KButtonX) ;	//Turn on shooter
		btnY = new JoystickButton(xBoxController, RobotMap.KButtonY) ;	//Turn off shooter
		btnLB = new JoystickButton(xBoxController, RobotMap.KLeftBumper) ;	//Decrease Flywheel Speed
		btnRB = new JoystickButton(xBoxController, RobotMap.KRightBumpter) ;	//Increase Flywheel Speed

//		shiftBtn.whenPressed(new TurnWithGyro());
		liftBtn.whenPressed(new LiftBase());
//		buttonX->WhenPressed(new EngageShooter());
//		buttonY->WhenPressed(new DisengageShooter());
//		buttonB->WhenPressed(new ToggleEsophagus());
//		buttonA->WhenPressed(new VisionTracking());
//		buttonLB->WhenPressed(new OpenEsophagus());		//buttonLB->WhenPressed(new FlywheelDecreaseSpeed());
//		buttonRB->WhenPressed(new CloseEsophagus());	//buttonRB->WhenPressed(new FlywheelIncreaseSpeed());
	}
	
	public double getRightControllerY() {			//Right controller is right side drive
		if(logitechController.getY() < -KXboxDeadZoneLimit || logitechController.getY() > KXboxDeadZoneLimit)
		{
			return -logitechController.getY(); //TODO check if it's twist for z-rotate axis
			//return leftController.getY();
		}
		else
		{
			return 0;
		}
	}

	public double getLeftControllerY() {			//Left controller is left side drive
		if(logitechController.getThrottle() < -KXboxDeadZoneLimit || logitechController.getThrottle() > KXboxDeadZoneLimit)
		{
			return -logitechController.getThrottle(); //TODO check if it's twist for z-rotate axis
			//return rightController.getY();
		}
		else
		{
			return 0;
		}
	}

	public boolean getLeftTrigger() {				//left controller's trigger is the shifter
		return liftBtn.get(); 
	}

	public boolean getRightTrigger() {			//right controller's trigger engages the lift
		return shiftBtn.get(); 
	}

	public double getLeftXBoxAxis() {			//left xbox axis controls the collector
		return (-xBoxController.getRawAxis(1));
	}

	//Fine control from collector, signal from joystick is reversed
	public double getRightXBoxAxis() {
		return (-xBoxController.getRawAxis(5));
	}
	
}
