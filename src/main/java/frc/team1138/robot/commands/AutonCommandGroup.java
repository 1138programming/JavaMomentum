package frc.team1138.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import frc.team1138.robot.OI;
import frc.team1138.robot.Robot;
import frc.team1138.robot.commands.DriveForward;
import frc.team1138.robot.commands.TurnWithGyro;

public class AutonCommandGroup extends CommandGroup
{
	private OI oi;

	public AutonCommandGroup()
	{
		requires(Robot.SUB_DRIVE_BASE);
		oi = new OI();
		//addSequential(new TurnWithGyro());
		addSequential(new DriveForward());
		addSequential(new TurnWithGyro());
	}
}
