package main.java.frc.team4150.robot.command;

import main.java.frc.team4150.robot.RobotBase;
import main.java.frc.team4150.robot.command.base.Command;
import main.java.frc.team4150.robot.subsystem.SolenoidSystem;
import main.java.frc.team4150.robot.util.Time;
import main.java.frc.team4150.robot.util.Time.Unit;

public class SetSolenoidCommand extends Command {
	private long startTime;
	private Time wait;
	private SolenoidSystem solenoid;
	private boolean direction;
	
	public SetSolenoidCommand (SolenoidSystem solenoid, boolean direction, Time wait) {
		this.solenoid = solenoid;
		this.direction = direction;
		this.wait = wait;
	}

	@Override
	public void init() {
		startTime = System.currentTimeMillis();
		solenoid.set(direction);
	}

	@Override
	public boolean periodic(RobotBase robot) {
		if (System.currentTimeMillis() - startTime > wait.to(Unit.MILLIS)) {
			return true;
		}
		return false;
	}
}
