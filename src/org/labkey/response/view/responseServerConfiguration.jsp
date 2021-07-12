<%@ page import="org.labkey.api.admin.AdminUrls" %>
<%@ page import="org.labkey.api.data.Container" %>
<%@ page import="org.labkey.api.data.PropertyManager" %>
<%@ page import="org.labkey.api.security.permissions.AdminOperationsPermission" %>
<%@ page import="org.labkey.response.ResponseController" %>
<%@ page import="org.labkey.api.view.HttpView" %>
<%@ page import="org.labkey.api.view.JspView" %>
<%@ page import="org.labkey.api.view.ActionURL" %>
<%@ page
        import="static org.labkey.response.ResponseController.ServerConfigurationAction.RESPONSE_SERVER_CONFIGURATION" %>
<%@ page extends="org.labkey.api.jsp.JspBase" %>
<%@ taglib prefix="labkey" uri="http://www.labkey.org/taglib" %>

<style>
    .input-disabled {
        color: grey;
    }

    .response-server-text-input {
        width: 400px;
    }

    input:disabled {
        background-color: rgba(239, 239, 239, 0.3);
        border-color: rgba(118, 118, 118, 0.3);
        color: rgb(170, 170, 170);
    }

    .response-server-radio-section {
        margin-bottom: 15px;
    }

    .response-server-input-row {
        display: flex;
        width: 575px;
        margin-left: 40px;
        margin-top: 15px;
    }

    .response-server-input-row div:last-child {
        margin-left: auto;
    }

    .response-server-navigation-buttons {
        margin-top: 40px;
    }

    .response-server-cancel-button {
        float: left;
    }

    .response-server-save-button {
        margin-left: 503px;
    }
</style>

<%
    JspView<ResponseController.ServerConfigurationForm> me = (JspView<ResponseController.ServerConfigurationForm>) HttpView.currentView();
    ResponseController.ServerConfigurationForm bean = me.getModelBean();

    PropertyManager.PropertyMap props = PropertyManager.getProperties(getContainer(), RESPONSE_SERVER_CONFIGURATION);
    String metadataLoadLocation = props.get("metadataLoadLocation");
    String metadataDirectory = props.get("metadataDirectory");
    String wcpBaseURL = props.get("wcpBaseURL");
    String wcpAccessToken = props.get("wcpAccessToken");


    String id = "d";
    String value = "";
    String metadataDirectoryHelpText = "The directory on the server that holds the survey design metadata files. For use in testing or when the metadata service is not available.";
    String wcpBaseURLHelpText = "The base URL for the Activity Metadata service. Should be an absolute URL that ends with /StudyMetaData; see example below. Note that this URL must NOT end with a question mark.";
    String wcpAccessTokenHelpText = "App token to be passed in request headers to the Activity Metadata Service to identify this client.";

    boolean disabled = true;
%>


<labkey:errors/>

<labkey:form name="ServerConfigurationForm" action="<%=new ActionURL(ResponseController.ServerConfigurationAction.class, getContainer())%>" method="POST">

    <div class="response-server-radio-section">
        <labkey:radio  name="metadataLoadLocation" value="file" currentValue="<%=metadataLoadLocation%>" /> Load metadata from files (used for local testing) <br>
        <div class="response-server-input-row">
            <label class="response-server-text-label response-server-metadata-directory"> Metadata Directory <%=helpPopup("Metadata Directory", metadataDirectoryHelpText, true, 300)%> </label>
            <labkey:input type="text" className="response-server-text-input" name="metadataDirectory" id="metadataDirectory" value="<%=metadataDirectory%>" />
        </div>
    </div>


    <div class="response-server-radio-section">
        <labkey:radio  name="metadataLoadLocation" value="wcpServer" currentValue="<%=metadataLoadLocation%>" /> Load metadata from WCP server (used for deployments) <br>
        <div class="response-server-input-row">
            <label class="response-server-text-label response-server-wcp-base-url"> WCP Base URL <%=helpPopup("WCP Base URL", wcpBaseURLHelpText, true, 300)%> </label>
            <labkey:input type="text" className="response-server-text-input" name="wcpBaseURL" id="wcpBaseURL" value="<%=wcpBaseURL%>" />
        </div>

        <div class="response-server-input-row">
            <label class="control-label response-server-wcp-base-url"> WCP Access Token <%=helpPopup("WCP Access Token", wcpAccessTokenHelpText, true, 300)%> </label>
            <labkey:input type="text" className="response-server-text-input" name="wcpAccessToken" id="wcpAccessToken" value="<%=wcpAccessToken%>" />
        </div>
    </div>

    <div class="response-server-navigation-buttons">
        <div class="response-server-cancel-button"> <%= button("Cancel").href(urlProvider(AdminUrls.class).getAdminConsoleURL()) %> </div>
        <div class="response-server-save-button"> <%= button("Save and Finish").submit(true) %> </div>
    </div>

</labkey:form>


<script type="text/javascript">
    +function ($) {
        const toggleTextInputDisabled = (toggle) => {
            $('#metadataDirectory').prop('disabled', !toggle);
            $('#wcpBaseURL').prop('disabled', toggle);
            $('#wcpAccessToken').prop('disabled', toggle);
        };

        const toggleDisabledBasedOnSelection = () => {
            const selected = $("input[name='metadataLoadLocation']:checked").val();
            if (selected === "file") {
                $('.response-server-wcp-base-url').addClass('input-disabled');
                $('.response-server-metadata-directory').removeClass('input-disabled');
                toggleTextInputDisabled(true);
            } else if (selected === "wcpServer") {
                $('.response-server-metadata-directory').addClass('input-disabled');
                $('.response-server-wcp-base-url').removeClass('input-disabled');
                toggleTextInputDisabled(false);
            }
        };

        $('.response-server-radio-section').change(() => { toggleDisabledBasedOnSelection(); });
        toggleDisabledBasedOnSelection();

    } (jQuery);
</script>