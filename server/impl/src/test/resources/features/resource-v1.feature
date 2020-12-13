Feature: Resources processing v.1

  Scenario: Available resource

    Given remote repo has file team1/test-app/common.yml in branch test-branch with the following content:
      """
      my-team:
        my-key: my-value
      """

    When GET request to /api/resource/v1/test-branch/team1/test-app/common.yml is made

    Then the last GET request returns the following:
      """
      my-team:
        my-key: my-value
      """