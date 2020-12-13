Feature: Key/value processing v.1

  Scenario: Single file path

    Given remote repo has file team1/test-app.yml in branch test-branch with the following content:
      """
      my-app:
        simple-key: my-value
        list-key1:
          - list-value1
          - list-value2
        list-key2: [list-value3, list-value4]
      """

    When GET request to /api/keyValue/v1/test-branch/team1/test-app.yml is made

    Then the last GET request returns the following:
      """
      my-app.simple-key=my-value
      my-app.list-key1[0]=list-value1
      my-app.list-key1[1]=list-value2
      my-app.list-key2[0]=list-value3
      my-app.list-key2[1]=list-value4
      """

  Scenario: Directory path

    Given remote repo has file team1/app/common/common.yml in branch test-branch with the following content:
      """
      my-app:
        my-key: my-value
      """

    When GET request to /api/keyValue/v1/test-branch/team1/app is made

    Then the last GET request returns the following:
      """
      my-app.my-key=my-value
      """

  Scenario: Hierarchy

    Given remote repo has file team1/common/common.yml in branch test-branch with the following content:
      """
      my-app:
        key1: common-value1
        key2: common-value2
      """

    And remote repo has file team1/production/production-common.yml in branch test-branch with the following content:
      """
      my-app:
        key2: production-value2
      """

    When GET request to /api/keyValue/v1/test-branch/team1/common,team1/production is made

    Then the last GET request returns the following:
      """
      my-app.key1=common-value1
      my-app.key2=production-value2
      """

  Scenario: Branch is respected

    Given remote repo has file team1/common.yml in branch test-branch with the following content:
      """
      my-app:
        key1: value1
      """

    When GET request to /api/keyValue/v1/master/team1 is made

    Then the last GET request has code 400