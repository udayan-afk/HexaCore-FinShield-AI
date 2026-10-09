package com.aissms.finshield.ui.screens.analyze

import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.components.OutlinedFinShieldButton
import com.aissms.finshield.ui.components.PrimaryFinShieldButton
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.BorderNeutral
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.IconBoxGreenBg
import com.aissms.finshield.ui.theme.IconBoxGreenTint
import com.aissms.finshield.ui.theme.IconBoxRedBg
import com.aissms.finshield.ui.theme.IconBoxRedTint
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.SurfaceCard
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoanAnalysisUploadScreen(
    onNavigateToReport: (String) -> Unit,
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileName by remember { mutableStateOf("HDFC_Personal_Loan.pdf") }
    var selectedFileSize by remember { mutableStateOf("1.8 MB") }
    var isAnalyzing by remember { mutableStateOf(false) }

    // SAF Document Picker for PDF
    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                if (cursor.moveToFirst()) {
                    if (nameIndex != -1) selectedFileName = cursor.getString(nameIndex)
                    if (sizeIndex != -1) {
                        val sizeBytes = cursor.getLong(sizeIndex)
                        selectedFileSize = String.format("%.1f MB", sizeBytes / (1024.0 * 1024.0))
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Analyze agreement",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.LoanAnalysisUpload.route,
                onNavigate = onNavigateTo
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stepper Header (Figma Image 3 & 4)
            AnalysisStepper(currentStep = 1)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Upload your loan agreement",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We'll extract key terms, verify repayment figures and flag clauses that deserve your attention.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 19.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (selectedFileUri == null && !isAnalyzing) {
                // Dashed Upload Box (Figma Image 3)
                DashedUploadCard(
                    onSelectPdfClick = { pdfPickerLauncher.launch("application/pdf") }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f).height(1.dp).background(BorderNeutral))
                    Text(
                        text = "  or  ",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.weight(1f).height(1.dp).background(BorderNeutral))
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Try Sample Agreement Link
                Row(
                    modifier = Modifier
                        .clickable {
                            selectedFileName = "HDFC_Personal_Loan.pdf"
                            selectedFileSize = "1.8 MB"
                            selectedFileUri = Uri.parse("sample://hdfc")
                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Try a sample agreement",
                        style = MaterialTheme.typography.titleMedium,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            } else {
                // File Selected Card (Figma Image 4)
                SelectedFileCard(
                    fileName = selectedFileName,
                    fileSize = selectedFileSize,
                    isAnalyzing = isAnalyzing,
                    onAnalyzeClick = {
                        isAnalyzing = true
                        scope.launch {
                            delay(1800)
                            isAnalyzing = false
                            onNavigateToReport(selectedFileName)
                        }
                    },
                    onChangeFileClick = {
                        selectedFileUri = null
                        pdfPickerLauncher.launch("application/pdf")
                    }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Privacy Card Box (Figma Image 3 & 4)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = IconBoxGreenBg
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = IconBoxGreenTint,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Your privacy comes first",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Your document is encrypted during processing and isn't shared with lenders.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "FinShield provides independent explanations, not legal or financial advice.",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 11.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AnalysisStepper(currentStep: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        StepItem(stepNumber = 1, title = "Upload", isActive = currentStep >= 1)
        Spacer(modifier = Modifier.weight(1f).height(2.dp).background(if (currentStep >= 2) EmeraldGreen else BorderNeutral))
        StepItem(stepNumber = 2, title = "Analyze", isActive = currentStep >= 2)
        Spacer(modifier = Modifier.weight(1f).height(2.dp).background(if (currentStep >= 3) EmeraldGreen else BorderNeutral))
        StepItem(stepNumber = 3, title = "Review", isActive = currentStep >= 3)
    }
}

@Composable
private fun StepItem(stepNumber: Int, title: String, isActive: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = if (isActive) EmeraldGreen else SurfaceCard,
            border = BorderStroke(1.5.dp, if (isActive) EmeraldGreen else BorderNeutral),
            modifier = Modifier.size(30.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stepNumber.toString(),
                    color = if (isActive) Color.White else TextSecondary,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) EmeraldGreen else TextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun DashedUploadCard(onSelectPdfClick: () -> Unit) {
    val stroke = Stroke(width = 2f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()
                drawRoundRect(
                    color = BorderNeutral,
                    style = stroke,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(20.dp.toPx())
                )
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = IconBoxGreenBg,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.FileUpload,
                        contentDescription = null,
                        tint = IconBoxGreenTint,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Select your agreement",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Choose a PDF from your device",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            OutlinedFinShieldButton(
                text = "Select PDF",
                onClick = onSelectPdfClick
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "PDF only - Maximum 20 MB",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun SelectedFileCard(
    fileName: String,
    fileSize: String,
    isAnalyzing: Boolean,
    onAnalyzeClick: () -> Unit,
    onChangeFileClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, BorderNeutral)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = IconBoxRedBg.copy(alpha = 0.4f),
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = IconBoxRedTint,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = IconBoxGreenBg
            ) {
                Text(
                    text = "READY TO ANALYZE",
                    color = IconBoxGreenTint,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = fileName,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "12 pages · $fileSize · PDF document",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "File is readable",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = IconBoxGreenTint,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "No password detected",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isAnalyzing) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = EmeraldGreen,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Analyzing agreement terms...",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                PrimaryFinShieldButton(
                    text = "Analyze Agreement",
                    onClick = onAnalyzeClick,
                    icon = Icons.Default.AutoAwesome,
                    showArrow = false
                )

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = onChangeFileClick) {
                    Text(
                        text = "Choose another file",
                        style = MaterialTheme.typography.titleMedium,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
