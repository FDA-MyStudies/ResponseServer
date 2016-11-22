/*
 * Copyright (c) 2016 LabKey Corporation
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
package org.labkey.mobileappstudy.query;

import org.jetbrains.annotations.Nullable;
import org.labkey.api.data.ActionButton;
import org.labkey.api.data.ButtonBar;
import org.labkey.api.data.Container;
import org.labkey.api.data.DbSchema;
import org.labkey.api.data.DbSchemaType;
import org.labkey.api.data.EnumTableInfo;
import org.labkey.api.data.TableInfo;
import org.labkey.api.module.Module;
import org.labkey.api.query.DefaultSchema;
import org.labkey.api.query.QuerySchema;
import org.labkey.api.query.QuerySettings;
import org.labkey.api.query.QueryView;
import org.labkey.api.query.SimpleUserSchema;
import org.labkey.api.security.User;
import org.labkey.api.view.DataView;
import org.labkey.api.view.ViewContext;
import org.labkey.mobileappstudy.MobileAppStudyController;
import org.labkey.mobileappstudy.MobileAppStudyModule;
import org.labkey.mobileappstudy.data.SurveyResponse;
import org.springframework.validation.BindException;

import static org.labkey.mobileappstudy.MobileAppStudySchema.ENROLLMENT_TOKEN_BATCH_TABLE;
import static org.labkey.mobileappstudy.MobileAppStudySchema.ENROLLMENT_TOKEN_TABLE;
import static org.labkey.mobileappstudy.MobileAppStudySchema.RESPONSE_STATUS_TABLE;
import static org.labkey.mobileappstudy.MobileAppStudySchema.RESPONSE_TABLE;

/**
 * Created by susanh on 10/10/16.
 */
public class MobileAppStudyQuerySchema extends SimpleUserSchema
{
    public static final String NAME = "mobileappstudy";
    public static final String DESCRIPTION = "Provides data about study enrollment and survey responses";

    public MobileAppStudyQuerySchema(User user, Container container)
    {
        super(NAME, DESCRIPTION, user, container, DbSchema.get(NAME, DbSchemaType.Module));
    }

    public static void register(final MobileAppStudyModule module)
    {
        DefaultSchema.registerProvider(NAME, new DefaultSchema.SchemaProvider(module)
        {
            @Override
            public QuerySchema createSchema(DefaultSchema schema, Module module)
            {
                return new MobileAppStudyQuerySchema(schema.getUser(), schema.getContainer());
            }
        });
    }

    @Nullable
    @Override
    protected TableInfo createTable(String name)
    {
        if (ENROLLMENT_TOKEN_BATCH_TABLE.equalsIgnoreCase(name))
        {
            return new EnrollmentTokenBatchTable(this);
        }
        else if (ENROLLMENT_TOKEN_TABLE.equalsIgnoreCase(name))
        {
            return new EnrollmentTokenTable(this);
        }
        else if(RESPONSE_STATUS_TABLE.equalsIgnoreCase(name))
        {
            return new EnumTableInfo<>(
                SurveyResponse.ResponseStatus.class,
                this,
                SurveyResponse.ResponseStatus::getPkId,
                true,
                "Possible states a SurveyResponse might be in"
            );
        }
        else
            return super.createTable(name);
    }

    @Override
    public QueryView createView(ViewContext context, QuerySettings settings, BindException errors)
    {
        if (RESPONSE_TABLE.equalsIgnoreCase(settings.getQueryName()))
        {
            return new QueryView (this, settings, errors) {
                @Override
                protected void populateButtonBar(DataView view, ButtonBar bar)
                {
                    super.populateButtonBar(view, bar);
                    ActionButton button = new ActionButton(MobileAppStudyController.ReprocessResponseAction.class, "Reprocess");
                    bar.add(button);
                }
            };
        }

        return super.createView(context, settings, errors);
    }
}
