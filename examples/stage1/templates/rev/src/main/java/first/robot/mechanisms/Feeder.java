/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot.mechanisms;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import first.robot.simulation.SingleFlywheelSim;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.framework.RobotBase;
import org.wpilib.hardware.bus.CANPort;

public class Feeder implements Mechanism {

  private SparkMax motor = new SparkMax(CANPort.CAN_S0, 5, MotorType.kBrushless);

  private final SingleFlywheelSim sim = SingleFlywheelSim.forFeeder(motor);

  public Feeder() {
    setDefaultCommand(idle());
  }

  public Command feed() {
    return runRepeatedly(() -> motor.setThrottle(0.75)).named("FeederFeed2");
  }

  public Command intake() {
    return runRepeatedly(() -> motor.setThrottle(-1)).named("FeederIntake");
  }

  public Command outtake() {
    return runRepeatedly(() -> motor.setThrottle(1)).named("FeederOuttake");
  }

  public Command idle() {
    return runRepeatedly(() -> motor.setThrottle(0)).named("FeederIdle");
  }

  public void periodic() { // Update the simulation
    if (RobotBase.isSimulation()) {
      sim.periodic();
    }
  }
}
