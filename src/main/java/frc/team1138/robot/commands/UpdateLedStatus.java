package frc.team1138.robot.commands;

import frc.team1138.robot.Robot;
import frc.team1138.robot.subsystems.RIODuinoSubsystem.LEDModes;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.DigitalInput;

import java.io.IOException;

/**
 * @author Edward Pedemonte
 * @version 1.0.0
 * This Command requires Robot.SUB_DRIVE_BASE
 */
public class UpdateLedStatus extends Command{
	private DigitalInput cubeLimitSwitch;
	private DigitalInput rungLimitSwitch;
	private DigitalInput lastMode;
	long lastUpdateTime;
	
    public UpdateLedStatus() {
		// TODO Auto-generated constructor stub
		requires(Robot.rioduinoSubsystem);
		cubeLimitSwitch = new DigitalInput(0);
		rungLimitSwitch = new DigitalInput(1);
		lastUpdateTime =  System.currentTimeMillis();
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		if (System.currentTimeMillis() > lastUpdateTime) {
			if (!cubeLimitSwitch.get() && cubeLimitSwitch != lastMode) {
				lastUpdateTime = System.currentTimeMillis() + 650;
				try {
					lastMode = cubeLimitSwitch;
					Robot.rioduinoSubsystem.setLEDMode(LEDModes.Cube);
				} catch (IOException e) {
					System.out.println(e);
				}
			} else if (!rungLimitSwitch.get() && rungLimitSwitch != lastMode) {
				lastUpdateTime = System.currentTimeMillis() + 650;
				try {
					lastMode = rungLimitSwitch;
					Robot.rioduinoSubsystem.setLEDMode(LEDModes.Rung);
				} catch (IOException e) {
					System.out.println(e);
				}
			} else if (lastMode != null && cubeLimitSwitch.get() && rungLimitSwitch.get()) {
				try {
					Robot.rioduinoSubsystem.setLEDMode(LEDModes.Idle);
					lastMode = null;
				} catch (IOException e) {
					System.out.println(e);
				}
			}
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return true;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
