package com.myx.feng.jobsevice;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;

/**
 * Created by mayuxin on 2018/4/23.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TService  extends JobService {
    @Override
    public boolean onStartJob(JobParameters params) {
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
