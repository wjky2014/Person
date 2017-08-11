/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package com.android.settings.applications;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.android.settings.R;
import com.android.settings.dashboard.SummaryLoader;
import com.android.settings.notification.NotificationBackend;
import com.android.settings.search.Indexable;

import android.content.res.Resources;
import android.os.UserHandle;
import android.util.Log;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.SearchIndexableRaw;
import com.android.settings.search.Indexable.SearchIndexProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of ManageApplications with no changes other than having its own
 * SummaryProvider.
 */
public class NotificationApps extends ManageApplications implements Indexable {

    private static class SummaryProvider implements SummaryLoader.SummaryProvider {

        private final Context mContext;
        private final SummaryLoader mLoader;
        private final NotificationBackend mNotificationBackend;

        private SummaryProvider(Context context, SummaryLoader loader) {
            mContext = context;
            mLoader = loader;
            mNotificationBackend = new NotificationBackend();
        }

        @Override
        public void setListening(boolean listening) {
            if (listening) {
                new AppCounter(mContext) {
                    @Override
                    protected void onCountComplete(int num) {
                        updateSummary(num);
                    }

                    @Override
                    protected boolean includeInCount(ApplicationInfo info) {
                        return mNotificationBackend.getNotificationsBanned(info.packageName,
                                info.uid);
                    }
                }.execute();
            }
        }

        private void updateSummary(int count) {
            if (count == 0) {
                mLoader.setSummary(this, mContext.getString(R.string.notification_summary_none));
            } else {
                mLoader.setSummary(this, mContext.getResources().getQuantityString(
                        R.plurals.notification_summary, count, count));
            }
        }
    }

    public static final SummaryLoader.SummaryProviderFactory SUMMARY_PROVIDER_FACTORY
            = new SummaryLoader.SummaryProviderFactory() {
        @Override
        public SummaryLoader.SummaryProvider createSummaryProvider(Activity activity,
                                                                   SummaryLoader summaryLoader) {
            return new SummaryProvider(activity, summaryLoader);
        }
    };

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {

        @Override
        public List<SearchIndexableRaw> getRawDataToIndex(Context context, boolean enabled) {
            final List<SearchIndexableRaw> result = new ArrayList<SearchIndexableRaw>();
            if (UserHandle.myUserId() != UserHandle.USER_OWNER) {
                return result;
            }
            Log.i("NotificationApps", "add NotificationApps getRawDataToIndex");
            SearchIndexableRaw data = new SearchIndexableRaw(context);
            final Resources res = context.getResources();
            final String screenTitle = res.getString(R.string.app_notifications_title);
            data.title = screenTitle;
            data.screenTitle = screenTitle;
            result.add(data);
            return result;
        }
    };
}
