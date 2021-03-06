package org.wso2.carbon.governance.lcm.services;

/**
 * Created by maheshakya on 5/8/14.
 * This is test service
 * Please remove this after testing.
 *
 * In order to run this you will need to create resources as follows in G-Reg server
 *
 * resource names with paths:
 * 1. /abc
 * 2. /My Resource
 *
 * Add any lifecycle to these resources. Use appropriate values when testing
 */

import org.wso2.carbon.registry.common.services.RegistryAbstractAdmin;
import org.wso2.carbon.governance.api.util.BatchResourceBean;
import org.wso2.carbon.governance.registry.extensions.utils.GovernanceAggregateOperations;

import java.util.Map;
import java.util.HashMap;

public class TestLifecycleAggregateOperationsService extends RegistryAbstractAdmin{

    private BatchResourceBean[] batchResourceBeans;

    private GovernanceAggregateOperations governanceAggregateOperations;

    public  String testBatchValidate(String state0, String state1){

        batchResourceBeans = new BatchResourceBean[2];
        batchResourceBeans[0] = new BatchResourceBean();
        batchResourceBeans[1] = new BatchResourceBean();

        batchResourceBeans[0].setResourceCurrentState(state0);
        batchResourceBeans[1].setResourceCurrentState(state1);

        boolean result = GovernanceAggregateOperations.createBatch(batchResourceBeans, "lifecyleStateValidation");
        String str;

        if (result){
            str = "Success";
        }
        else{
            str = "Failure";
        }


        return str;
    }

    public String testBatchCheckItem(String state0, String state1,
                                     String param_0, String param_1, String param_2,
                                     String resource0_item0, String resource0_item1,String resource0_item2,
                                     String resource1_item0, String resource1_item1, String resource1_item2,
                                     String lc0, String lc1, String path0, String path1){

        String[] checkItemList0 = new String[3];
        checkItemList0[0] = resource0_item0;
        checkItemList0[1] = resource0_item1;
        checkItemList0[2] = resource0_item2;

        String[] checkItemList1 = new String[3];
        checkItemList1[0] = resource1_item0;
        checkItemList1[1] = resource1_item1;
        checkItemList1[2] = resource1_item2;

        String[] paths = new String[2];
        paths[0] = path0;
        paths[1] = path1;

        String[] lcNames = new String[2];
        lcNames[0] = lc0;
        lcNames[1] = lc1;

        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("0.item", param_0);
        parameterMap.put("1.item", param_1);
        parameterMap.put("2.item", param_2);


        batchResourceBeans = new BatchResourceBean[2];
        batchResourceBeans[0] = new BatchResourceBean();
        batchResourceBeans[1] = new BatchResourceBean();

        batchResourceBeans[0].setResourceCurrentState(state0);
        batchResourceBeans[1].setResourceCurrentState(state1);

        batchResourceBeans[0].setCheckListItemsList(checkItemList0);
        batchResourceBeans[1].setCheckListItemsList(checkItemList1);

        batchResourceBeans[0].setResourcePath(paths[0]);
        batchResourceBeans[1].setResourcePath(paths[1]);

        batchResourceBeans[0].setResourceLCName(lcNames[0]);
        batchResourceBeans[1].setResourceLCName(lcNames[1]);

        governanceAggregateOperations = new GovernanceAggregateOperations();


        boolean result = governanceAggregateOperations.batchCheckItemInvoke(batchResourceBeans,
                parameterMap, "lifecyleStateValidation");

        String str;

        if (result){
            str = "Success";
        }
        else{
            str = "Failure";
        }


        return str;
    }

    public String testBatchStateTransition(String action, String state0, String state1,
                                           String resource0_item0, String resource0_item1,String resource0_item2,
                                           String resource1_item0, String resource1_item1, String resource1_item2,
                                           String lc0, String lc1, String path0, String path1){
        String[] checkItemList0 = new String[3];
        checkItemList0[0] = resource0_item0;
        checkItemList0[1] = resource0_item1;
        checkItemList0[2] = resource0_item2;

        String[] checkItemList1 = new String[3];
        checkItemList1[0] = resource1_item0;
        checkItemList1[1] = resource1_item1;
        checkItemList1[2] = resource1_item2;

        String[] paths = new String[2];
        paths[0] = path0;
        paths[1] = path1;

        String[] lcNames = new String[2];
        lcNames[0] = lc0;
        lcNames[1] = lc1;

        batchResourceBeans = new BatchResourceBean[2];
        batchResourceBeans[0] = new BatchResourceBean();
        batchResourceBeans[1] = new BatchResourceBean();

        batchResourceBeans[0].setResourceCurrentState(state0);
        batchResourceBeans[1].setResourceCurrentState(state1);

        batchResourceBeans[0].setCheckListItemsList(checkItemList0);
        batchResourceBeans[1].setCheckListItemsList(checkItemList1);

        batchResourceBeans[0].setResourcePath(paths[0]);
        batchResourceBeans[1].setResourcePath(paths[1]);

        batchResourceBeans[0].setResourceLCName(lcNames[0]);
        batchResourceBeans[1].setResourceLCName(lcNames[1]);

        governanceAggregateOperations = new GovernanceAggregateOperations();

        boolean result = governanceAggregateOperations.batchStateTransitionInvoke(batchResourceBeans,
                action, "lifecyleStateValidation");
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
