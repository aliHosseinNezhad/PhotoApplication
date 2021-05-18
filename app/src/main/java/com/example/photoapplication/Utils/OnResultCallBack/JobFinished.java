package com.example.photoapplication.Utils.OnResultCallBack;

import android.os.Bundle;

public abstract class JobFinished {
    public abstract void jobFinished(boolean finished, String... name);
}
