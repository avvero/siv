Feature: Client's scenarios
  Scenario: Client registration and funding
    When: Client with id <clientId> is registered
    And: Account with id <accountId> is created for client with id <clientId>
    Then: Account with id <accountId> is funded on <amount> USD