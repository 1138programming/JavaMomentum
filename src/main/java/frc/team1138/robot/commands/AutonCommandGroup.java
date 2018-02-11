package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1138.robot.Robot;
import frc.team1138.robot.commands.DriveForward;
import frc.team1138.robot.commands.TurnWithGyro;

public class AutonCommandGroup extends CommandGroup
{
	public AutonCommandGroup()
	{
		requires(Robot.SUB_DRIVE_BASE);
		//addSequential(new TurnWithGyro());
		addSequential(new DriveForward());
		addSequential(new TurnWithGyro());
	}
}
