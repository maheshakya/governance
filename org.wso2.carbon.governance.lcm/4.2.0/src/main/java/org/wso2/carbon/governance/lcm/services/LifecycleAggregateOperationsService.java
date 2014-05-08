/*
*  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.carbon.governance.lcm.services;

import org.wso2.carbon.registry.common.services.RegistryAbstractAdmin;
import org.wso2.carbon.governance.api.util.BatchResourceBean;
import org.wso2.carbon.governance.registry.extensions.utils.GovernanceAggregateOperations;

import java.util.Map;


public class LifecycleAggregateOperationsService extends RegistryAbstractAdmin{


    private GovernanceAggregateOperations governanceAggregateOperations;

    /**
     *
     * @param batchResourceBeans
     * @param validator
     * @return status of batch validation
     */
    public boolean activateBatchValidation(BatchResourceBean[] batchResourceBeans, String validator){
        boolean success;
        success = GovernanceAggregateOperations.createBatch(batchResourceBeans, validator);
        return success;
    }

    /**
     *
     * @param batchResourceBeans
     * @param parameterMap
     * @param validator
     * @return status of check item invocation
     */
    public boolean activateBatchCheckItem(BatchResourceBean[] batchResourceBeans,
                                          Map<String, String> parameterMap, String validator){
        boolean success;
        governanceAggregateOperations = new GovernanceAggregateOperations();
        success = governanceAggregateOperations.batchCheckItemInvoke(batchResourceBeans,parameterMap, validator);
        return success;
    }

    /**
     *
     * @param batchResourceBeans
     * @param action
     * @param validator
     * @return status of state transition invocation
     */
    public boolean activateBatchStateTransition(BatchResourceBean[] batchResourceBeans,
                                          String action, String validator){
        boolean success;
        governanceAggregateOperations = new GovernanceAggregateOperations();
        success = governanceAggregateOperations.batchStateTransitionInvoke(batchResourceBeans, action, validator);
        return success;
    }

    /**
     * This method can be used to retrieve the names of available validators
     * @return an array of validator names
     */
    public String[] getValidators(){
        return GovernanceAggregateOperations.getRegisteredValidators();
    }

}
