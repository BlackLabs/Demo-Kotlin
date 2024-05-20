package mx.pagandocheck.pagandodemokotlin.controllers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import mx.pagandocheck.pagandodemokotlin.CheckServices
import mx.pagandocheck.pagandodemokotlin.R

class MainActivity2 : AppCompatActivity() {
    private var btnLoginApi: Button? = null
    private var btnLogOut: Button? = null
    private var btnGetOrganizations: Button? = null
    private var btnChangeOrganization: Button? = null
    private var btnOrganizationTransaction: Button? = null
    private var btnGetOperations: Button? = null
    private var btnSetSignature: Button? = null
    private var btnPrintBitmap: Button? = null
    private var btnPrintStr: Button? = null
    private var btnGetPayLaterPromotions: Button? = null
    private var btnGetPromotions: Button? = null
    private var btnTransactionByFolio: Button? = null
    private var btnGetPendingCheckouts: Button? = null
    private var btnDoCheckout: Button? = null
    private var btnReadCard: Button? = null
    private var btnInitKeys: Button? = null
    private var btnTerminalClosure: Button? = null
    private var btnPreTerminalClosure: Button? = null
    private var btnGenerateTerminalClosure: Button? = null
    private var btnTerminalClosureByID: Button? = null
    private var btnGetTokenStatus: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        initializeViews()
        addFunctions()
        init()
    }

    private fun init() {
        val checkServices = CheckServices.getInstance(this)
    }

    private fun initializeViews() {
        btnLoginApi = findViewById(R.id.btnLoginApi)
        btnLogOut = findViewById(R.id.btnLogOut)
        btnGetOrganizations = findViewById(R.id.btnGetOrganizations)
        btnChangeOrganization = findViewById(R.id.btnChangeOrganization)
        btnOrganizationTransaction = findViewById(R.id.btnOrganizationTransaction)
        btnGetOperations = findViewById(R.id.btnGetOperations)
        btnSetSignature = findViewById(R.id.btnSetSignature)
        btnPrintBitmap = findViewById(R.id.btnPrintBitmap)
        btnPrintStr = findViewById(R.id.btnPrintStr)
        btnGetPayLaterPromotions = findViewById(R.id.btnGetPayLaterPromotions)
        btnGetPromotions = findViewById(R.id.btnGetPromotions)
        btnTransactionByFolio = findViewById(R.id.btnTransactionByFolio)
        btnGetPendingCheckouts = findViewById(R.id.btnGetPendingCheckouts)
        btnDoCheckout = findViewById(R.id.btnDoCheckout)
        btnReadCard = findViewById(R.id.btnReadCard)
        btnTerminalClosure = findViewById(R.id.btnTerminalClosure)
        btnPreTerminalClosure = findViewById(R.id.btnPreTerminalClosure)
        btnGenerateTerminalClosure = findViewById(R.id.btnGenerateTerminalClosure)
        btnTerminalClosureByID = findViewById(R.id.btnTerminalClosureById)
        btnInitKeys = findViewById(R.id.btnInitKeys)
        btnGetTokenStatus = findViewById(R.id.btnGetTokenStatus)
    }

    private fun addFunctions() {

        btnLoginApi!!.setOnClickListener { v: View? ->
            navigateTo(
                LoginApiView::class.java
            )
        }
        btnLogOut!!.setOnClickListener { v: View? ->
            navigateTo(
                LogOutView::class.java
            )
        }
        btnGetOrganizations!!.setOnClickListener { v: View? ->
            navigateTo(
                GetOrganizationsView::class.java
            )
        }
        btnChangeOrganization!!.setOnClickListener { v: View? ->
            navigateTo(
                ChangeOrganizationView::class.java
            )
        }
        btnOrganizationTransaction!!.setOnClickListener { v: View? ->
            navigateTo(
                OrganizationTransactionView::class.java
            )
        }
        btnGetOperations!!.setOnClickListener { v: View? ->
            navigateTo(
                GetOperationsView::class.java
            )
        }
        btnSetSignature!!.setOnClickListener { v: View? ->
            navigateTo(
                SetSignatureView::class.java
            )
        }
        btnPrintBitmap!!.setOnClickListener { v: View? ->
            navigateTo(
                PrintBitmapView::class.java
            )
        }
        btnPrintStr!!.setOnClickListener { v: View? ->
            navigateTo(
                PrintStrView::class.java
            )
        }
        btnGetPayLaterPromotions!!.setOnClickListener { v: View? ->
            navigateTo(
                GetPayLaterPromotionsView::class.java
            )
        }
        btnGetPromotions!!.setOnClickListener { v: View? ->
            navigateTo(
                GetPromotionsView::class.java
            )
        }
        btnTransactionByFolio!!.setOnClickListener { v: View? ->
            navigateTo(
                TransactionByFolioView::class.java
            )
        }
        btnGetPendingCheckouts!!.setOnClickListener { v: View? ->
            navigateTo(
                GetPendingCheckoutsView::class.java
            )
        }
        btnDoCheckout!!.setOnClickListener { v: View? ->
            navigateTo(
                DoCheckoutView::class.java
            )
        }
        btnReadCard!!.setOnClickListener { v: View? ->
            navigateTo(
                ReadCardView::class.java
            )
        }
        btnTerminalClosure!!.setOnClickListener { v: View? ->
            navigateTo(
                TerminalClosureView::class.java
            )
        }
        btnPreTerminalClosure!!.setOnClickListener { v: View? ->
            navigateTo(
                PreTerminalClosureView::class.java
            )
        }
        btnGenerateTerminalClosure!!.setOnClickListener { v: View? ->
            navigateTo(
                GenerateTerminalClosureView::class.java
            )
        }
        btnTerminalClosureByID!!.setOnClickListener { v: View? ->
            navigateTo(
                TerminalClosureByIDView::class.java
            )
        }
        btnInitKeys!!.setOnClickListener { v: View? ->
            navigateTo(
                InitKeysView::class.java
            )
        }
        btnGetTokenStatus!!.setOnClickListener { v: View? ->
            navigateTo(
                TokenStatus::class.java
            )
        }
    }

    private fun navigateTo(classToNavigate: Class<*>) {
        val intent = Intent(this, classToNavigate)
        startActivity(intent)
    }
}
