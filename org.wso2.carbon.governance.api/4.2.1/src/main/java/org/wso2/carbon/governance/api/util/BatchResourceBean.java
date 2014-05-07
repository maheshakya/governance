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
package org.wso2.carbon.governance.api.util;

import org.wso2.carbon.governance.api.exception.BatchValidateException;

public class BatchResourceBean {

    private String resourcePath;
    private String resourceLCName;
    private String resourceCurrentState;
    private String[] checkListItemsList;
    private String[] voteItemsList;

    /**
     *
     * @return resource ath of the BatchResource
     * @throws BatchValidateException
     */
    public String getResourcePath() throws BatchValidateException {
        if(this.resourcePath == null){
            throw new BatchValidateException("Resource path not found");
        }
        return this.resourcePath;
    }

    /**
     *
     * @return resource life cycle of the BatchResource
     * @throws BatchValidateException
     */
    public String getResourceLCName() throws BatchValidateException{
        if(this.resourceLCName == null){
            throw new BatchValidateException("Resource Life cycle not found");
        }
        return this.resourceLCName;
    }

    /**
     *
     * @return current state of the BatchResource
     * @throws BatchValidateException
     */
    public String getResourceCurrentState() throws BatchValidateException{
        if(this.resourceCurrentState == null){
            throw new BatchValidateException("Resource state not found");
        }
        return this.resourceCurrentState;
    }

    /**
     *
     * @return list of current values of checklist items in the BatchResource
     * @throws BatchValidateException
     */
    public String[] getCheckListItemsList() throws BatchValidateException{
        if(this.checkListItemsList == null){
            throw new BatchValidateException("Resource checklist items not found");
        }
        return this.checkListItemsList;
    }

    /**
     *
     * @return list of current values of vote items in the BatchResource
     * @throws BatchValidateException
     */
    public String[] getVoteItemsList() throws BatchValidateException{
        if(this.voteItemsList == null){
            throw new BatchValidateException("Resource vote items not found");
        }
        return this.voteItemsList;
    }

    /**
     *
     * @param resourcePath
     */
    public void setResourcePath(String resourcePath){
        this.resourcePath = resourcePath;
    }

    /**
     * set method for lifecycle name
     * @param resourceLCName
     */
    public void setResourceLCName(String resourceLCName){
        this.resourceLCName = resourceLCName;
    }

    /**
     * set method for lifecycle current state
     * @param resourceCurrentState
     */
    public void setResourceCurrentState(String resourceCurrentState){
        this.resourceCurrentState = resourceCurrentState;
    }

    /**
     * set method for lifecycle checklist items list
     * @param checkListItemsList
     */
    public void setCheckListItemsList(String[] checkListItemsList){
        this.checkListItemsList = checkListItemsList;
    }

    /**
     * set method for lifecycle vote items list
     * @param voteItemsList
     */
    public void setVoteItemsList(String[] voteItemsList){
        this.voteItemsList = voteItemsList;
    }
}
