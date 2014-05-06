package org.wso2.carbon.governance.api.util;

import org.wso2.carbon.governance.api.exception.GovernanceException;

/**
 * Created by maheshakya on 5/5/14.
 */
public class BatchResourceBean {

    private String resourcePath;
    private String resourceLCName;
    private String resourceCurrentState;
    private String[] checkListItemsList;
    private String[] voteItemsList;

    /**
     *
     * @return resource ath of the BatchResource
     * @throws GovernanceException
     */
    public String getResourcePath() throws GovernanceException {
        if(this.resourcePath == null){
            throw new GovernanceException("*Resource path not found.*");
        }
        return this.resourcePath;
    }

    /**
     *
     * @return resource life cycle of the BatchResource
     * @throws GovernanceException
     */
    public String getResourceLCName() throws GovernanceException{
        if(this.resourceLCName == null){
            throw new GovernanceException("*Resource Life cycle not found.*");
        }
        return this.resourceLCName;
    }

    /**
     *
     * @return current state of the BatchResource
     * @throws GovernanceException
     */
    public String getResourceCurrentState() throws GovernanceException{
        if(this.resourceCurrentState == null){
            throw new GovernanceException("*Resource state not found.*");
        }
        return this.resourceCurrentState;
    }

    /**
     *
     * @return list of current values of checklist items in the BatchResource
     * @throws GovernanceException
     */
    public String[] getCheckListItemsList() throws GovernanceException{
        if(this.checkListItemsList == null){
            throw new GovernanceException("*Resource checklist items not found.*");
        }
        return this.checkListItemsList;
    }

    /**
     *
     * @return list of current values of vote items in the BatchResource
     * @throws GovernanceException
     */
    public String[] getVoteItemsList() throws GovernanceException{
        if(this.voteItemsList == null){
            throw new GovernanceException("*Resource vote items not found.*");
        }
        return this.voteItemsList;
    }

    public void setResourcePath(String resourcePath){
        this.resourcePath = resourcePath;
    }

    public void setResourceLCName(String resourceLCName){
        this.resourceLCName = resourceLCName;
    }

    public void setResourceCurrentState(String resourceCurrentState){
        this.resourceCurrentState = resourceCurrentState;
    }

    public void setCheckListItemsList(String[] checkListItemsList){
        this.checkListItemsList = checkListItemsList;
    }

    public void setVoteItemsList(String[] voteItemsList){
        this.voteItemsList = voteItemsList;
    }
}
