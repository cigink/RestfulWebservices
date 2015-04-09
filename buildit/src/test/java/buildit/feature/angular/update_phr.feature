Feature: Update Plant Hire Request
  As a site engineer
  So that I can order plants
  I want to update the rejected Plant Hire Requests

  Scenario: Rejected PHR is updated by Site Engineer  
    Given PHR in "REJECTED" status
    And Site Engineer is viewing "List Plant Hire Requests"
    When Site Engineer clicks the "updatePHR" button
    Then Site Engineer should be able change PHR
 
  Scenario: Site Engineer don't want to update Rejected PHR
    Given PHR in "REJECTED" status
    And Site Engineer is viewing "List Plant Hire Requests"
	When Site Engineer clicks the "updatePHR" button
	And the Plants is not listed
    Then Site Engineer should be able cancel the PHR
   