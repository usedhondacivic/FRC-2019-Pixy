/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;
import edu.wpi.first.wpilibj.TimedRobot;
import io.github.pseudoresonance.pixy2api.Pixy2;
import io.github.pseudoresonance.pixy2api.Pixy2CCC.Block;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  Pixy2 pixy = Pixy2.createInstance(Pixy2.LinkType.SPI);

  @Override
  public void robotInit() {
    pixy.init();
  }
  
  @Override
  public void robotPeriodic() {
    int result = pixy.getCCC().getBlocks(false, 1, 1);
    //System.out.println(result);
    if(result != -0){
      ArrayList<Block> blocks = pixy.getCCC().getBlocks();
      if(blocks.size() > 0){
        System.out.println(blocks.get(0));
      }
    }
  }

  @Override
  public void autonomousInit() {
    
  }

  @Override
  public void autonomousPeriodic() {
    
  }

  @Override
  public void teleopInit() {
    //pixy.getVersionInfo().print();
  }

  @Override
  public void teleopPeriodic() {
  }

  @Override
  public void testPeriodic() {
  }
}
