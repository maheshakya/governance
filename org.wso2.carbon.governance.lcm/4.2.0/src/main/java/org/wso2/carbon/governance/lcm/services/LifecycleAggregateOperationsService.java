package org.wso2.carbon.governance.lcm.services;

/**
 * Created by maheshakya on 4/23/14.
 */

import org.wso2.carbon.governance.lcm.util.CommonUtil;
import org.wso2.carbon.registry.common.services.RegistryAbstractAdmin;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.utils.RegistryUtils;

import org.wso2.carbon.governance.registry.extensions.utils.GovernanceAggregateOperations;

import java.util.HashMap;
import java.util.Map;


public class LifecycleAggregateOperationsService extends RegistryAbstractAdmin{


    private GovernanceAggregateOperations governanceAggregateOperations;

    public  String testBatchValidate(){

        String[] paths = new String[5];

        paths[0] = "/abc";
        paths[1] = "/_system/governance/TestResource";
        paths[2] = "/My Resource";
        paths[3] = "/resource_1";
        paths[4] = "/resource_2";


        boolean result = GovernanceAggregateOperations.createBatch(paths, "lifecyleValidation");
        String str;

        if (result){
            str = "Success";
        }
        else{
            str = "Failure";
        }


        return str;
    }

    public String testBatchCheckItem(String item_1, String item_2, String item_3){
        String[] paths = new String[5];
        governanceAggregateOperations = new GovernanceAggregateOperations();

        paths[0] = "/abc";
        paths[1] = "/_system/governance/TestResource";
        paths[2] = "/My Resource";
        paths[3] = "/resource_1";
        paths[4] = "/resource_2";

        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("0.item", item_1);
        parameterMap.put("1.item", item_2);
        parameterMap.put("2.item", item_3);


        boolean result = governanceAggregateOperations.bachtCheckItemInvoke(paths, parameterMap, "lifecyleValidation");
        String str;

        if (result){
            str = "Success";
        }
        else{
            str = "Failure";
        }


        return str;
    }

    public String testBatchStateTransition(String action){
        String[] paths = new String[5];
        governanceAggregateOperations = new GovernanceAggregateOperations();

        paths[0] = "/abc";
        paths[1] = "/_system/governance/TestResource";
        paths[2] = "/My Resource";
        paths[3] = "/resource_1";
        paths[4] = "/resource_2";

        boolean result = governanceAggregateOperations.batchStateTransitionInvoke(paths, action, "lifecyleValidation");
        String str;

        if (result){
            str = "Success";
        }
        else{
            str = "Failure";
        }


        return str;
    }

}
