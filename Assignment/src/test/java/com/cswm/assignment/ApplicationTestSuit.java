package com.cswm.assignment;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)				
@Suite.SuiteClasses({				
  ExecutionTest.class,
  InstrumentTest.class, 
  orderBookTest.class,
  OrderTest.class
})	
public class ApplicationTestSuit {

}
