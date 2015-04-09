Feature: Extend Plant Hire Request
  As a site engineer
  So that if plant longer needed
  I want to extend the Plant Hire Request

  Scenario: PENDING_CONFIRMATION status Extend PHR
    Given PHR in "PENDING_CONFIRMATION" status
    And Site Engineer is viewing "List of PHRs"
    When Site Engineer clicks the "extendPHR" button
    Then Site Engineer should be able update PHR
    
  Scenario: ACCEPTED status Extend PHR
    Given PHR in "ACCEPTED" status
    And Site Engineer is viewing "List of PHRs"
    When Site Engineer clicks the "extendPHR" button
    Then Site Engineer should be able update PHR
   