package org.labkey.response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.labkey.api.data.ContainerManager;
import org.labkey.api.data.DeferredUpgrade;
import org.labkey.api.data.PropertyManager;
import org.labkey.api.data.UpgradeCode;
import org.labkey.api.module.Module;
import org.labkey.api.module.ModuleContext;
import org.labkey.api.module.ModuleLoader;
import org.labkey.api.module.ModuleProperty;

import java.util.HashMap;
import java.util.Map;

import static org.labkey.response.ResponseController.ServerConfigurationAction.RESPONSE_SERVER_CONFIGURATION;
import static org.labkey.response.ResponseController.ServerConfigurationAction.WCP_SERVER;
import static org.labkey.response.ResponseModule.SURVEY_METADATA_DIRECTORY;

public class ResponseUpgradeCode implements UpgradeCode
{
    private static final Logger LOG = LogManager.getLogger(ResponseUpgradeCode.class);

    /**
     * Invoked within mobileappstudy-20.001-20.002.sql
     *
     */
    @DeferredUpgrade
    public static void migrateResponseServerConfig(ModuleContext context)
    {
        Module m = ModuleLoader.getInstance().getModule("Response");
        if (m == null)
            return;

        // TODO: modularize into helper function for each property?
        String metaDataDirectoryValue = "";
        ModuleProperty mp = m.getModuleProperties().get(SURVEY_METADATA_DIRECTORY);
        if (mp != null)
        {
            // maybe go down into the DB, obtain them that way?
            String siteDefaultValue = mp.getValueContainerSpecific(ContainerManager.getRoot());
            String projectLevelValue = mp.getEffectiveValue(ContainerManager.getRoot()); // todo
            String containerLevelValue = mp.getValueContainerSpecific(ContainerManager.getRoot());

            // Use site default values if existent, and fall back of the project-level then container-level values
            metaDataDirectoryValue = (siteDefaultValue != null) ? siteDefaultValue : (projectLevelValue != null) ? projectLevelValue : containerLevelValue;

            if (siteDefaultValue != null && projectLevelValue != null)
                LOG.warn(String.format("The project-level value '%s' for the Response module property %s is being deprecated in favor of the Site Default '%s'", projectLevelValue, SURVEY_METADATA_DIRECTORY, siteDefaultValue));

            if (siteDefaultValue != null && containerLevelValue != null)
                LOG.warn(String.format("The container-level value '%s' for the Response module property %s is being deprecated in favor of the Site Default '%s'", containerLevelValue, SURVEY_METADATA_DIRECTORY, siteDefaultValue));
            else if (projectLevelValue != null && containerLevelValue != null)
                LOG.warn(String.format("The container-level value '%s' for the Response module property %s is being deprecated in favor of the project-level value '%s'", containerLevelValue, SURVEY_METADATA_DIRECTORY, projectLevelValue));
        }



//        ModuleProperty mp1 = m.getModuleProperties().get("MetadataServiceBaseUrl");
//        ModuleProperty mp2 = m.getModuleProperties().get("SurveyMetadataDirectory");




        PropertyManager.PropertyMap props = PropertyManager.getEncryptedStore().getWritableProperties(ContainerManager.getRoot(), RESPONSE_SERVER_CONFIGURATION, false);
        if (props != null)
        {
            Map<String, String> valueMap = new HashMap<>();
            valueMap.put(ResponseController.ServerConfigurationAction.METADATA_LOAD_LOCATION, WCP_SERVER);
            valueMap.put(ResponseController.ServerConfigurationAction.METADATA_DIRECTORY, "");
            valueMap.put(ResponseController.ServerConfigurationAction.WCP_BASE_URL, "");
            valueMap.put(ResponseController.ServerConfigurationAction.WCP_USERNAME, "");
            valueMap.put(ResponseController.ServerConfigurationAction.WCP_PASSWORD, "");

            props.putAll(valueMap);
            props.save();
        }
    }
}
