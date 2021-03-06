package main.java.frc.team4150.robot;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import main.java.frc.team4150.robot.command.base.Command;
import main.java.frc.team4150.robot.command.base.CommandManager;
import main.java.frc.team4150.robot.input.InputEnum;
import main.java.frc.team4150.robot.input.joystick.ControllerInput;
import main.java.frc.team4150.robot.subsystem.base.SubsystemEnum;
import main.java.frc.team4150.robot.subsystem.motor.MotorSystem;

public abstract class RobotBase extends IterativeRobot {

    private CommandManager commandManager;
    private SubsystemEnum[] subsystemEnums;
    private InputEnum[] inputEnums;

    public RobotBase(SubsystemEnum[] subsystemEnums, InputEnum[] inputEnums) {
        super();
        this.commandManager = new CommandManager();
        this.subsystemEnums = subsystemEnums;
        this.inputEnums = inputEnums;
    }

    /**
     * Robot initialization code here; called when the robot starts
     */
    public abstract void start();

    /**
     * Push all commands in this method; called once when autonomous starts
     * This system will probably be redone to be more secure/user friendly
     */
    public abstract void addCommands();

    /**
     * Initialization code for teleop; called once when teleop starts
     */
    public abstract void teleopStart();

    /**
     * Main code for teleop; periodically called until teleop ends
     */
    public abstract void teleopLoop();
    
    public abstract void stopLoop();
    
    public abstract void updateNTVariables();

    @Override
    public void robotInit() {
        //initialize all the subsystems
        for (SubsystemEnum subsystem : subsystemEnums) {
            subsystem.getSubsystem().init();
        }
        start();
        CameraServer.getInstance().startAutomaticCapture();
    }

    @Override
    public void teleopInit() {
    	SmartDashboard.putBoolean("vars/teleopEnabled", true);
        teleopStart();
    }

    @Override
    public void teleopPeriodic() {
        teleopLoop();
        //commandManager.periodic(this);
        for (SubsystemEnum subsystem : subsystemEnums) {
            subsystem.getSubsystem().periodic();
        }
        for (InputEnum inputEnum : this.inputEnums) {
            if (inputEnum.getInput() instanceof ControllerInput) {
                ControllerInput joystickInput = (ControllerInput) inputEnum.getInput();
                joystickInput.postPeriodic();
            }
        }
        updateNTVariables();
        periodic();
    }

    @Override
    public void autonomousInit() {
    	SmartDashboard.putBoolean("vars/teleopEnabled", false);
    	this.commandManager.clear();
        addCommands();
    }

    @Override
    public void autonomousPeriodic() {
        commandManager.periodic(this);
        for (SubsystemEnum subsystem : subsystemEnums) {
            subsystem.getSubsystem().periodic();
        }
        updateNTVariables();
        periodic();
    }
    
    @Override
    public void disabledInit() {
    	SmartDashboard.putBoolean("vars/teleopEnabled", false);
    	this.commandManager.clear();
    	for (SubsystemEnum se : this.subsystemEnums) {
    		if(se.getSubsystem() instanceof MotorSystem) {
    			MotorSystem ms = (MotorSystem)se.getSubsystem();
    			ms.forceStop();
    		}
    	}
    }
    
    @Override
    public void disabledPeriodic() {
    	stopLoop();
    	for (InputEnum inputEnum : this.inputEnums) {
            if (inputEnum.getInput() instanceof ControllerInput) {
                ControllerInput joystickInput = (ControllerInput) inputEnum.getInput();
                joystickInput.postPeriodic();
            }
        }
    	updateNTVariables();
    }
    
    /**
     * Pushes a command to the command manager
     * @param command - the command you want to push
     */
    public void addCommand(Command command){
    	this.commandManager.push(command);
    }
    
    public void periodic() {}
}
