/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot.mechanisms;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import first.robot.simulation.DrivetrainSim;
import java.util.function.DoubleSupplier;
import org.wpilib.command3.Command;
import org.wpilib.command3.Mechanism;
import org.wpilib.drive.DifferentialDrive;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.imu.OnboardIMU;
import org.wpilib.hardware.imu.OnboardIMU.MountOrientation;

public class Drivetrain implements Mechanism {

  private SparkMax leftLeader = new SparkMax(CANPort.CAN_S0, 0, MotorType.kBrushless);
  private SparkMax leftFollower = new SparkMax(CANPort.CAN_S0, 1, MotorType.kBrushless);
  private SparkMax rightLeader = new SparkMax(CANPort.CAN_S0, 2, MotorType.kBrushless);
  private SparkMax rightFollower = new SparkMax(CANPort.CAN_S0, 3, MotorType.kBrushless);

  public final DifferentialDrive drivetrain =
      new DifferentialDrive(leftLeader::setThrottle, rightLeader::setThrottle);
  private OnboardIMU imu = new OnboardIMU(MountOrientation.FLAT);

  DrivetrainSim sim = new DrivetrainSim(leftLeader, rightLeader);

  public Drivetrain() {
    var leftConfig = new SparkMaxConfig();
    leftConfig.inverted(true);
    leftLeader.configure(
        leftConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    leftFollower.configure(
        leftConfig.follow(leftLeader),
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    var rightConfig = new SparkMaxConfig();
    rightConfig.inverted(false);
    rightLeader.configure(
        rightConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    rightFollower.configure(
        rightConfig.follow(rightLeader),
        ResetMode.kResetSafeParameters,
        PersistMode.kPersistParameters);

    setDefaultCommand(idle());
  }

  public void periodic() {
    sim.periodic();
  }

  @Override
  public Command idle() {
    return runRepeatedly(() -> drivetrain.arcadeDrive(0, 0)).named("DrivetrainIdle");
  }

  public Command arcadeDrive(DoubleSupplier forward, DoubleSupplier rotation) {
    return runRepeatedly(
            () -> drivetrain.arcadeDrive(forward.getAsDouble(), rotation.getAsDouble()))
        .named("DrivetrainArcadeDrive");
  }
}
