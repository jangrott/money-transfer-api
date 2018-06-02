Feature: Transfer REST API

  Scenario: Transfer between two clients accounts
    Given system contains following clients
      | firstName | lastName |
      | John      | Doe      |
      | Jane      | Doe      |
    And clients have following accounts
      | firstName | lastName | accountBalance |
      | John      | Doe      | 10000.0        |
      | Jane      | Doe      | 20000.0        |
    When client requests POST /transfer with following body
      | fromAccount | <JohnDoeAccountId> |
      | toAccount   | <JaneDoeAccountId> |
      | amount      | 5000.0             |
    Then response status is 200
    And balance of accounts are following
      | accountId        | balance |
      | <JohnDoeAccount> | 5000.0  |
      | <JaneDoeAccount> | 25000.0 |