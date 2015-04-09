Feature: Cancel Plant Hire Request
  As a site engineer
  So that not to order plants 
  I want to cancel the Plant Hire Requests

   Scenario: Site engineer cancels the accepted PHR
    Given PHR in "ACCEPTED" status
    And Site Engineer is viewing "List of PHRs"
    When Site Engineer clicks the "cancelPHR" button
    Then Site Engineer should be able to see "CANCELLED" PHR status
    
   Scenario: Site engineer cancels the rejected PO
    Given PO in "REJECTED" status
    And Site Engineer is viewing "List of PHRs"
    When Site Engineer clicks the "cancelPHR" button
    Then Site Engineer should be able to see "CANCELLED" PHR status
    
    