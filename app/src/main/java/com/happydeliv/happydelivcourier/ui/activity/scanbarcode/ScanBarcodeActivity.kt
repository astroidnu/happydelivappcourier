package com.happydeliv.happydelivcourier.ui.activity.scanbarcode

import android.graphics.PointF
import android.os.Bundle
import com.happydeliv.happydelivcourier.R
import com.scoproject.newsapp.ui.common.BaseActivity
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_scan_barcode.*
import me.dm7.barcodescanner.zbar.ZBarScannerView
import javax.inject.Inject


/**
 * Created by ibnumuzzakkir on 25/02/18.
 * Android Engineer
 * SCO Project
 */
class ScanBarcodeActivity : BaseActivity(), ScanBarcodeContract.View, QRCodeReaderView.OnQRCodeReadListener, ZBarScannerView.ResultHandler {
    @Inject
    lateinit var mScanBarcodePresenter : ScanBarcodePresenter

    private val qrCodeReaderView by lazy {
        qrdecoderview
    }
    private var mScannerView: ZBarScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mScanBarcodePresenter.attachView(this)
        mScannerView = ZBarScannerView(this)    // Programmatically initialize the scanner view
        setContentView(mScannerView)

    }
    override fun onActivityReady(savedInstanceState: Bundle?) {

//        qrCodeReaderView.setOnQRCodeReadListener(this)
//
//        // Use this function to enable/disable decoding
//        qrCodeReaderView.setQRDecodingEnabled(true)
//
//        // Use this function to change the autofocus interval (default is 5 secs)
//        qrCodeReaderView.setAutofocusInterval(2000L)
//
//        // Use this function to enable/disable Torch
//        qrCodeReaderView.setTorchEnabled(true)
//
//        // Use this function to set front camera preview
//        qrCodeReaderView.setFrontCamera()
//
//        // Use this function to set back camera preview
//        qrCodeReaderView.setBackCamera()
    }


    public override fun onResume() {
        super.onResume()
        mScannerView?.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView?.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView?.stopCamera()           // Stop camera on pause
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_scan_barcode
    }

    override fun onBackPressed() {
        mActivityNavigation.navigateToHomePage()
        super.onBackPressed()
    }
    override fun navigateToHome() {
        onBackPressed()
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        Toast.makeText(this, "Contents = " + text, Toast.LENGTH_SHORT).show()
    }

    override fun handleResult(result: me.dm7.barcodescanner.zbar.Result?) {
        Toast.makeText(this, "Contents = " + result?.contents.toString(), Toast.LENGTH_SHORT).show()
        mScanBarcodePresenter.addTrackingPackage(result?.contents.toString())
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

}