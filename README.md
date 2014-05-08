WSO2 Governance Registry - Aggregate operations support for lifecyles
=====================================================================

This repository contains the implementation of Aggregate operations support for lifecyles. This addresses feature indicated in Redmine issue #2341: Lifecycle Integration (supporting aggregate operations) in the WSO2 Governance Registry project.

## Design details

![Basic design](  https://docs.google.com/drawings/d/1OBcYUIYWQCTWqfmOpEr9c6kAAjcM-mysaeX6N0AGkAo/pub?w=1327&h=746)


* ArtifactBatchValidator provides and interface for all custom validation methods. 
* BatchResourceBean stores the values path, aspect name, checklist item values, vote item values, state of resources which are required to perform batch validations and registry aspect invocations.
* BatchValidateException is a special type of exception for raised when batch validation fails.
* LifecycleStateValidateUtil implements ArtifactBatchValidator. This validates batches with the same lifecyle state at the present. Similarly, you can create any type of custom validators.
* BatchOperations contains checklist item checking and state transitions for batches. 
* Each custom validator has to be registered as an OSGi service. This will be done in GovernanceRegistryextensionsComponent. 
* In GovernanceAggregateOperations, these registered validators are searched using an OSGi service tracker to get the required validator and it will be used for proceeding batch operations. All batch operation are handled from this class.
* LifecycleAggregateOperationsService provides a service to use these aggregate operations. Methods in that service require BatchResourceBean object arrays for the respective resources on which the batch operations must be performed, with other required parameters for each operation.

