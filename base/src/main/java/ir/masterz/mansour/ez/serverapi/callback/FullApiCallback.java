package ir.masterz.mansour.ez.serverapi.callback;

import ir.masterz.mansour.ez.serverapi.callback.basic.ErrorCallback;
import ir.masterz.mansour.ez.serverapi.callback.basic.FailureCallback;
import ir.masterz.mansour.ez.serverapi.callback.basic.ResponseCallback;
import ir.masterz.mansour.ez.serverapi.callback.basic.SuccessCallback;


/**
 * Created by Sora on 11/8/2016.
 */

@Deprecated
public interface FullApiCallback extends SuccessCallback, ErrorCallback, ResponseCallback, FailureCallback {

}
