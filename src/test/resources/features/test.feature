@api @users
Feature: Users API Validation

  @positive @smoke
  Scenario: Create a new user with valid data
    Given user payload with
      | name   | email            | gender | status |
      | Manish | manishHarshupt@test.com | male   | active |
    And authorization token is available
    When client sends POST request to "/users"
    Then the response status code should be 201
    And the response should contain the created user details

  @positive @get @smoke
  Scenario: Get created user by id
    Given authorization token is available
    When client sends GET request to "/users/{user_id}"
    Then the response status code should be 200
    And the response should contain user details
    
    @smoke
    Scenario: Create user with duplicate email
    Given user payload with
      | name | email            | gender | status |
      | Test | manisha@test.com | male   | active |
    And authorization token is available
    When client sends POST request to "/users"
    Then the response status code should be 422
    And the response should contain an "has already been taken" as message

  #This scenario doesn't work for gorest
  @negative @auth
  Scenario: Create user without authorization token
    Given user payload with
      | name  | email          | gender | status |
      | Rahul | rahul@test.com | male   | active |
    And no authorization token
    When client sends POST request to "/users" without token
    Then the response status code should be 401
    And the response should contain an "missing token" as message

  #Done
  @negative @auth
  Scenario: Create user with invalid authorization token
    Given user payload with
      | name | email         | gender | status |
      | Ravi | ravi@test.com | male   | active |
    And invalid authorization token
    When client sends POST request to "/users"
    Then the response status code should be 401
    And the response should contain an "Invalid token" as message

  #Done
  @negative @validation
  Scenario Outline: Create user with missing required field
    Given invalid user payload missing "<field>"
    And authorization token is available
    When client sends POST request to "/users"
    Then the response status code should be 422
    And the response should contain an error message

    Examples: 
      | field  |
      | name   |
      | email  |
      | gender |
      | status |

  @positive @chaining
  Scenario: Update created user
    Given user payload with
      | name        | email            | gender | status |
      | UpdatedUser | updated@test.com | female | active |
    And authorization token is available
    When client sends PUT request to "/users/{user_id}" with
      | name        | email            | gender | status |
      | UpdatedUser | updated@test.com | female | active |
    Then the response status code should be 200
    And the response should contain updated user details

  @positive @chaining
  Scenario: Delete created user
    Given authorization token is available
    When client sends DELETE request to "/users/{user_id}"
    Then the response status code should be 204

  @negative @validation
  Scenario Outline: Create user with invalid email format
    Given user payload with
      | name   | email     | gender | status |
      | Manish | "<email>" | male   | active |
    And authorization token is available
    When client sends POST request to "/users"
    Then the response status code should be 422
    And the response should contain an "is invalid" as message

    Examples: 
      | email     |
      | test@     |
      | test.com  |
      | @mail.com |

  @negative @validation
  Scenario Outline: Create user with invalid enum values
    Given user payload with
      | name   | email   | gender   | status   |
      | <name> | <email> | <gender> | <status> |
    And authorization token is available
    When client sends POST request to "/users"
    Then the response status code should be 422
    And the response should contain an "<message>" as message

    Examples: 
      | name | email        | gender  | status  | message                               |
      | Test | manishc@test | unknown | active  | can't be blank, can be male of female |
      | Test | manishc@test | male    | pending | can't be blank                        |

  @negative @get
  Scenario: Get user with non existing id
    Given authorization token is available
    When client sends GET request to "/users/99999999"
    Then the response status code should be 404
    And the response should contain an "Resource not found" as message
