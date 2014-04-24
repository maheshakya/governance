/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.governance.api.aggregate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.governance.api.aggregate.BatchResourceBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * This provides aggregated operations for a batch of Assets.
 */

public class AggregateOperationsManager {

    private static final Log log = LogFactory.getLog(AggregateOperationsManager.class);
    private Registry registry;
    private Map<String, List<BatchResourceBean>> batches;
    private final int maximumNumberPerBatch ;
    private final String REGISTRY_ASPECTS = "registry.Aspects";
    private final String REGISTRY_LIFECYCLE = "registry.lifecycle.";

    /**
     * Constructor accepting an instance of the registry to use.
     *
     * @param registry the instance of the registry.
     */
    public AggregateOperationsManager(Registry registry) throws RegistryException {
        this.registry = registry;
        batches = new HashMap<String, List<BatchResourceBean>>();
        maximumNumberPerBatch = 100; //Temporary
    }
    public void selectBatch() throws GovernanceException{

    }

    /**
     * Creates a batch from a given array of resources. Each and every resource in the array should
     * have a life cycle attached to it self and should be in the same life cycle state in order to
     * complete the operation.
     *
     * @param resources an array of Resource objects of which a batch is needed to be created.
     * @param batchID ID of the batch
     *
     * @throws GovernanceException if the operation failed.
     */
    public void createBatch(Resource[] resources, String batchID) throws GovernanceException {

        if (resources != null && resources.length != 0){
            String message;

            List<BatchResourceBean> resourcesList = new ArrayList<BatchResourceBean>();
            String currentState;
            BatchResourceBean batchResource;

            try{
                batchResource = new BatchResourceBean(resources[0]);
            }catch (GovernanceException e){
                message = "Failed to initialize batch resource " + resources[0].toString();
                log.error(message);
                throw new GovernanceException(message, e);
            }

            try{
                currentState = batchResource.getResourceCurrentState();
            }catch (GovernanceException e){
                message = "No state found in resource " + resources[0].toString();
                log.error(message);
                throw new GovernanceException(message, e);
            }

            resourcesList.add(batchResource);

            String state;
            for (int i = 1; i < resources.length; i++){
                try{
                    batchResource = new BatchResourceBean(resources[i]);
                }catch(GovernanceException e){
                    message = "Failed to initialize batch resource " + resources[i].toString();
                    log.error(message);
                    throw new GovernanceException(message, e);
                }

                try{
                    state = batchResource.getResourceCurrentState();
                }catch(GovernanceException e){
                    message = "No state found in " + resources[i].toString();
                    log.error(message);
                    throw new GovernanceException(message, e);
                }


                if (!state.equals(currentState) ){
                    message = "States do not match. States: " + currentState + " and " + state;
                    throw new GovernanceException(message);
                }
                resourcesList.add(batchResource);
            }

            batches.put(batchID, resourcesList);
            System.out.println("Created batch "+batchID+" successfully.");

        }

        else{
            throw new GovernanceException("Could not find resources.");
        }
    }

    /**
     *
     * @param batchID
     * @param action
     * @param parameterMap
     * @throws GovernanceException
     */
    public void invokeCheckItem(String batchID, String action, Map<String,String> parameterMap)
            throws GovernanceException{
        if (batchID == null || action == null || parameterMap == null){
            throw new GovernanceException("*batchID, action or parameterMap cannot be null*");
        }
        if (!batches.containsKey(batchID)){
            throw new GovernanceException("batchID " + batchID + " not found");
        }
        boolean succeeded = false;
        try {
            int numberOfParameters = parameterMap.size();
            BatchResourceBean batchResource;
            String aspect;
            String path;

            registry.beginTransaction();

            System.out.println("Transaction began.");

            List<BatchResourceBean> resourceList = batches.get(batchID);
            for(int i=0; i<resourceList.size(); i++){
                if(resourceList.get(i).getCheckListItemsList().length != parameterMap.size()){
                    throw new GovernanceException("*Incompatible parameters or something*");
                }
                aspect = resourceList.get(i).getResourceLCName();
                path = resourceList.get(i).getResourcePath();



                for(int j=0; j<parameterMap.size(); j++){
                    if(!parameterMap.get(j + ".item").equals(resourceList.get(i).getCheckListItemsList()[j])){
                        System.out.println(parameterMap.get(j + ".item") + " " + resourceList.get(i).getCheckListItemsList()[j]);
                        registry.invokeAspect(path, aspect, action, parameterMap);

                    }
                }
            }

            succeeded = true;
        } catch(GovernanceException e){
            String message = "GovernanceException";
            throw new GovernanceException(message, e);
        } catch(RegistryException e){
            String message = "RegistryException";
            throw new GovernanceException(message, e);
        }finally {
            if(succeeded){
                try{
                    registry.commitTransaction();
                    System.out.println("Item check successful.");

                } catch(RegistryException e) {
                    throw new GovernanceException(e);
                }
            }
            else{
                try{
                    registry.rollbackTransaction();
                } catch (RegistryException e){

                }
            }
        }


    }

    /**
     *
     * @param batchID
     * @param action
     * @throws GovernanceException
     */
    public void invokeStateTransition(String batchID, String action) throws GovernanceException{
        if (batchID == null || action == null){
            throw new GovernanceException("*batchID or action cannot be null*");
        }
        boolean succeeded = false;
        try {
            registry.beginTransaction();

            succeeded = true;
        } catch(RegistryException e){
            throw new GovernanceException(e);
        }
    }

    /**
     *
     * @param batchID
     * @throws GovernanceException
     */
    public void removeBatch(String batchID) throws GovernanceException {
        if (batchID == null){
            throw new GovernanceException("*batchID or action cannot be null*");
        }
        batches.remove(batchID);
    }

    public void testFunction() {
        Resource[] resources = new Resource[5];
        try{
        resources[0] = registry.get("/_system/governance/TestResource");
        resources[1] = registry.get("/abc");
        resources[2] = registry.get("/My Resource");
        resources[3] = registry.get("/resource_1");
        resources[4] = registry.get("/resource_2");

        AggregateOperationsManager aggregateOperationsManager = new AggregateOperationsManager(registry);

        aggregateOperationsManager.createBatch(resources, "testID");

        String action = "itemClick";
        Map<String, String> parameterMap = new HashMap<String, String>();
        parameterMap.put("0.item", "false");
        parameterMap.put("0.item", "false");
        parameterMap.put("0.item", "false");

        aggregateOperationsManager.invokeCheckItem("testID",action, parameterMap);

        } catch (RegistryException e){
            System.out.println(e);
        }
    }

    public void testFunction_2(){
        String[] paths = new String[5];

        paths[0] = "/_system/governance/TestResource";
        paths[1] = "/abc";
        paths[2] = "/My Resource";
        paths[3] = "/resource_1";
        paths[4] = "/resource_2";


    }



}