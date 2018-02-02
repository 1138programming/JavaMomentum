package MotionProfile;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Notifier;

public class ProfileExecutor {

    // Private, constant declarations.
    private final int minBufferPoints = 5;

    private final int IDLE = 0;
    private final int BUFFERING = 1;
    private final int EXECUTING = 2;

    // Declarations that are important and stuff.
    private TalonSRX motor;
    private MotionProfileStatus status;
    private MotionProfile profile;

    // Declarations that service the motion profile loop.
    double pos, vel, heading;
    private int state;
    private boolean start;
    private SetValueMotionProfile setValue;

    public ProfileExecutor(TalonSRX motor, MotionProfile profile) {
        this.motor = motor;
        this.profile = profile;
        this.status = new MotionProfileStatus();

        this.state = 0;
        this.start = false;
        this.setValue = SetValueMotionProfile.Disable;
    }

    public void startMotionProfile() {
        Notifier notifier = new Notifier(new ProfileNotifier(motor));

        motor.changeMotionControlFramePeriod(50);
        notifier.startPeriodic(0.005);

        start = true;
    }

    public void resetMotionProfile() {
        motor.clearMotionProfileTrajectories();
        setValue = SetValueMotionProfile.Disable;
        state = 0;
        start = false;
    }

    public void controlMotionProfile() {
        motor.getMotionProfileStatus(status);

        if (motor.getControlMode() != ControlMode.MotionProfile) {
            state = IDLE;
        } else {
            switch (state) {
                case IDLE:
                    if (start) {
                        start = false;
                        setValue = SetValueMotionProfile.Disable;

                        startFilling();

                        state = BUFFERING;
                    }
                    break;
                case BUFFERING:
                    if (status.btmBufferCnt > minBufferPoints) {
                        setValue = SetValueMotionProfile.Enable;
                        state = EXECUTING;
                    }
                    break;
                case EXECUTING:
                    if (status.activePointValid && status.isLast) {
                        setValue = SetValueMotionProfile.Hold;
                        state = IDLE;
                    }
            }
        }

        motor.getMotionProfileStatus(status);
        heading = motor.getActiveTrajectoryHeading();
        pos = motor.getActiveTrajectoryPosition();
        vel = motor.getActiveTrajectoryVelocity();

    }

    private void startFilling() {
        TrajectoryPoint point = new TrajectoryPoint();

        if (status.hasUnderrun) {
            motor.clearMotionProfileHasUnderrun(0);
        }

        motor.clearMotionProfileTrajectories();

        /* set the base trajectory period to zero, use the individual trajectory period below */
        motor.changeMotionControlFramePeriod(50);

        for (int i = 0; i < profile.getPoints().length; i++) {
            double positionRot = profile.getPoints()[i][0];
            double velocityRPM = profile.getPoints()[i][1];
            /* for each point, fill our structure and pass it to API */
            point.position = positionRot * profile.getSensorUnitsPerRotation(); //Convert Revolutions to Units
            point.velocity = velocityRPM * profile.getSensorUnitsPerRotation() / 600.0; //Convert RPM to Units/100ms
            point.headingDeg = 0; /* future feature - not used in this example*/
            point.profileSlotSelect0 = 0; 
            point.profileSlotSelect1 = 0;
            point.zeroPos = false;
            point.isLastPoint = false;

            if (i == 0) {
                point.zeroPos = true;
            }

            if ((i + 1) == profile.getPoints().length) {
                point.isLastPoint = true;
            }

            motor.pushMotionProfileTrajectory(point);
        }

    }

    /**
     *
     * @return the output value to pass to Talon's set() routine. 0 for disable
     *         motion-profile output, 1 for enable motion-profile, 2 for hold
     *         current motion profile trajectory point.
     */
    public SetValueMotionProfile getSetValue() {
        return setValue;
    }

}
