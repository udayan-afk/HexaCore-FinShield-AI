package com.aissms.finshield.ui.screens.compare

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aissms.finshield.model.InterestType
import com.aissms.finshield.model.LenderOfferMetrics
import com.aissms.finshield.model.LoanCategory
import com.aissms.finshield.model.MockLoanData
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.components.PrimaryFinShieldButton
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.BadgeReviewAmberBg
import com.aissms.finshield.ui.theme.BadgeReviewAmberText
import com.aissms.finshield.ui.theme.BorderNeutral
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.IconBoxGreenBg
import com.aissms.finshield.ui.theme.IconBoxGreenTint
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.SurfaceCard
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

enum class SortOption(val label: String) {
    TOTAL_COST("Lowest Cost"),
    INTEREST_RATE("Lowest Rate"),
    EMI("Lowest EMI")
}

@Composable
fun LoanComparisonEntryScreen(
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(LoanCategory.PERSONAL) }
    var searchQuery by remember { mutableStateOf("") }
    var principalAmount by remember { mutableDoubleStateOf(500000.0) }
    var tenureMonths by remember { mutableIntStateOf(36) }
    var sortBy by remember { mutableStateOf(SortOption.TOTAL_COST) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedLenderForDetails by remember { mutableStateOf<LenderOfferMetrics?>(null) }

    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 0
        }
    }

    // Filter offers by category and search term
    val filteredOffers = remember(selectedCategory, searchQuery) {
        MockLoanData.lenderCatalog.filter { offer ->
            val matchesCategory = offer.category == selectedCategory
            val matchesSearch = searchQuery.isBlank() ||
                    offer.lenderName.contains(searchQuery, ignoreCase = true) ||
                    offer.category.displayName.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    // Calculate metrics for current loan amount and tenure
    val offerMetrics = remember(filteredOffers, principalAmount, tenureMonths, sortBy) {
        val list = filteredOffers.map { it.calculateMetrics(principalAmount, tenureMonths) }
        when (sortBy) {
            SortOption.TOTAL_COST -> list.sortedBy { it.estimatedTotalCost }
            SortOption.INTEREST_RATE -> list.sortedBy { it.annualInterestRate }
            SortOption.EMI -> list.sortedBy { it.monthlyEmi }
        }
    }

    val lowestCostLender = remember(offerMetrics) {
        offerMetrics.minByOrNull { it.estimatedTotalCost }?.lenderName
    }

    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Compare loan offers",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.LoanComparison.route,
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Title & Subtitle
            Column {
                Text(
                    text = "Compare loan offers",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Same loan, clearer comparison across top lenders",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            // Compact Illustrative Data Notice Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = IconBoxGreenBg
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = IconBoxGreenTint,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Curated sample rates for transparent comparison. Official terms verified per bank scheme.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        fontSize = 11.5.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            // Search Bar & Category Selector Chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search bank name or loan type...", fontSize = 13.sp) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SurfaceCard,
                        unfocusedContainerColor = SurfaceCard,
                        focusedBorderColor = EmeraldGreen,
                        unfocusedBorderColor = BorderNeutral
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LoanCategory.entries.forEach { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = category },
                            label = {
                                Text(
                                    text = category.displayName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryNavy,
                                selectedLabelColor = Color.White,
                                containerColor = SurfaceCard,
                                labelColor = TextPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) PrimaryNavy else BorderNeutral
                            )
                        )
                    }
                }
            }

            // Loan Amount & Tenure Summary Card with Edit Action
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "COMPARING PARAMETERS",
                            style = MaterialTheme.typography.labelSmall,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 10.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${currencyFormatter.format(principalAmount)} · $tenureMonths Months",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${selectedCategory.displayName} offers (${offerMetrics.size} available)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.clickable { showEditDialog = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = EmeraldGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Edit",
                                color = Color.White,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Sort Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sort by:",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    SortOption.entries.forEach { option ->
                        val isSelected = sortBy == option
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) IconBoxGreenBg else SurfaceCard,
                            border = BorderStroke(1.dp, if (isSelected) EmeraldGreen else BorderNeutral),
                            modifier = Modifier.clickable { sortBy = option }
                        ) {
                            Text(
                                text = option.label,
                                color = if (isSelected) EmeraldGreen else TextSecondary,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }
                }
            }

            // Horizontally Scrollable Comparison Matrix Table
            if (offerMetrics.isNotEmpty()) {
                Text(
                    text = "Side-by-Side Matrix",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                ComparisonMatrixTable(
                    metricsList = offerMetrics,
                    lowestCostLender = lowestCostLender,
                    currencyFormatter = currencyFormatter
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Detailed Lender Breakdown",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                // Detailed Lender Cards List
                offerMetrics.forEach { metrics ->
                    LenderCardItem(
                        metrics = metrics,
                        isLowestCost = metrics.lenderName == lowestCostLender,
                        currencyFormatter = currencyFormatter,
                        onOfficialDetailsClick = { selectedLenderForDetails = metrics }
                    )
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = BorderStroke(1.dp, BorderNeutral)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No offers found for search",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Try adjusting your search query or category filter.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    // Edit Loan Amount & Tenure Dialog
    if (showEditDialog) {
        var tempPrincipal by remember { mutableDoubleStateOf(principalAmount) }
        var tempTenure by remember { mutableIntStateOf(tenureMonths) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Text(
                    text = "Adjust Loan Parameters",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Loan Amount", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                            Text(currencyFormatter.format(tempPrincipal), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                        }
                        Slider(
                            value = tempPrincipal.toFloat(),
                            onValueChange = { tempPrincipal = (it / 10000).toInt() * 10000.0 },
                            valueRange = 50000f..5000000f,
                            colors = SliderDefaults.colors(thumbColor = EmeraldGreen, activeTrackColor = EmeraldGreen)
                        )
                    }

                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tenure", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                            Text("$tempTenure Months", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = PrimaryNavy)
                        }
                        Slider(
                            value = tempTenure.toFloat(),
                            onValueChange = { tempTenure = (it / 6).toInt() * 6 },
                            valueRange = 12f..84f,
                            colors = SliderDefaults.colors(thumbColor = EmeraldGreen, activeTrackColor = EmeraldGreen)
                        )
                    }
                }
            },
            confirmButton = {
                PrimaryFinShieldButton(
                    text = "Apply Parameters",
                    onClick = {
                        principalAmount = tempPrincipal
                        tenureMonths = tempTenure
                        showEditDialog = false
                    },
                    showArrow = false
                )
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceCard,
            shape = RoundedCornerShape(20.dp)
        )
    }

    // Official Lender Scheme Details Dialog
    if (selectedLenderForDetails != null) {
        val details = selectedLenderForDetails!!
        AlertDialog(
            onDismissRequest = { selectedLenderForDetails = null },
            title = {
                Text(
                    text = details.lenderName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Scheme: ${details.category.displayName} Offer",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryNavy,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = details.officialDetailsNote,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Interest Method: ${details.interestType.label}\n• Rate: ${details.annualInterestRate}% p.a.\n• Processing Fee: ${currencyFormatter.format(details.processingFee)}\n• Indicative APR: ${details.indicativeApr}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedLenderForDetails = null }) {
                    Text("Close", color = EmeraldGreen, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = SurfaceCard,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
private fun ComparisonMatrixTable(
    metricsList: List<LenderOfferMetrics>,
    lowestCostLender: String?,
    currencyFormatter: NumberFormat
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.dp, BorderNeutral)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(14.dp)
        ) {
            // Metrics Header Column
            Column(modifier = Modifier.width(130.dp)) {
                MatrixHeaderCell(text = "Metric")
                MatrixLabelCell(text = "Interest Rate")
                MatrixLabelCell(text = "Calculation")
                MatrixLabelCell(text = "Monthly EMI")
                MatrixLabelCell(text = "Processing Fee")
                MatrixLabelCell(text = "Total Interest")
                MatrixLabelCell(text = "Total Cost")
                MatrixLabelCell(text = "Indicative APR")
            }

            // Lender Data Columns
            metricsList.forEach { item ->
                val isLowest = item.lenderName == lowestCostLender
                Column(
                    modifier = Modifier
                        .width(130.dp)
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Cell with Lender Name
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isLowest) IconBoxGreenBg else PrimaryNavy.copy(alpha = 0.06f),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                            Text(
                                text = item.lenderName,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (isLowest) EmeraldGreen else PrimaryNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }

                    MatrixValueCell(text = "${item.annualInterestRate}%")
                    MatrixValueCell(text = item.interestType.label, fontSize = 10.sp)
                    MatrixValueCell(text = currencyFormatter.format(item.monthlyEmi), isBold = true)
                    MatrixValueCell(text = currencyFormatter.format(item.processingFee))
                    MatrixValueCell(text = currencyFormatter.format(item.totalInterest))
                    MatrixValueCell(
                        text = currencyFormatter.format(item.estimatedTotalCost),
                        textColor = if (isLowest) EmeraldGreen else TextPrimary,
                        isBold = true
                    )
                    MatrixValueCell(text = "${item.indicativeApr}%")
                }
            }
        }
    }
}

@Composable
private fun MatrixHeaderCell(text: String) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.height(42.dp).fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MatrixLabelCell(text: String) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier.height(34.dp).fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.bodySmall, color = TextSecondary, fontSize = 11.5.sp)
    }
}

@Composable
private fun MatrixValueCell(
    text: String,
    textColor: Color = TextPrimary,
    isBold: Boolean = false,
    fontSize: androidx.compose.ui.unit.TextUnit = 12.sp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.height(34.dp).fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontSize = fontSize
        )
    }
}

@Composable
private fun LenderCardItem(
    metrics: LenderOfferMetrics,
    isLowestCost: Boolean,
    currencyFormatter: NumberFormat,
    onOfficialDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        border = BorderStroke(1.5.dp, if (isLowestCost) EmeraldGreen else BorderNeutral)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = metrics.lenderName,
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = metrics.category.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }

                if (isLowestCost) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = IconBoxGreenBg
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "LOWEST TOTAL COST",
                                color = EmeraldGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Badges row
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (metrics.interestType == InterestType.REDUCING_BALANCE) IconBoxGreenBg else BadgeReviewAmberBg
                ) {
                    Text(
                        text = metrics.interestType.label,
                        color = if (metrics.interestType == InterestType.REDUCING_BALANCE) IconBoxGreenTint else BadgeReviewAmberText,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = PrimaryNavy.copy(alpha = 0.06f)
                ) {
                    Text(
                        text = "Indicative APR: ${metrics.indicativeApr}%",
                        color = PrimaryNavy,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 2x2 Specs Grid
            Row(modifier = Modifier.fillMaxWidth()) {
                LenderSpecItem(label = "Interest Rate", value = "${metrics.annualInterestRate}% p.a.", modifier = Modifier.weight(1f))
                LenderSpecItem(label = "Monthly EMI", value = currencyFormatter.format(metrics.monthlyEmi), valueColor = EmeraldGreen, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                LenderSpecItem(label = "Processing Fee", value = currencyFormatter.format(metrics.processingFee), modifier = Modifier.weight(1f))
                LenderSpecItem(label = "Estimated Total Cost", value = currencyFormatter.format(metrics.estimatedTotalCost), valueColor = PrimaryNavy, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Official Lender Details Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onOfficialDetailsClick),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "View official scheme details",
                    style = MaterialTheme.typography.labelMedium,
                    color = EmeraldGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun LenderSpecItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = TextPrimary
) {
    Column(modifier = modifier) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextSecondary, fontSize = 11.sp)
        Spacer(modifier = Modifier.height(2.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
    }
}
