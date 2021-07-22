<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="org.labkey.api.view.ActionURL" %>
<%@ page import="org.labkey.api.view.HttpView" %>
<%@ page import="org.labkey.api.view.JspView" %>
<%@ page import="org.labkey.api.view.template.ClientDependencies" %>
<%@ page import="org.labkey.response.ResponseController.ForwardingSettingsAction" %>
<%@ page import="org.labkey.response.ResponseController.ForwardingSettingsForm" %>
<%@ page import="org.labkey.response.ResponseManager" %>
<%@ page import="org.labkey.response.forwarder.ForwarderProperties" %>
<%@ page import="org.labkey.response.forwarder.ForwardingType" %>
<%@ page import="java.util.Map" %>
<%@ page extends="org.labkey.api.jsp.JspBase" %>
<%@ taglib prefix="labkey" uri="http://www.labkey.org/taglib" %>

<style>
    .lk-study-id {
        width: 300px;
    }

    .lk-response-collection {
        vertical-align: text-bottom;
    }

    .lk-response-collection-buttons {
        margin-top: 20px;
    }

    .lk-response-update-metadata {
        margin-left: 20px;
    }
</style>

<%!
    @Override
    public void addClientDependencies(ClientDependencies dependencies)
    {
        dependencies.add("internal/jQuery");
        dependencies.add("Ext4");
        dependencies.add("mobileAppStudy/panel/forwarderSettings.css");
        dependencies.add("mobileAppStudy/panel/forwarderSettings.js");
    }
%>
<%
    JspView<ForwardingSettingsForm> me = (JspView<ForwardingSettingsForm>) HttpView.currentView();
    ForwardingSettingsForm bean = me.getModelBean();

    ResponseManager manager = ResponseManager.get();

    Map<String, String> forwardingProperties = manager.getForwardingProperties(getContainer());
    ForwardingType authType = ForwarderProperties.getForwardingType(getContainer());

    String basicAuthURL = forwardingProperties.get(ForwarderProperties.URL_PROPERTY_NAME);
    String basicAuthUser = forwardingProperties.get(ForwarderProperties.USER_PROPERTY_NAME);
    String basicAuthPassword = StringUtils.isNotBlank(bean.getPassword()) ?
            ForwarderProperties.PASSWORD_PLACEHOLDER :
            "";

    String oauthRequestURL = forwardingProperties.get(ForwarderProperties.TOKEN_REQUEST_URL);
    String oauthTokenFieldPath = forwardingProperties.get(ForwarderProperties.TOKEN_FIELD);
    String oauthTokenHeader= forwardingProperties.get(ForwarderProperties.TOKEN_HEADER);
    String oauthURL = forwardingProperties.get(ForwarderProperties.OAUTH_URL);
%>

<labkey:panel title="Study Setup">
    Enter the StudyId to be associated with this folder. The StudyId should be the same as it appears in the study design interface. <br/><br/>

    <labkey:input type="text" className="form-control lk-study-id" name="studyId" placeholder="Enter StudyId" value="<%=basicAuthUser%>" /> <br/>
    <labkey:input type="checkbox" id="responseCollection" /> <span class="lk-response-collection"> Enable Response Collection </span>

    <div class="lk-response-collection-buttons">
        <%= button("Submit").submit(true) %>
        <span class="lk-response-update-metadata"> <%= button("Update Metadata").href("asdf") %> </span>
    </div>

</labkey:panel>


<labkey:panel title="Response Forwarding">
    <labkey:errors></labkey:errors>

    <labkey:form name="mobileAppStudyForwardingSettingsForm" action="<%=new ActionURL(ForwardingSettingsAction.class, getContainer())%>" method="POST" >
        <div id="authTypeSelector" class=" form-group" >
            <label>
                <input type="radio" name="forwardingType" value="<%=ForwardingType.Disabled%>"<%=checked(authType == ForwardingType.Disabled)%>/>
                Disabled
            </label><br>
            <label>
                <input type="radio" name="forwardingType" value="<%=ForwardingType.Basic%>"<%=checked(authType == ForwardingType.Basic)%>/>
                Basic Authorization
            </label><br>
            <label>
                <input type="radio" name="forwardingType" value="<%=ForwardingType.OAuth%>"<%=checked(authType == ForwardingType.OAuth)%>/>
                OAuth
            </label><br>
        </div>

        <div style="padding: 10px;" >
            <div id="basicAuthPanel" class=" form-group">
                <labkey:input type="text" className=" form-control lk-forwarder-input" label="User" name="username" value="<%=basicAuthUser%>" />
                <labkey:input type="password" className=" form-control lk-forwarder-input" label="Password" name="password" value="<%=basicAuthPassword%>"/>
                <labkey:input type="text" className=" form-control lk-forwarder-input lk-forwarder-url" label="Endpoint URL" name="basicURL" value="<%=basicAuthURL%>" />
            </div>
            <div id="oauthPanel" class=" form-group">
                <labkey:input type="text" className=" form-control lk-forwarder-input lk-forwarder-url" label="Token Request URL" name="tokenRequestURL" value="<%=oauthRequestURL%>" />
                <labkey:input type="text" className=" form-control lk-forwarder-input" label="Token Field" name="tokenField" value="<%=oauthTokenFieldPath%>"/>
                <labkey:input type="text" className=" form-control lk-forwarder-input" label="Header Name" name="header" value="<%=oauthTokenHeader%>" />
                <labkey:input type="text" className=" form-control lk-forwarder-input lk-forwarder-url" label="Endpoint URL" name="oauthURL" value="<%=oauthURL%>" />
            </div>
        </div>
        <div id="buttonBar">
            <button id="forwarderSubmitButton" type="submit" class="labkey-button primary" >Submit</button>
        </div>
    </labkey:form>
</labkey:panel>
<script type="text/javascript">

    +function ($) {

        $('#authTypeSelector').change(LABKEY.MobileAppForwarderSettings.showAuthPanel);

        LABKEY.MobileAppForwarderSettings.showAuthPanel();
    } (jQuery);
</script>
