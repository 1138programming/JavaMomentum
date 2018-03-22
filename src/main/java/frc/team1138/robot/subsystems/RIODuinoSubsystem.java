package frc.team1138.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team1138.robot.commands.UpdateLedStatus;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

import java.io.IOException;


/**
* The RIODuinoSubsystem class
* 
* Edward Pedemonte
*/ 
public class RIODuinoSubsystem extends Subsystem
{
	public enum LEDModes {
		Off			((byte) 0),
		Idle		((byte) 1),
		Cube		((byte) 2),
		Rung		((byte) 3);

	    private final byte value;
	    private LEDModes(byte value) {
	        this.value = value;
	    }

	    public byte getValue() {
	        return value;
	    }
	}
	
	private enum RIODuinoUserCodeStatus {
		Success		((byte) 0),
		Error		((byte) 1);
		
	    private final byte value;
	    private RIODuinoUserCodeStatus(byte value) {
	        this.value = value;
	    }

	    public byte getValue() {
	        return value;
	    }
	}
	
	private enum DeviceByte {
		NeoPixel	((byte) 0),
		Ultrasonic	((byte) 1);
		
		private final byte value;
		private DeviceByte(byte value) {
			this.value = value;
		}
		
		public byte getValue() {
			return value;
		}
	}

	// Initialize the I2C port
	private final I2C Wire;// = new I2C(Port.kMXP, 1138);
	//private final SerialPort Serial;
	private byte[] received;
	
	public RIODuinoSubsystem()
	{
		System.out.println("LED Subsystem Initializing...");
		received = new byte[2];
		Wire = new I2C(Port.kMXP, 4);
		
		try {
			// Default to Off Mode
			setLEDMode(LEDModes.Off);
			System.out.println("LED Subsystem Initialized!");
		} catch (IOException e) {
			System.out.println("LED Subsystem Failed!");
			System.out.println(e);
		}
	}

	public void initDefaultCommand()
	{
		UpdateLedStatus defaultCommand = new UpdateLedStatus();
		defaultCommand.setRunWhenDisabled(true);
		setDefaultCommand(defaultCommand);
	}
	
	public void setLEDMode(LEDModes mode) throws IOException {
		// Turn the mode into a byte to send (from the enum declaration)
		byte[] toSend = new byte[2];
		toSend[0] = DeviceByte.NeoPixel.getValue();
		toSend[1] = mode.getValue();
		
		//Serial.write(toSend, 1);
		//System.out.println(Serial.read(1));
		
		if (Wire != null && toSend != null) {
			// Check that we have a proper I2C connection to avoid
			// NullPointerExceptions
			Wire.writeBulk(toSend, 2);
		}
		
		// Receive a response to check for an error
		Wire.readOnly(received, 1);
		
		// Do we have an error?
		if (received[0] == RIODuinoUserCodeStatus.Error.getValue()) {
			throw new IOException("User Code Error from rioDuino");
		}
	}
	
	public int getUltrasonic() throws IOException {
		byte[] toSend = new byte[1];
		toSend[0] = DeviceByte.Ultrasonic.getValue();
		if (Wire != null && toSend != null) {
			// Check that we have a proper I2C connection to avoid
			// NullPointerExceptions
			Wire.writeBulk(toSend, 1);
		}
		Wire.readOnly(received, 2);
		if (received[0] == RIODuinoUserCodeStatus.Error.getValue()) {
			throw new IOException("User Code Error from rioDuino");
		}
		return (int) received[1];
	}
}