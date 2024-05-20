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
    private var btnGetTransactionById: Button? = null

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
        btnGetTransactionById = findViewById(R.id.btnTransactionById)
    }

    private fun addFunctions() {

        btnLoginApi!!.setOnClickListener {
            navigateTo(
                LoginApiView::class.java
            )
        }
        btnLogOut!!.setOnClickListener {
            navigateTo(
                LogOutView::class.java
            )
        }
        btnGetOrganizations!!.setOnClickListener {
            navigateTo(
                GetOrganizationsView::class.java
            )
        }
        btnChangeOrganization!!.setOnClickListener {
            navigateTo(
                ChangeOrganizationView::class.java
            )
        }
        btnOrganizationTransaction!!.setOnClickListener {
            navigateTo(
                OrganizationTransactionView::class.java
            )
        }
        btnGetOperations!!.setOnClickListener {
            navigateTo(
                GetOperationsView::class.java
            )
        }
        btnSetSignature!!.setOnClickListener {
            navigateTo(
                SetSignatureView::class.java
            )
        }
        btnPrintBitmap!!.setOnClickListener {
            navigateTo(
                PrintBitmapView::class.java
            )
        }
        btnPrintStr!!.setOnClickListener {
            navigateTo(
                PrintStrView::class.java
            )
        }
        btnGetPayLaterPromotions!!.setOnClickListener {
            navigateTo(
                GetPayLaterPromotionsView::class.java
            )
        }
        btnGetPromotions!!.setOnClickListener {
            navigateTo(
                GetPromotionsView::class.java
            )
        }
        btnTransactionByFolio!!.setOnClickListener {
            navigateTo(
                TransactionByFolioView::class.java
            )
        }
        btnGetPendingCheckouts!!.setOnClickListener {
            navigateTo(
                GetPendingCheckoutsView::class.java
            )
        }
        btnDoCheckout!!.setOnClickListener {
            navigateTo(
                DoCheckoutView::class.java
            )
        }
        btnReadCard!!.setOnClickListener {
            navigateTo(
                ReadCardView::class.java
            )
        }
        btnTerminalClosure!!.setOnClickListener {
            navigateTo(
                TerminalClosureView::class.java
            )
        }
        btnPreTerminalClosure!!.setOnClickListener {
            navigateTo(
                PreTerminalClosureView::class.java
            )
        }
        btnGenerateTerminalClosure!!.setOnClickListener {
            navigateTo(
                GenerateTerminalClosureView::class.java
            )
        }
        btnTerminalClosureByID!!.setOnClickListener {
            navigateTo(
                TerminalClosureByIDView::class.java
            )
        }
        btnInitKeys!!.setOnClickListener {
            navigateTo(
                InitKeysView::class.java
            )
        }
        btnGetTokenStatus!!.setOnClickListener {
            navigateTo(
                TokenStatus::class.java
            )
        }

        btnGetTransactionById!!.setOnClickListener {
            navigateTo(
                DetailTransactionById::class.java
            )
        }
    }

    private fun navigateTo(classToNavigate: Class<*>) {
        val intent = Intent(this, classToNavigate)
        startActivity(intent)
    }
}
