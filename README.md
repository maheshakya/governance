WSO2 Governance Registry - Aggregate operations support for lifecyles
======================================================================

This repository contains the implementation of Aggregate operations support for lifecyles. This addresses feature indicated in Redmine : #2341: Lifecycle Integration (supporting aggregate operations) in the WSO2 Governance Registry project.

## Design details

![Basic design](  https://docs.google.com/drawings/d/1OBcYUIYWQCTWqfmOpEr9c6kAAjcM-mysaeX6N0AGkAo/pub?w=1327&h=746)


* `ArtifactBatchValidator` provides and interface for all custom validation methods. 
* `BatchResourceBean` stores the values path, aspect name, checklist item values, vote item values, state of resources which are required to perform batch validations and registry aspect invocations.
* `BatchValidateException` is a special type of exception for raised when batch validation fails.
* `LifecycleStateValidateUtil` implements `ArtifactBatchValidator`. This validates batches with the same lifecyle state at the present. Similarly, you can create any type of custom validators.
* `BatchOperations` contains checklist item checking and state transitions for batches. 
* Each custom validator has to be registered as an OSGi service. This will be done in `GovernanceRegistryextensionsComponent`. 
* In `GovernanceAggregateOperations`, these registered validators are searched using an OSGi service tracker to get the required validator and it will be used for proceeding batch operations. All batch operation are handled from this class.
* `LifecycleAggregateOperationsService` provides a service to use these aggregate operations. Methods in that service require `BatchResourceBean` object arrays for the respective resources on which the batch operations must be performed, with other required parameters for each operation.

**Class and method specific information is expressed in the comments for each of them.**

## Running and testing

The required methods to activate batch operations are implemented in the `LifecycleAggregateOperationsService`. They are as follows:

* `activateBatchValidation` : takes a `BatchResourceBean` array and `String` validator as parameters. Returns the boolean result of the validation.
* `activateBatchCheckItem` : takes a `BatchResourceBean` array, a `String` validator and a `HashMap` parameterMap as parameters. Returns the boolean result of the check items invocation.
* `activateBatchStateTransition` : takes a `BatchResourceBean` array, a `String` validator and a `String` action as parameters. Returns the boolean result of the validation.
* `getValidators` : takes a `BatchResourceBean` array and String validator as parameters. Returns the boolean result of the validation.

These methods are a example of how the aggregate operations implemented in `GovernanceAggregateOperations` can be used.

In order to test the functionality, I have created another class called `TestLifecycleAggregateOperationsService` in `governance.lcm.services`. First you have to build `governance.api` (version 4.2.1), `governance.registry.extensions` and `governance.lcm` using maven and copy the corresponding `jar` files into your servers' `repository/components/plugings`. Before running the test, you will have to do as follows:

1. Set `HideAdminServiceWSDLs` property to true in `repository/conf/carbon.xml` in your G-Reg server.
2. Start the server, create any two(2) resources and add lifecycle to them.

You will be able to see the test service from the following URL:
[https://localhost:9443/services/TestLifecycleAggregateOperationsService?wsdl](https://localhost:9443/services/TestLifecycleAggregateOperationsService?wsdl)

Note: You can use your IP address in the place of "localhost"

Now you can test the functionality using the SoapUI. Steps are as follows. 

1. Create a SOAP project using the above wsdl.
2. Use the Soap11bindings. For every request, set username and password(credentials in your G-Reg server).
3. There are three methods. You can test each by providing required parameters and making the request. If the corresponding aggregate operation is successful, you will get "success" as the result and vice versa for failure.

Following images depict a demonstration.

1. Batch validation: success
![Batch validation successful.](https://docs.google.com/drawings/d/1W0_wSS_GRjCxny7Xt3mZBWm3YGahEhEHbb2z4gKThG0/pub?w=960&h=720)

2. Batch validation: fail
![Batch validation fail](https://docs.google.com/drawings/d/129LkoZhqLPBjs7EzrbLGEEDdazbOr0euIa5jtrUiJDk/pub?w=960&h=720)

3. Check items: success
![check items success](https://docs.google.com/drawings/d/1MgQn-JYlhbvfnsCv0_WdvKFhG4UUotPfwCzSUHOPcJU/pub?w=960&h=720)

You can check whether the operations are successful by checking the resource information in the the UI of the G-Reg server.
