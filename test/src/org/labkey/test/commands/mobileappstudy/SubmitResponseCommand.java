package org.labkey.test.commands.mobileappstudy;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.labkey.test.WebTestHelper;
import org.labkey.test.data.mobileappstudy.Survey;

import java.util.function.Consumer;

public class SubmitResponseCommand extends MobileAppCommand
{
    public static final String  SURVEYINFO_MISSING_MESSAGE = "SurveyInfo not found.",
                                SURVEYID_MISSING_MESSAGE = "SurveyId not included in request",
                                SURVEYVERSION_MISSING_MESSAGE = "SurveyVersion not included in request.",
                                RESPONSE_MISSING_MESSAGE = "Response not included in request.",
                                PARTICIPANTID_MISSING_MESSAGE = "ParticipantId not included in request.",
                                NO_PARTICIPANT_MESSAGE = "Unable to identify participant.",
                                NO_STUDY_MESSAGE = "AppToken not associated with study",
                                SURVEY_NOT_FOUND_MESSAGE = "Survey not found.",
                                COLLECTION_DISABLED_MESSAGE_FORMAT = "Response collection is not currently enabled for study [ %1s ].";

    public static final String ACTION_NAME = "ProcessResponse";

    private final static String BODY_JSON_FORMAT = "{ \n" +
            "  \"type\": \"SurveyResponse\", \n" +
            "%1s" +
            "   \"participantId\": \"%2s\", \n" +
            "   \"response\": %3s\n" +
            "}";

    public final static String MISSING_RESPONSE_JSON_FORMAT = "{ \n" +
            "  \"type\": \"SurveyResponse\", \n" +
            "  \"surveyInfo\": { \n" +
            "      \"surveyId\": \"%1s\", \n" +
            "      \"version\": \"%2s\" \n" +
            "   }, \n" +
            "   \"participantId\": \"%3s\"\n" +
            "}";

    private final static String SURVEY_INFO_FORMAT = "  \"surveyInfo\": { \n" +
            "      \"surveyId\": \"%1s\", \n" +
            "      \"version\": \"%2s\" \n" +
            "   }, \n";

    private String body;
    private boolean _logRequest = false;
    private String targetUrl = WebTestHelper.buildURL(CONTROLLER_NAME, ACTION_NAME);

    public SubmitResponseCommand(Consumer<String> logger)
    {
        setLogger(logger);
        setBody("");
    }

    public SubmitResponseCommand(Consumer<String> logger, String surveyId, String version, String appToken, String surveyResponses)
    {
        setLogger(logger);
        if (StringUtils.isNotBlank(surveyId) || StringUtils.isNotBlank(version))
        {
            String surveyInfo = String.format(SURVEY_INFO_FORMAT, StringUtils.defaultString(surveyId, ""), StringUtils.defaultString(version,""));
            setBody(String.format(BODY_JSON_FORMAT, surveyInfo, appToken, surveyResponses));
        }
        else
            setBody(String.format(BODY_JSON_FORMAT, "", appToken, surveyResponses));

    }

    public SubmitResponseCommand(Consumer<String> logger, Survey survey)
    {
        this(logger, survey.getSurveyId(), survey.getVersion(), survey.getAppToken(), survey.getResponseJson());
    }

    public boolean getLogRequest()
    {
        return _logRequest;
    }

    public void setLogRequest(boolean logRequest)
    {
        _logRequest = logRequest;
    }

    @Override
    public HttpResponse execute(int expectedStatusCode)
    {
        HttpPost post = new HttpPost(getTargetURL());
        if (StringUtils.isNotBlank(getBody()))
            post.setEntity(new StringEntity(getBody(), ContentType.APPLICATION_JSON));

        if (getLogRequest())
            log("Request body:\n\n" + getBody() + "\n\n");

        log("Posting response to LabKey");
        return execute(post, expectedStatusCode);
    }

    @Override
    public String getTargetURL()
    {
        return targetUrl;
    }

    public String changeProjectTarget(String projectName)
    {
        targetUrl = WebTestHelper.buildURL(CONTROLLER_NAME, projectName, ACTION_NAME);
        return targetUrl;
    }


    public void setBody(String body)
    {
        this.body = body;
    }

    public String getBody()
    {
        return this.body;
    }
}
