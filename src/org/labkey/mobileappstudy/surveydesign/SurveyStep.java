/*
 * Copyright (c) 2017 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.labkey.mobileappstudy.surveydesign;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.JdbcType;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by iansigmon on 2/2/17.
 */
public class SurveyStep
{
    private static final int VARCHAR_TEXT_BOUNDARY = 4000;

    /**
     * Enum describing the various Step Types
     */
    public enum SurveyStepType
    {
        Form("form"),
        Instruction("instruction"),
        Question("question"),
        Task("task"),
        UNKNOWN(null);

        private static final Map<String, SurveyStepType> stepTypeMap;

        static {
            Map<String, SurveyStepType> map = new HashMap<>();
            for (SurveyStepType resultType : values())
                map.put(resultType.designTypeString, resultType);

            stepTypeMap = Collections.unmodifiableMap(map);
        }

        //String representing this in the design json
        private String designTypeString;

        SurveyStepType(String type)
        {
            designTypeString = type;
        }

        static SurveyStepType getStepType(String key)
        {
            return stepTypeMap.getOrDefault(key, UNKNOWN);
        }
    }

    private enum Style
    {
        Integer("Integer", JdbcType.DOUBLE),
        Double("Decimal", JdbcType.DOUBLE),
        Date("Date", JdbcType.TIMESTAMP),
        Date_Time("Date-Time", JdbcType.TIMESTAMP),
        UNKNOWN(null, null);

        private static final Map<String, Style> styleMap;

        static {
            Map<String, Style> map = new HashMap<>();
            for (Style resultType : values())
                map.put(resultType.designStyleString, resultType);

            styleMap = Collections.unmodifiableMap(map);
        }

        //String representing this in the design json
        private String designStyleString;
        private JdbcType propertyType;

        Style(String key, JdbcType type)
        {
            designStyleString = key;
            propertyType = type;
        }

        static Style getStyle(String key)
        {
            return styleMap.getOrDefault(key, UNKNOWN);
        }
        public JdbcType getPropertyType()
        {
            return propertyType;
        }
    }

    /**
     * Enum describing the various possible result types for any given step
     */
    public enum StepResultType
    {
        //TODO: This should be unified with SurveyResult.ValueType
        Scale("scale", (step) -> JdbcType.DOUBLE),
        ContinuousScale("continuousScale", (step) -> JdbcType.DOUBLE),
        TextScale("textScale", (step) -> JdbcType.VARCHAR),
        ValuePicker ("valuePicker", (step) -> JdbcType.VARCHAR),
        ImageChoice ("imageChoice", (step) -> JdbcType.VARCHAR),
        TextChoice ("textChoice", (step) -> JdbcType.VARCHAR),       //Keep type for value field in Choice list
        GroupedResult("grouped", (step) -> null),
        Boolean ("boolean", (step) -> JdbcType.BOOLEAN),
        Numeric ("numeric", (step) -> step.getStyle().getPropertyType()),           //TODO: Per result schema value should always be a double
        TimeOfDay("timeOfDay", (step) -> JdbcType.TIMESTAMP),
        Date("date", (step) -> step.getStyle().getPropertyType()),               //TODO: Per result schema value should always be the same date format
        Text("text", (step) -> JdbcType.VARCHAR),
        Email ("email", (step) -> JdbcType.VARCHAR),
        TimeInterval ("timeInterval", (step) -> JdbcType.DOUBLE),
        Height("height", (step) -> JdbcType.DOUBLE),
        Location("location", (step) -> JdbcType.VARCHAR),

        UNKNOWN("Unknown", (step) -> null);

        private static final Map<String, StepResultType> resultTypeMap;

        //String representing this in the design json
        private String resultTypeString;

        //Corresponding backing property type
        private Function<SurveyStep, JdbcType> resultTypeDelegate;

        static {
            Map<String, StepResultType> map = new HashMap<>();
            for (StepResultType resultType : values())
                map.put(resultType.resultTypeString, resultType);

            resultTypeMap = Collections.unmodifiableMap(map);
        }


        StepResultType(String key, Function<SurveyStep, JdbcType> interpretFormat)
        {
            resultTypeString = key;
            resultTypeDelegate = interpretFormat;
        }

        public static StepResultType getStepResultType(String key)
        {
            return resultTypeMap.getOrDefault(key, UNKNOWN);
        }

        @Nullable
        public JdbcType getPropertyType(SurveyStep step)
        {
            return resultTypeDelegate.apply(step);
        }
    }

    public enum PHIClassification
    {
        PHI("PHI"),
        Limited("Limited"),
        NotPHI("NotPHI");

        private static final Map<String, PHIClassification> CONVERTER;

        private String jsonString;
        static {
            Map<String, PHIClassification> map = new HashMap<>();
            for(PHIClassification val : values())
                map.put(val.jsonString, val);

            CONVERTER = Collections.unmodifiableMap(map);
        }

        PHIClassification(String val)
        {
            jsonString = val;
        }

         public static PHIClassification getClassicication(String key)
         {
             if (StringUtils.isNotBlank(key))
                 return CONVERTER.get(key);
             else
                 //TODO: not sure what default should be, so defaulting to most limited
                 return PHIClassification.PHI;
         }

         public String getJsonValue()
         {
             return jsonString;
         }
    }

    private final static String MAXLENGTH_KEY = "maxLength";
    private final static String STYLE_KEY = "style";

    private String type;
    private String resultType;
    private String key;
    private String phi;
    private String title;
    private List<SurveyStep> steps = null;

    private Map<String, Object> format;

    //Currently ignored since we need to make the field
    private boolean skippable = true;

    //Currently ignored. Only applicable to forms and choice, and those are distinguished by result type
    private boolean repeatable = false;

    public boolean getRepeatable()
    {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable)
    {
        this.repeatable = repeatable;
    }

    public boolean isSkippable()
    {
        return skippable;
    }

    public void setSkippable(boolean skippable)
    {
        this.skippable = skippable;
    }

    public String getType()
    {
        return type;
    }

    public String getPhi()
    {
        return phi;
    }

    public void setPhi(String phi)
    {
        this.phi = phi;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setSteps(List<SurveyStep> steps)
    {
        this.steps = steps;
    }

    public void setFormat(Map<String, Object> format)
    {
        this.format = format;
    }

    public boolean isRepeatable()
    {
        return repeatable;
    }

    @JsonIgnore
    public SurveyStepType getStepType()
    {
        return SurveyStepType.getStepType(type);
    }

    public String getKey()
    {
        return key;
    }

    void setKey(String key)
    {
        this.key = key;
    }

    public String getResultType()
    {
        return this.resultType;
    }

    void setResultType(String resultType)
    {
        this.resultType = resultType;
    }

    void setType(String type)
    {
        this.type = type;
    }

    public Map<String, Object> getFormat()
    {
        return format;
    }

    public void setPhiClassification(PHIClassification classification)
    {
        this.phi = classification.getJsonValue();
    }

    @JsonIgnore
    public PHIClassification getPHIClassification()
    {
        return PHIClassification.getClassicication(phi);
    }

    public String getTitle()
    {
        return title;
    }

    @JsonIgnore
    @Nullable
    public Integer getMaxLength()
    {
        if (getFormat() == null || getFormat().get(MAXLENGTH_KEY) == null)
            return null;

        String val = String.valueOf(getFormat().get(MAXLENGTH_KEY));
        if (StringUtils.isBlank(val))
            return null;

        //Json MaxLength = 0 indicates Max text size
        Integer intVal = Integer.valueOf(val);
        return intVal == 0 || intVal > VARCHAR_TEXT_BOUNDARY ? Integer.MAX_VALUE : intVal;
    }

    @Nullable
    public List<SurveyStep> getSteps()
    {
        return steps;
    }

    @JsonIgnore
    public Style getStyle()
    {
        if (getFormat() == null)
            return null;

        String val = String.valueOf(getFormat().get(STYLE_KEY));
        if (StringUtils.isBlank(val))
            return null;

        return Style.getStyle(val);
    }

    @JsonIgnore
    public JdbcType getPropertyType()
    {
        return StepResultType.getStepResultType(getResultType()).getPropertyType(this);
    }
}
