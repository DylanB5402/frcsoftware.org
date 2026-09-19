/*
 * Copyright 2026 FRCSoftware
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */
package first.robot.opmode;

import static org.wpilib.units.Units.Seconds;

import first.robot.Robot;
import org.wpilib.command3.Scheduler;
import org.wpilib.opmode.PeriodicOpMode;

public class DriveStraight extends PeriodicOpMode {
  private final Robot robot;

  public DriveStraight(Robot robot) {
    this.robot = robot;
  }

  @Override
  public void start() {
    var autoCommand = robot.drivetrain.arcadeDrive(() -> 0.5, () -> 0).withTimeout(Seconds.of(4));
    Scheduler.getDefault().schedule(autoCommand);
  }
}
