package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1138.robot.OI;
import frc.team1138.robot.Robot;

public class AutonCommandGroup extends CommandGroup
{
	private final OI oi;

	public AutonCommandGroup()
	{
		requires(Robot.SUB_DRIVE_BASE);
		oi = new OI();
		addSequential(new DriveWithEncoders());
	}
}
