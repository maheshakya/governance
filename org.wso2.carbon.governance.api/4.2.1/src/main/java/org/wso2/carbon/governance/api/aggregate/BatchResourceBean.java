package org.wso2.carbon.governance.api.aggregate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.registry.core.Resource;

import java.util.Enumeration;
import java.util.List;

/**
 * This defines the structure of a batch of assets.
 */
public class BatchResourceBean {

    private static final Log log = LogFactory.getLog(BatchResourceBean.class);
    //private Resource resource;
    private String resourcePath;
    private String resourceCurrentState;
    private String resourceLCName;
    private String[] checkListItemsList;
    private String[] voteItemsList;

    private final String REGISTRY_LC_NAME = "registry.LC.name";
    private final String REGISTRY_LIFECYCLE = "registry.lifecycle.";
    private final String REGISTRY_CUSTOM_LIFECYCLE_CHECKLIST = "registry.custom_lifecycle.checklist.option.";
    private final String REGISTRY_CUSTOM_LIFECYCLE_VOTE = "registry.custom_lifecycle.votes.option." ;
    private final String REGISTRY_ASPECTS = "registry.Aspects";
    /**
     * Constructor accepting resource and creating properties.
     *
     * @param resource the instance of the registry.
     * @throws org.wso2.carbon.governance.api.exception.GovernanceException
     *
     */
    public BatchResourceBean(Resource resource) throws GovernanceException{

        if (resource == null){
            throw new GovernanceException("*No resource found.*");
        }
        //this.resource = resource;

        if (resource.getPath() == null){
            throw new GovernanceException("*No Path*");
        }
        resourcePath = resource.getPath();

        if (resource.getProperty(REGISTRY_LC_NAME) == null){
            throw new GovernanceException("*No life cycles found or something*");
        }
        resourceLCName = resource.getProperty(REGISTRY_LC_NAME);
        String stateProperty = REGISTRY_LIFECYCLE + resourceLCName + ".state";

        if (resource.getProperty(stateProperty) == null){
            throw new GovernanceException("*State is null or something*");
        }
        resourceCurrentState = resource.getProperty(stateProperty);

        System.out.println(resourcePath);

        Enumeration en = resource.getProperties().propertyNames();

        String str;
        int votes = 0;
        int checkItems = 0;
        while(en.hasMoreElements()){
            str = (String) en.nextElement();
            if (str.startsWith(REGISTRY_CUSTOM_LIFECYCLE_VOTE)){
                votes++;
            }
            if (str.startsWith(REGISTRY_CUSTOM_LIFECYCLE_CHECKLIST)){
                checkItems++;
            }
        }
        votes = votes/2;
        checkItems = checkItems/2;

        System.out.println("votes: " + votes);
        System.out.println("checkItems: " + checkItems);


        if(votes>0){
            voteItemsList = new String[votes];
            String VOTE_VALUE;

            for(int i=0;i<votes;i++){
                VOTE_VALUE = resource.getPropertyValues(REGISTRY_CUSTOM_LIFECYCLE_VOTE +i +".vote").get(4);
                voteItemsList[i] = VOTE_VALUE.substring(8);
            }
        }

        if(checkItems>0){
            checkListItemsList = new String[checkItems];
            String ITEM_VALUE;

            for(int i=0;i<checkItems;i++){
                ITEM_VALUE=resource.getPropertyValues(REGISTRY_CUSTOM_LIFECYCLE_CHECKLIST +i +".item").get(3);
                checkListItemsList[i] = ITEM_VALUE.substring(6);
            }
        }

        //System.out.println("voteItemslist: " + voteItemsList.toString());
        System.out.println("checkListItemsList: " + checkListItemsList.toString());
        for (int i = 0; i<checkListItemsList.length; i++){
            System.out.println(checkListItemsList[i]);
        }
        System.out.println();
    }

    /**
     *
     * @return resource ath of the BatchResource
     * @throws GovernanceException
     */
    public String getResourcePath() throws GovernanceException{
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




}
