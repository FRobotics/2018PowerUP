package main.java.frc.team4150.robot.subsystem.drive;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Encoder;
import main.java.frc.team4150.robot.subsystem.base.SubsystemBase;
import main.java.frc.team4150.robot.util.Util;

public class EncoderSystem extends SubsystemBase {
	
	private ArrayList<Double> previousDistances;
	private Encoder encoder;
	
	public EncoderSystem(int port1, int port2, double radius, boolean invert) {
		encoder = new Encoder(port1, port2, invert, Encoder.EncodingType.k4X);
		encoder.setDistancePerPulse(radius * Util.inchPerDegree / 11.8);
		previousDistances = new ArrayList<Double>();
	}
	
	public EncoderSystem(int port1, int port2, double radius) {
		this(port1, port2, radius, false);
	}

	@Override
	public void init() {
		
	}

	@Override
	public void periodic() {
		
	}
	
	public double getDistance() {
		return encoder.getDistance();
	}
	
	public void resetDistance() {
		previousDistances.add(this.getDistance());
		this.encoder.reset();
	}
	
	public int getCount() {
		return encoder.get();
	}
	
}
