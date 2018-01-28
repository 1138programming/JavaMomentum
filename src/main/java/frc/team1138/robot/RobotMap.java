package frc.team1138.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * doubleing around.
 * 
 * @author Zheyuan Hu
 * @version 1.0.0
 */
public class RobotMap
{
	public static final int KLeftRearBaseTalon = 3;
	public static final int KLeftFrontBaseTalon = 1;
	public static final int KRightRearBaseTalon = 4;
	public static final int KRightFrontBaseTalon = 2;
	// public static final int KLeftRearBaseTalon = 4 ;
	// public static final int KLeftFrontBaseTalon = 2 ;
	// public static final int KRightRearBaseTalon = 3 ;
	// public static final int KRightFrontBaseTalon = 1 ;
	public static final int KFlywheelRightTalon = 5;
	public static final int KFlywheelLeftTalon = 6;
	public static final int KFilterFrontTalon = 7;
	public static final int KFilterRearTalon = 8;
	public static final int KCollectorTalon = 9;

	public static final int KLeftBaseMaster = 1; // KLeftMaster = Master Talon for left side
	public static final int KRightBaseMaster = 2; // KRightMaster = Master Talon for right side

	// all of the solenoids are doubles, so they need 2 numbers each. If you change
	// one, be sure to change
	// the other one of the pair.
	public static final int KShifterSolenoid1 = 0;
	public static final int KShifterSolenoid2 = 1;
	public static final int KLiftSolenoid1 = 2;
	public static final int KLiftSolenoid2 = 3;
	public static final int KEsophagusSolenoid1 = 4;
	public static final int KEsophagusSolenoid2 = 5;

	// Sensors
	public static final int KBaseUltrasonic1 = 0;
	public static final int KBaseUltrasonic2 = 1;

	public static final double KDeadZoneLimit = 0.1;
	public static final double KXboxDeadZoneLimit = 0.2;

	// static finalants for Autonomous routines
	// Circumference of wheel - 330.2 millimeters. Divide by this number to get
	// number of rotations for distances
	public static final double KWheelRadius = 5.255; // In centimeters
	public static final double KWheelCircumference = 33.02; // In centimeters
	public static final double KDistanceToBaseLine = 191.64; // In centimeters
	public static final double KDistanceToPilotTower = 86.86; // In centimeters
	public static final double KRevsToBaseLine = KDistanceToBaseLine / KWheelCircumference; // rotations from the
																							// diamond plate to the
																							// baseline from Field CAD
																							// (191.64cm)
	public static final double KRevsToPilotTower = KDistanceToPilotTower / KWheelCircumference; // rotations from the
																								// baseline to the pilot
																								// tower from Field CAD
																								// (86.86cm)
	public static final double KTurnToPilotTower = 55; // degrees to turn from the baseline to face the pilot tower.
	public static final double KAutonStraightSpeed = .5; // TODO lets go slowly and backwards
	public static final double KAutonTurnSpeed = .5; // TODO turn slowly towards pilot tower
	public static final double KRevsToCrossTheLine = 10; // Unofficial distance to cross the line in autonomous
	public static final double KRevsToVisionTracking = 3; // Unofficial distance until we turn on vision tracking
	public static final double KEncoderTicksPerRev = 4096; // The amount of ticks it takes to do one full rotation with
															// the encoder

	// Okay, don't get clever and decide this isn't actually the way we are turning.
	// I don't care which way we are turning.
	// Left turn means the turn we make when our starting position is on the left
	// side of the field. Right Turn is when
	// our starting position is on the right side of the field. We are also doing
	// the turn going backwards and that
	// changes the direction too. Don't think to much about this or your brain will
	// explode.
	public static final boolean KLeftTurn = true;
	public static final boolean KRightTurn = false;

	// Joystick Definitions
	public static final int KLeftJoystick = 0;
	public static final int KRightJoystick = 1;
	public static final int KXBoxController = 2;

	// XBox button definitions
	public static final int KButtonA = 1; // Toggle Vision
	public static final int KButtonB = 2; // Toggle Esophagus
	public static final int KButtonX = 3; // Turn on shooter
	public static final int KButtonY = 4; // Turn off shooter
	public static final int KLeftBumper = 5; // Decrease Flywheel Speed
	public static final int KRightBumpter = 6;
}
