/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot.mechanisms;

import static org.wpilib.units.Units.Seconds;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import first.robot.simulation.SingleFlywheelSim;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.command3.NeedsNameBuilderStage;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;

public class IntakeLauncher implements Mechanism {

  private SparkMax motor = new SparkMax(CANPort.CAN_S0, 4, MotorType.kBrushless);

  private final SingleFlywheelSim sim = SingleFlywheelSim.forIntakeLauncher(motor);

  public IntakeLauncher() {
    setDefaultCommand(idle());
  }

  public NeedsNameBuilderStage setThrottle(double throttle) {
    return runRepeatedly(() -> motor.setThrottle(throttle));
  }

  public Command shoot() {
    return run(coroutine -> {
          coroutine.wait(Seconds.of(2));
          coroutine.await(setThrottle(0.9).named("IntakeLauncherSetThrottle (0.9)"));
        })
        .named("IntakeLauncherShoot");
  }

  public Command idle() {
    return setThrottle(0).named("IntakeLauncherIdle");
  }

  public Command intake() {
    return setThrottle(0.8).named("IntakeLauncherIntake");
  }

  public Command outtake() {
    return setThrottle(-0.8).named("IntakeLauncherOuttake");
  }

  public void periodic() { // Update the simulation
    if (RobotBase.isSimulation()) {
      sim.periodic();
    }
  }
}
