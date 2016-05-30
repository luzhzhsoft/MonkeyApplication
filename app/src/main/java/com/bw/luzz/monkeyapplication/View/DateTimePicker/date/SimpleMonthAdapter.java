/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bw.luzz.monkeyapplication.View.DateTimePicker.date;

import android.content.Context;

import com.bw.luzz.monkeyapplication.View.DateTimePicker.date.*;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.date.MonthView;
import com.bw.luzz.monkeyapplication.View.DateTimePicker.date.SimpleMonthView;

/**
 * An adapter for items.
 */
public class SimpleMonthAdapter extends com.bw.luzz.monkeyapplication.View.DateTimePicker.date.MonthAdapter {

    public SimpleMonthAdapter(Context context, DatePickerController controller) {
        super(context, controller);
    }

    @Override
    public com.bw.luzz.monkeyapplication.View.DateTimePicker.date.MonthView createMonthView(Context context) {
        final MonthView monthView = new SimpleMonthView(context, null, mController);
        return monthView;
    }
}
