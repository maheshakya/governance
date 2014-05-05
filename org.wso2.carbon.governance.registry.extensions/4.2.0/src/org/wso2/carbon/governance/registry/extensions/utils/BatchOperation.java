package org.wso2.carbon.governance.registry.extensions.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.governance.api.aggregate.BatchResourceBean;
import org.wso2.carbon.governance.api.exception.GovernanceException;
import org.wso2.carbon.governance.registry.extensions.internal.GovernanceRegistryExtensionsComponent;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by maheshakya on 4/29/14.
 */
public class BatchOperation {

    private static final Log log = LogFactory.getLog(BatchOperation.class);
    private Registry registry;


    private final String REGISTRY_CUSTOM_LIFECYCLE_CHECKLIST = "registry.custom_lifecycle.checklist.option.";
    private final String REGISTRY_LC_NAME = "registry.LC.name";

    public BatchOperation(){
        int tenantId = PrivilegedCarbonContext.getCurrentContext().getTenantId();
        String userName = PrivilegedCarbonContext.getCurrentContext().getUsername();
        try{
            this.registry = GovernanceRegistryExtensionsComponent.getRegistryService().getRegistry(userName, tenantId);
        }catch(RegistryException e){
            log.error("Failed to get registry.", e);
        }
    }


    public boolean invokeBatchCheckItem(String[] paths, Map<String,String> parameterMap)throws GovernanceException{
        String action = "itemClick";
        boolean success = false;

        if (parameterMap == null){
            throw new GovernanceException("paramterMap cannot be null");
        }

        try {
            String aspect = null;
            String[] checkItemsList = null;

            registry.beginTransaction();

            log.info("Transaction began.");

            for(int i=0; i<paths.length; i++){
                checkItemsList = getCheckListItemsList(registry.get(paths[i]));
                if(checkItemsList == null || checkItemsList.length != parameterMap.size()){
                    throw new GovernanceException("*Incompatible parameters or something*");
                }
                aspect = getResourceLCName(registry.get(paths[i]));

                if(aspect == null){
                    throw new GovernanceException("Lifecycle not found in resource:" + paths[i]);
                }

                for(int j=0; j<parameterMap.size(); j++){
                    if(!parameterMap.get(j + ".item").equals(checkItemsList[j])){
                        registry.invokeAspect(paths[i], aspect, action, parameterMap);

                    }
                }
            }
            success = true;
        } catch(GovernanceException e){
            String message = "GovernanceException";
            throw new GovernanceException(message, e);
        } catch(RegistryException e){
            String message = "RegistryException";
            throw new GovernanceException(message, e);
        }finally {
            if(success){
                try{
                    registry.commitTransaction();
                    log.info("Item check successful.");

                } catch(RegistryException e) {
                    throw new GovernanceException(e);
                }
            }
            else{
                try{
                    registry.rollbackTransaction();
                    log.error("Item check Failed.");
                } catch (RegistryException e){
                    throw new GovernanceException(e);
                }
            }
        }
        return success;

    }

    public boolean invokeBatchStateTransition(String paths[], String action) throws GovernanceException{
        boolean success = false;

        if (action == null){
            throw new GovernanceException("action cannot be null.");
        }

        try {
            String aspect = null;
            String[] checkItemsList = null;
            Map<String, String> parameterMap = new HashMap<String, String>();

            registry.beginTransaction();

            log.info("Transaction began.");

            for(int i=0; i<paths.length; i++){
                checkItemsList = getCheckListItemsList(registry.get(paths[i]));
                if(checkItemsList == null){
                    throw new GovernanceException("Check items not found in resource:" + paths[i]);
                }

                for(int j=0; j<checkItemsList.length; j++){
                    parameterMap.put(j+".item", checkItemsList[j]);
                }

                aspect = getResourceLCName(registry.get(paths[i]));
                if(aspect == null){
                    throw new GovernanceException("Lifecycle not found in resource:" + paths[i]);
                }
                registry.invokeAspect(paths[i], aspect, action, parameterMap);
            }
            success = true;
        } catch(GovernanceException e){
            String message = "GovernanceException";
            throw new GovernanceException(message, e);
        } catch(RegistryException e){
            String message = "RegistryException";
            throw new GovernanceException(message, e);
        }finally {
            if(success){
                try{
                    registry.commitTransaction();
                    log.info("Item check successful.");

                } catch(RegistryException e) {
                    throw new GovernanceException(e);
                }
            }
            else{
                try{
                    registry.rollbackTransaction();
                    log.error("Item check Failed.");
                } catch (RegistryException e){
                    throw new GovernanceException(e);
                }
            }
        }


        return success;
    }



    private String[] getCheckListItemsList(Resource resource){
        String[] checkListItemsList = null;

        Enumeration en = resource.getProperties().propertyNames();

        String str;
        int checkItems = 0;
        while(en.hasMoreElements()){
            str = (String) en.nextElement();
            if (str.startsWith(REGISTRY_CUSTOM_LIFECYCLE_CHECKLIST)){
                checkItems++;
            }
        }
        checkItems = checkItems/2;
        if(checkItems>0){
            checkListItemsList = new String[checkItems];
            String ITEM_VALUE;

            for(int i=0;i<checkItems;i++){
                ITEM_VALUE=resource.getPropertyValues(REGISTRY_CUSTOM_LIFECYCLE_CHECKLIST +i +".item").get(3);
                checkListItemsList[i] = ITEM_VALUE.substring(6);
            }
        }
        return checkListItemsList;

    }

    private String getResourceLCName(Resource resource){
        String resourceLCName = resource.getProperty(REGISTRY_LC_NAME);

        return resourceLCName;
    }
}
