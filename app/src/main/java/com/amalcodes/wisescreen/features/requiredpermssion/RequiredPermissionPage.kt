package com.amalcodes.wisescreen.features.requiredpermssion

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.amalcodes.wisescreen.R
import com.amalcodes.wisescreen.core.Util
import com.amalcodes.wisescreen.presentation.components.SwitchText
import com.amalcodes.wisescreen.presentation.service.CurrentAppAccessibilityService
import kotlinx.coroutines.delay

@ExperimentalMaterial3Api
@Composable
fun RequiredPermissionPage(
    onExit: () -> Unit,
    goToUsageAccessSetting: () -> Unit,
    goToAccessibilitySetting: () -> Unit,
    onGranted: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val (isAccessibilityServiceActivated, setIsAccessibilityServiceActivated) = remember { mutableStateOf(false) }
    val (isUsageAccessGranted, setIsUsageAccessGranted) = remember { mutableStateOf(false) }
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            setIsAccessibilityServiceActivated(Util.isAccessibilityServiceGranted(context, CurrentAppAccessibilityService::class.java))
            setIsUsageAccessGranted(Util.isAppUsageStatsGranted(context))
        }
    }
    LaunchedEffect(isUsageAccessGranted, isAccessibilityServiceActivated) {
//        val isGranted = listOf(isAccessibilityServiceActivated, isUsageAccessGranted).all { it }
//        if (isGranted) onGranted()
        delay(500)
        onGranted()
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val (titleRef, imgRef, descriptionRef, switchGrantUsagePermissionRef, switchActivateServiceRef, btnRef) = createRefs()
            Text(
                modifier = Modifier.constrainAs(ref = titleRef) {
                    centerHorizontallyTo(parent)
                    top.linkTo(parent.top, 24.dp)
                },
                text = stringResource(id = R.string.text_title_permission_dialog),
                style = MaterialTheme.typography.headlineMedium
            )
            Image(
                modifier = Modifier.constrainAs(ref = imgRef) {
                    centerHorizontallyTo(parent)
                    height = Dimension.value(120.dp)
                    width = Dimension.fillToConstraints
                    top.linkTo(titleRef.bottom, 16.dp)
                },
                painter = painterResource(id = R.drawable.il_permission),
                contentDescription = stringResource(id = R.string.text_title_permission_dialog),
            )
            Text(
                modifier = Modifier.constrainAs(ref = descriptionRef) {
                    linkTo(start = parent.start, end = parent.end, startMargin = 24.dp, endMargin = 24.dp)
                    top.linkTo(imgRef.bottom, 16.dp)
                    width = Dimension.fillToConstraints
                },
                text = stringResource(id = R.string.text_description_permission_dialog),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
            SwitchText(
                modifier = Modifier
                    .constrainAs(ref = switchGrantUsagePermissionRef) {
                        top.linkTo(descriptionRef.bottom, 16.dp)
                        linkTo(start = parent.start, end = parent.end, startMargin = 16.dp, endMargin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
                    .padding(vertical = 8.dp),
                text = stringResource(id = R.string.text_Grant_Usage_Access),
                checked = isUsageAccessGranted,
                onCheckedChange = { if (it) goToUsageAccessSetting() },
            )
            SwitchText(
                modifier = Modifier
                    .constrainAs(ref = switchActivateServiceRef) {
                        top.linkTo(switchGrantUsagePermissionRef.bottom)
                        linkTo(start = parent.start, end = parent.end, startMargin = 16.dp, endMargin = 16.dp)
                        width = Dimension.fillToConstraints
                    }
                    .padding(vertical = 8.dp),
                text = stringResource(id = R.string.text_Activate_Service),
                checked = isAccessibilityServiceActivated,
                onCheckedChange = { if (it) goToAccessibilitySetting() },
            )
            TextButton(
                modifier = Modifier.constrainAs(ref = btnRef) {
                    centerHorizontallyTo(parent)
                    bottom.linkTo(parent.bottom, 16.dp)
                },
                onClick = onExit,
            ) {
                Text(text = stringResource(id = R.string.text_Exit))
            }
        }
    }
}