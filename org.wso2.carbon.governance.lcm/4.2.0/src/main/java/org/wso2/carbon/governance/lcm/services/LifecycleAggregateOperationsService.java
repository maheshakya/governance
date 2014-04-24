package org.wso2.carbon.governance.lcm.services;

/**
 * Created by maheshakya on 4/23/14.
 */

import org.wso2.carbon.governance.lcm.util.CommonUtil;
import org.wso2.carbon.registry.common.services.RegistryAbstractAdmin;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.utils.RegistryUtils;

import org.wso2.carbon.governance.registry.extensions.utils.GovernanceAggregateOperations;



public class LifecycleAggregateOperationsService extends RegistryAbstractAdmin{


    private GovernanceAggregateOperations governanceAggregateOperations;

    public String testBatchValidate(){
        governanceAggregateOperations = new GovernanceAggregateOperations(getRootRegistry());

        String[] paths = new String[5];

        paths[0] = "/_system/governance/TestResource";
        paths[1] = "/abc";
        paths[2] = "/My Resource";
        paths[3] = "/resource_1";
        paths[4] = "/resource_2";

        governanceAggregateOperations.createBatch(paths, "TestID");

        return "gona";
    }

}
